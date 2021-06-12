package fynn.renderer;

import fynn.*;
import fynn.model.Instance;
import fynn.util.InputUtil;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.util.concurrent.BlockingQueue;

import static fynn.MagicNumbers.rotationConst;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;


public class SimRenderer extends Game {
    private ShaderProgram shaderProgram;
    private int viewMatrixLocation;

    boolean traces = false;

    private int vaoID;
    private int vboID;
    protected BlockingQueue<Instance> bq;
    private Instance renderInstance;
    Camera cam;
    private int pointSizeLocation;
    private Vector4f backgroundColor = new Vector4f(0,0,0,0);

    public SimRenderer(BlockingQueue<Instance> bq) {
        this.bq = bq;
    }

    public void updateRenderInstance(){
        try {
            renderInstance = bq.take();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void init() {

        cam = new Camera(new Vector3f(0, 0.5f, 0), new Vector3f(0, 0, 0));
        glfwSetWindowTitle(Game.getWindowID(), "Graviteeeyyy");
        glfwSetCursorPosCallback(Game.getWindowID(), (window, xpos, ypos) -> mousePos(xpos, ypos));
        glfwSetMouseButtonCallback(Game.getWindowID(), (window, button, action, mods) -> mouseButton(button, action, mods));

        shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("fynn/shaders/vertexShader.glsl");
        shaderProgram.attachFragmentShader("fynn/shaders/fragmentShader.glsl");
        shaderProgram.link();

        updateRenderInstance();
        // The vertices of our Points
        // Generate and bind a Vertex Array
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        // Create a FloatBuffer of vertices

        // Create a Buffer Object and upload the vertices buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, renderInstance.getPosBuffer(), GL_DYNAMIC_DRAW);

        // Point the buffer at location 0, the location we set
        // inside the vertex shader. You can use any location
        // but the locations should match
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindVertexArray(0);

        glEnable(GL_BLEND);
        glEnable(GL_PROGRAM_POINT_SIZE);

        viewMatrixLocation = glGetUniformLocation(shaderProgram.programID, "viewMatrix");
        pointSizeLocation = glGetUniformLocation(shaderProgram.programID, "pointSize");

    }

    public void update(float dt) {
        if(bq.remainingCapacity() < 10){
            updateRenderInstance();
        }
        glBufferData(GL_ARRAY_BUFFER, renderInstance.getPosBuffer(), GL_DYNAMIC_DRAW);

    }


    double lastPosX = 0, lastPosY = 0;

    public void mousePos(double xpos, double ypos) {
        System.out.println(xpos + " , " + ypos);
        if(mouseState0 == 1){
            float dx = (float) (xpos - lastPosX);
            float dy =  (float) (ypos - lastPosY);

            cam.rotateX(dy * 0.3f);
            cam.rotateY(-dx * 0.3f);

            lastPosX = xpos;
            lastPosY = ypos;
        }
    }


    int mouseState0;
    int mouseState1;
    int mouseState2;


    public void mouseButton(int button, int action, int mods) {
        System.out.println("button: " + button + " action: " + action);
        switch (button) {
            case 0:
                mouseState0 = action;
                break;
            case 1:
                mouseState1 = action;
                break;
            case 2:
                mouseState2 = action;
                break;
        }
    }

    public void keyPressed(int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            glfwSetWindowShouldClose(Game.getWindowID(), true); // We will detect this in the rendering loop
        if (key == GLFW_KEY_1 && action == GLFW_RELEASE) {
            cam.scaleUp();
        }
        if (key == GLFW_KEY_2 && action == GLFW_RELEASE) {
            cam.scaleDown();
        }
        if (key == GLFW_KEY_3 && action == GLFW_RELEASE) {
            traces = !traces;
        }
        if (key == GLFW_KEY_4 && action == GLFW_RELEASE) {
            InputUtil.shouldReset = true;
            InputUtil.instanceType = 1;
        }
        if (key == GLFW_KEY_5 && action == GLFW_RELEASE) {
            InputUtil.shouldReset = true;
            InputUtil.instanceType  = 2;
        }
        if (key == GLFW_KEY_6 && action == GLFW_RELEASE) {
            InputUtil.shouldReset = true;
            InputUtil.instanceType  = 3;
        }if (key == GLFW_KEY_0 && action == GLFW_RELEASE) {
            if(backgroundColor.x == 0){
                backgroundColor = new Vector4f(0.9f,0.9f,0.9f,1f);
            }else {
                backgroundColor = new Vector4f(0,0,0,0);
            }
        }
        if (key == GLFW_KEY_0 && action == GLFW_RELEASE) {
            cam.move(new Vector3f(0,1,0));
        }
        if (key == GLFW_KEY_9 && action == GLFW_RELEASE) {
            cam.rotateX(0.1f);
        }


        }

    public void mouseScroll(double x, double y) {
        if (y > 0) {
            cam.scaleUp();

        } else {
            cam.scaleDown();
        }
    }


    public void render(float delta) {
        // Clear the screen

        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, backgroundColor.w);
        if(!traces){
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        }else{
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        }
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        // Use our program
        shaderProgram.bind();

        // Bind the vertex array and enable our location
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);

        float[] mvpMat = new float[16];
        cam.rotateY(rotationConst);
        cam.getViewMatrix().get(mvpMat);

        glUniformMatrix4fv(viewMatrixLocation, false, mvpMat);
        glUniform1f(pointSizeLocation, 20);
        glDrawArrays(GL_POINTS, 0, renderInstance.getNumParticles());
        glUniform1f(pointSizeLocation, 4);
        glDrawArrays(GL_POINTS, 0, renderInstance.getNumParticles());

        // Disable our location
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        // Un-bind our program
        ShaderProgram.unbind();
    }

    public void dispose() {
        // Dispose the program
        shaderProgram.dispose();

        // Dispose the vertex array
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);

        // Dispose the buffer object
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboID);
    }
}