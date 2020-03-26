package fynn.renderer;

import fynn.util.FileUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * ShaderProgram Class. Used to load and use Vertex and Fragment shaders easily.
 *
 * @author Sri Harsha Chilakapati
 */
public class ShaderProgram
{
    int programID;

    int vertexShaderID;
    int fragmentShaderID;

    public ShaderProgram()
    {
        programID = glCreateProgram();
    }


    public void attachVertexShader(String name)
    {
        // Load the source
        String vertexShaderSource = FileUtil.readFromFile(name);

        // Create the shader and set the source
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID, vertexShaderSource);

        glCompileShader(vertexShaderID);

        // Check for errors
        if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Error creating vertex shader\n"
                    + glGetShaderInfoLog(vertexShaderID, glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH)));

        glAttachShader(programID, vertexShaderID);
    }


    public void attachFragmentShader(String name)
    {
        String fragmentShaderSource = FileUtil.readFromFile(name);

        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentShaderSource);

        glCompileShader(fragmentShaderID);

        if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Error creating fragment shader\n"
                    + glGetShaderInfoLog(fragmentShaderID, glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH)));

        // Attach the shader
        glAttachShader(programID, fragmentShaderID);
    }


    public void link() {
        glLinkProgram(programID);
        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException("Unable to link shader program:");
    }

    public void bind()
    {
        glUseProgram(programID);
    }

    public static void unbind()
    {
        glUseProgram(0);
    }


    public void dispose()
    {
        // Unbind the program
        unbind();

        // Detach the shaders
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);

        // Delete the shaders
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);

        // Delete the program
        glDeleteProgram(programID);
    }

    public int getID()
    {
        return programID;
    }
}