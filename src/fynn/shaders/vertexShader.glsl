#version 330 core
layout(location = 0)
in vec3 position;
out vec4 out_color;
uniform mat4 viewMatrix;
uniform float pointSize;

void main()
{
    gl_Position = viewMatrix * vec4(position, 1.0);
    gl_PointSize = pointSize;
    float intensity = 0.4f;
    if(pointSize > 5){
        intensity = 0.1f;
    }
    out_color=vec4(0.9,0.1,0.2,intensity);
}