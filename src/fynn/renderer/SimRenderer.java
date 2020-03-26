package fynn.renderer;

import fynn.model.Instance;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.concurrent.BlockingQueue;

import static fynn.MagicNumbers.rotationConst;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;


public class SimRenderer extends Game {
    private ShaderProgram shaderProgram;
    private int viewMatrixLocation;

    private int vaoID;
    private int vboID;
    protected BlockingQueue<Instance> bq = null;
    private Instance renderInstance;
    Camera cam;

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
        cam = new Camera(new Vector3f(0.0f, 0, 1.0f), new Vector3f(0, 0, 1));
        glfwSetWindowTitle(Game.getWindowID(), "Graviteeeyyy");

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
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        glEnable(GL_PROGRAM_POINT_SIZE);
        //glPointSize(10);

        viewMatrixLocation = glGetUniformLocation(shaderProgram.programID, "viewMatrix");

    }

    public void update(float dt) {
        if(bq.remainingCapacity() < 10){
            updateRenderInstance();
        }
        glBufferData(GL_ARRAY_BUFFER, renderInstance.getPosBuffer().rewind(), GL_DYNAMIC_DRAW);

    }

    public void keyPressed(int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            glfwSetWindowShouldClose(Game.getWindowID(), true); // We will detect this in the rendering loop
        if (key == GLFW_KEY_0 && action == GLFW_RELEASE) {
            //simulation.randomize(numParticles);
        }
        if (key == GLFW_KEY_1 && action == GLFW_RELEASE) {
            cam.scaleUp();
        }
        if (key == GLFW_KEY_2 && action == GLFW_RELEASE) {
            cam.scaleDown();
        }
    }

    public void mouseScroll(double x, double y) {
        if (y > 0) {
            //cam.scaleUp();
            cam.rotateY(10);
        } else {
            cam.rotateY(-10);
            //cam.scaleDown();
        }
    }


    public void render(float delta) {
        // Clear the screen
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Use our program
        shaderProgram.bind();

        // Bind the vertex array and enable our location
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);


        Matrix4f mvp = cam.getViewMatrix();
        float[] mvpMat = new float[16];
        cam.rotateY(rotationConst);
        cam.getViewMatrix().get(mvpMat);

        glUniformMatrix4fv(viewMatrixLocation, false, mvpMat);
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