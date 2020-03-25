#version 330 core

out vec4 fragColor;

void main()
{
    //ragColor = vec4(0.3f, 0.25f, 0.05f, 2.0f);
    fragColor = vec4(gl_FragCoord.x, gl_FragCoord.y, gl_FragCoord.x, 0.7f);
}
