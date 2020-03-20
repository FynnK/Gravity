package fynn;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;


public class SimRenderer extends Game {
    private ShaderProgram shaderProgram;
    private int viewMatrixLocation;

    private int vaoID;
    private int vboID;
    private int numParticles = 3;
    Simulation simulation;
    private float rotation = 0.5f;
    Camera cam;

    public void init() {
        simulation = new Simulation(numParticles);
        cam = new Camera(new Vector3f(0.0f, 0, 1.0f), new Vector3f(0, 0, 1));


        glfwSetWindowTitle(Game.getWindowID(), "Graviteeeyyy");

        shaderProgram = new ShaderProgram();
        shaderProgram.attachVertexShader("fynn/vertexShader.glsl");
        shaderProgram.attachFragmentShader("fynn/fragmentShader.glsl");
        shaderProgram.link();


        float[] vertices = simulation.getVertices();
        // The vertices of our Points

        // Generate and bind a Vertex Array
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);


        // Create a FloatBuffer of vertices
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();

        // Create a Buffer Object and upload the vertices buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_DYNAMIC_DRAW);

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

        float[] vertices = simulation.getVertices();
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_DYNAMIC_DRAW);



        simulation.update(dt);

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
       // cam.rotateY(rotation);
        cam.getViewMatrix().get(mvpMat);

        glUniformMatrix4fv(viewMatrixLocation, false, mvpMat);
        glDrawArrays(GL_POINTS, 0, numParticles);
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

    public static void main(String[] args) {
        new SimRenderer().start();
    }
}