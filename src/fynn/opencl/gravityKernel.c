kernel void gravity(global const float* pos, global const float* vel, global float* result, int const size, const float G) {

    const int id = get_global_id(0)*3;
    int i;


    local float cumulX;
    local float cumulY;
    local float cumulZ;

    cumulX = vel[id];
    cumulY = vel[id + 1];
    cumulZ = vel[id + 2];

    local     float dx;
    local     float dy;
    local     float dz;

    local     float distsqr;
    local     float mag;

    local     float dirX;
    local     float dirY;
    local     float dirZ;

    for(i = 0; i < size;i+=3){
        dx = pos[i]-pos[id];
        dy = pos[i+1]-pos[id+1];
        dz = pos[i+2]-pos[id+2];

        distsqr = dx*dx + dy*dy + dz*dz;

        mag = sqrt(distsqr);
        float k = mag*distsqr;
        float k2 = distsqr >= 1;

        cumulX += (G * dx/k)*k2;
        cumulY += (G * dy/k)*k2;
        cumulZ += (G * dz/k)*k2;

    }

    result[id] = cumulX;
    result[id + 1] = cumulY;
    result[id + 2] = cumulZ;



}