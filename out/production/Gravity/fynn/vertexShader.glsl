#version 330 core
layout(location = 0)
in vec3 position;
uniform mat4 viewMatrix;

void main()
{
    gl_Position = viewMatrix * vec4(position, 1.0);
    gl_PointSize = (1-gl_Position.z)*5.0f +2;

}