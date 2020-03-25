package fynn.model;

import fynn.util.particleFactory;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL;
import org.lwjgl.opencl.CLCapabilities;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import static fynn.MagicNumbers.*;
import static fynn.opencl.InfoUtil.checkCLError;
import static org.lwjgl.opencl.CL10.clGetDeviceIDs;
import static org.lwjgl.opencl.CL10.clGetPlatformIDs;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Simulation implements Runnable {
    private Instance renderInstance;
    private Instance workInstance;
    particleFactory pFact;
    ClAccelerator clAcc;

    BlockingQueue bq = null;
    boolean running = true;

    public Simulation(int numberOfParticles, BlockingQueue bq) {
        pFact = new particleFactory();
        clAcc = new ClAccelerator();
        workInstance = pFact.createInstance(numberOfParticles,pScale, vScale);
        renderInstance = workInstance;
        this.bq = bq;
    }


    public void addParticles(int num){
        ArrayList<Particle> pL = pFact.createParticles(num, pScale, 0.1f);
        workInstance.addParticles(pL);
    }

    public float[] getVertices() {
        return renderInstance.getVertices();
    }


    public void update(float dt) {
        if (bq.remainingCapacity() == 0) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        renderInstance = workInstance;

        try {
            bq.put(renderInstance);

        } catch (Exception e) {
            e.printStackTrace();
        }

        workInstance.update(dt);

    }

    @Override
    public void run() {
        while (running) {
            update(dT);
        }
    }

}

