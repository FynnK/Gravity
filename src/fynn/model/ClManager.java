package fynn.model;


import fynn.util.FileUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static fynn.opencl.InfoUtil.checkCLError;
import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memUTF8;


public final class ClManager {
    private CLContextCallback clContextCB;
    private long clContext;
    private IntBuffer errcode_ret;
    private long clSumKernel;
    private long clDevice;
    private long clQueue;
    private long posMemory;
    private long velMemory;
    private long clPlatform;
    private CLCapabilities clPlatformCapabilities;
    private long resultMemory;
    private int size;
    int errcode;
    private boolean memInit = false;

    private long sumProgram;


    public ClManager() {
        initializeCL();
    }


    private long createProgram(String source) {

        long clProgram = CL10.clCreateProgramWithSource(clContext, source, errcode_ret);

        errcode = clBuildProgram(clProgram, clDevice, "", null, NULL);
        checkCLError(errcode);

        clSumKernel = clCreateKernel(clProgram, "sum", errcode_ret);
        checkCLError(errcode_ret);
        return clProgram;
    }

    public void init(int num) {
        sumProgram = createProgram(FileUtil.readFromFile("fynn/opencl/sumKernel.c"));
        size = num * 3;
    }


    public FloatBuffer runSum(FloatBuffer a, FloatBuffer b) {
        copytoMemory(a,b);

        final int dimensions = 1;
        PointerBuffer globalWorkSize = BufferUtils.createPointerBuffer(dimensions); // In here we put the total number of work items we want in each dimension.

        globalWorkSize.put(0, size); // Size is a variable we defined a while back showing how many
        // elements are in our arrays.

        // Run the specified number of work units using our OpenCL program kernel
        errcode = clEnqueueNDRangeKernel(clQueue, clSumKernel, dimensions, null, globalWorkSize, null,null, null);
        CL10.clFinish(clQueue);


        FloatBuffer resultBuff = BufferUtils.createFloatBuffer(size);
        CL10.clEnqueueReadBuffer(clQueue, resultMemory, true, 0, resultBuff, null, null);
        System.out.println("result at " + 33 + " = " + resultBuff.get(33));
        return resultBuff;
    }

    public void copytoMemory(FloatBuffer pos, FloatBuffer vel) {
        if (memInit == false) {
            createMemory(pos, vel);
        } else {
            CL10.clEnqueueWriteBuffer(clQueue, posMemory, true,0, pos, null, null);
            CL10.clEnqueueWriteBuffer(clQueue, velMemory, true,0, vel, null, null);
        }
    }

    private void createMemory(FloatBuffer pos, FloatBuffer vel) {
        // Create OpenCL memory object containing the first buffer's list of numbers
        posMemory = CL10.clCreateBuffer(clContext, CL10.CL_MEM_WRITE_ONLY | CL10.CL_MEM_COPY_HOST_PTR,
                pos, errcode_ret);
        checkCLError(errcode_ret);


        // Create OpenCL memory object containing the second buffer's list of numbers
        velMemory = CL10.clCreateBuffer(clContext, CL10.CL_MEM_WRITE_ONLY | CL10.CL_MEM_COPY_HOST_PTR,
                vel, errcode_ret);
        checkCLError(errcode_ret);

        System.out.println(pos.get(33));
        pos.rewind();
        System.out.println(vel.get(33));
        vel.rewind();

        // Remember the length argument here is in bytes. 4 bytes per float.
        resultMemory = CL10.clCreateBuffer(clContext, CL10.CL_MEM_READ_ONLY, size*4 , errcode_ret);
        checkCLError(errcode_ret);

        clSetKernelArg1p(clSumKernel, 0, posMemory);
        clSetKernelArg1p(clSumKernel, 1, velMemory);
        clSetKernelArg1p(clSumKernel, 2, resultMemory);
        clSetKernelArg1i(clSumKernel, 3, size);

        memInit = true;
    }

    public void destroy() {
        cleanup();
    }

    private void cleanup() {
        // Destroy our kernel and program
        CL10.clReleaseCommandQueue(clQueue);
        CL10.clReleaseKernel(clSumKernel);
        CL10.clReleaseProgram(sumProgram);

        // Destroy our memory objects
        CL10.clReleaseMemObject(posMemory);
        CL10.clReleaseMemObject(velMemory);
        CL10.clReleaseMemObject(resultMemory);

        // Not strictly necessary
        CL.destroy();
    }

    public void initializeCL() {
        errcode_ret = BufferUtils.createIntBuffer(1);

        // Get the first available platform
        try (MemoryStack stack = stackPush()) {
            IntBuffer pi = stack.mallocInt(1);
            checkCLError(clGetPlatformIDs(null, pi));
            if (pi.get(0) == 0) {
                throw new IllegalStateException("No OpenCL platforms found.");
            }

            PointerBuffer platformIDs = stack.mallocPointer(pi.get(0));
            checkCLError(clGetPlatformIDs(platformIDs, (IntBuffer) null));

            for (int i = 0; i < platformIDs.capacity() && i == 0; i++) {
                long platform = platformIDs.get(i);
                clPlatformCapabilities = CL.createPlatformCapabilities(platform);
                clPlatform = platform;
            }
        }


        clDevice = getDevice(clPlatform, clPlatformCapabilities, CL_DEVICE_TYPE_GPU);

        // Create the context
        PointerBuffer ctxProps = BufferUtils.createPointerBuffer(7);
        ctxProps.put(CL_CONTEXT_PLATFORM).put(clPlatform).put(NULL).flip();

        clContext = clCreateContext(ctxProps, clDevice, clContextCB = CLContextCallback.create((errinfo, private_info, cb, user_data) -> System.out.printf("cl_context_callback\n\tInfo: %s", memUTF8(errinfo))),
                NULL, errcode_ret);

        // create command queue
        clQueue = clCreateCommandQueue(clContext, clDevice, NULL, errcode_ret);
        checkCLError(errcode_ret);
    }

    private static long getDevice(long platform, CLCapabilities platformCaps, int deviceType) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pi = stack.mallocInt(1);
            checkCLError(clGetDeviceIDs(platform, deviceType, null, pi));

            PointerBuffer devices = stack.mallocPointer(pi.get(0));
            checkCLError(clGetDeviceIDs(platform, deviceType, devices, (IntBuffer) null));

            for (int i = 0; i < devices.capacity(); i++) {
                long device = devices.get(i);

                CLCapabilities caps = CL.createDeviceCapabilities(device, platformCaps);
                if (!(caps.cl_khr_gl_sharing || caps.cl_APPLE_gl_sharing)) {
                    continue;
                }

                return device;
            }
        }

        return NULL;
    }


}