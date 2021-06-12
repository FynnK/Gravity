package fynn.renderer;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Game implements Runnable {
    private static long windowID;

    public Game(){

    }


    public void createWindow() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            System.err.println("Error initializing GLFW");
            System.exit(1);
        }

        // Window Hints for OpenGL context
        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        windowID = glfwCreateWindow(640, 480, "My GLFW Window", NULL, NULL);

        if (windowID == NULL) {
            System.err.println("Error creating a window");
            System.exit(1);
        }

        glfwShowWindow(windowID);

        glfwMakeContextCurrent(windowID);
        GL.createCapabilities();

        glfwSwapInterval(1);
    }


    public void end() {
        glfwSetWindowShouldClose(windowID, true);
    }

    public void windowMoved(int x, int y) {
        System.out.println(String.format("Window moved: [%d, %d]", x, y));
    }

    public void windowResized(int width, int height) {
        System.out.println(String.format("Window resized: [%d, %d]", width, height));
        glViewport(0, 0, width, height);

    }

    public void windowClosing() {
        System.out.println("Window closing");
    }

    public void windowFocusChanged(boolean focused) {
        System.out.println("Window focus changed: FOCUS = " + focused);
    }

    public void windowIconfyChanged(boolean iconified) {
        System.out.println("Window iconified/restored: ICONIFIED = "+ iconified);
    }

    public void framebufferResized(int width, int height) {
        System.out.println("Framebuffer resized: "+ width+ ", "+ height);
    }

    public void keyPressed(int key, int scancode, int action, int mods) {

    }

    public void mouseScroll(double xOffset, double yOffset) {
    }

    public void init() {
    }

    public void update(float delta) {
    }

    public void render(float delta) {
    }

    public void dispose() {
    }

    public static long getWindowID() {
        return windowID;
    }



    @Override
    public void run() {
        createWindow();
        float now, delta, last = 0;

        // Set the callbacks
        glfwSetWindowPosCallback(windowID, (window, x, y) -> windowMoved(x, y));
        glfwSetWindowSizeCallback(windowID, (window, width, height) -> windowResized(width, height));
        glfwSetWindowCloseCallback(windowID, window -> windowClosing());
        glfwSetWindowFocusCallback(windowID, (window, focused) -> windowFocusChanged(focused));
        glfwSetWindowIconifyCallback(windowID, (window, iconified) -> windowIconfyChanged(iconified));
        glfwSetFramebufferSizeCallback(windowID, (window, width, height) -> framebufferResized(width, height));
        glfwSetKeyCallback(windowID, (window, key, scancode, action, mods) -> keyPressed(key, scancode, action, mods));
        glfwSetScrollCallback(windowID, (window, xOffset, yOffset) -> mouseScroll(xOffset, yOffset));
        glfwSetMouseButtonCallback(windowID, (window, button, action, mods) -> mouseButton(button, action, mods));
        glfwSetCursorPosCallback(windowID, ((window, xpos, ypos) -> mousePos(xpos, ypos)));


        // Initialize the game
        init();

        // Loop continuously and render and update
        while (!glfwWindowShouldClose(windowID)) {
            now = (float) glfwGetTime();
            delta = now - last;
            last = now;
            //System.out.println("fps: "+ 1 /delta);
            // Update and Render
            update(delta);
            render(delta);

            // Poll the events and swap the buffers
            glfwPollEvents();
            glfwSwapBuffers(windowID);
        }

        // Dispose the game
        dispose();

        // Free the callbacks

            glfwSetWindowPosCallback(windowID, null).free();
            glfwSetWindowSizeCallback(windowID, null).free();
            glfwSetWindowCloseCallback(windowID, null).free();
            glfwSetWindowFocusCallback(windowID, null).free();
            glfwSetWindowIconifyCallback(windowID, null).free();
            glfwSetFramebufferSizeCallback(windowID, null).free();



        // Destroy the window
        glfwDestroyWindow(windowID);
        glfwTerminate();

        System.exit(0);
    }

    public void mousePos(double xpos, double ypos) {
    }

    public void mouseButton(int button, int action, int mods) {
    }
}