#version 330 core
in vec4 out_color;
out vec4 fragColor;

void main()
{
    //ragColor = vec4(0.3f, 0.25f, 0.05f, 2.0f);
    float intensity = 0;
    float dx = (gl_PointCoord.x-0.5f)*2.0f;
    float dy = (gl_PointCoord.y-0.5f)*2.0f;
    intensity = cos(sqrt(dx*dx+dy*dy)*3.14159265357989324f)/1.41;
    fragColor = vec4(out_color.x,out_color.y, out_color.z, out_color.w * intensity);

}
