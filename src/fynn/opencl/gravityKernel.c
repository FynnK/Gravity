
kernel void gravity(global const float* pos, global const float* vel, global float* result, int const size) {

    const int id = get_global_id(0)*3;
    int i;

    float cumulX = vel[id];
    float cumulY = vel[id + 1];
    float cumulZ = vel[id + 2];


    for(i = 0; i < size;i+=3){
        if(i == id){continue;}
        float dx = pos[i]-pos[id];
        float dy = pos[i+1]-pos[id+1];
        float dz = pos[i+2]-pos[id+2];

        float distsqr = dx*dx + dy*dy + dz*dz;
        float mag = sqrt(distsqr);

        float dirX = dx / mag;
        float dirY = dy / mag;
        float dirZ = dz / mag;

        cumulX += 0.06643f* dirX / distsqr;
        cumulY += 0.06643f * dirY / distsqr;
        cumulZ += 0.06643f * dirZ / distsqr;

    }

    result[id] = cumulX;
    result[id + 1] = cumulY;
    result[id + 2] = cumulZ;

}