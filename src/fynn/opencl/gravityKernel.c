#define G 0.006643f
kernel void gravity(global const float* pos, global const float* vel, global float* result, int const size) {

    const int id = get_global_id(0)*3;
    int i;

    float cumulX = vel[id];
    float cumulY = vel[id + 1];
    float cumulZ = vel[id + 2];

    float dx;
    float dy;
    float dz;

    float distsqr;
    float mag;

    float dirX;
    float dirY;
    float dirZ;


    for(i = 0; i < size;i+=3){
        if(i == id){continue;}
        dx = pos[i]-pos[id];
        dy = pos[i+1]-pos[id+1];
        dz = pos[i+2]-pos[id+2];

        distsqr = dx*dx + dy*dy + dz*dz;
        mag = sqrt(distsqr);

        cumulX += G * (dx/mag) / distsqr;
        cumulY += G * (dy/mag) / distsqr;
        cumulZ += G * (dz/mag) / distsqr;

    }

    result[id] = cumulX;
    result[id + 1] = cumulY;
    result[id + 2] = cumulZ;

}