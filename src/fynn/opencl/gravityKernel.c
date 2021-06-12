kernel void gravity(global const float* pos, global const float* vel, global float* result, int const size, const float G) {

    const int id = get_global_id(0)*3;
    int i;

    int N=size; // this is total number of masses for this example
    int LN=256;  // this is length of each chunk in local memory,
              // means 256 masses per compute unit

    int L=get_local_id(0);   // local thread id keys 0...255 for each group

     float cumulX;
     float cumulY;
     float cumulZ;

    cumulX = vel[id];
    cumulY = vel[id + 1];
    cumulZ = vel[id + 2];

    float xi=pos[id]; float yi=pos[id+1]; float zi = pos[id+2]; // re-use for 65536 times
    __local xL[256]; __local yL[256]; __local zL[256];
   //declare local mem array with constant length

    float dx;
    float dy;
    float dz;

    float distsqr;
    float mag;

    float dirX;
    float dirY;
    float dirZ;

    float pX = pos[id];
    float pY = pos[id+1];
    float pZ = pos[id+2];

    for(i = 0; i < size;i+=3){
        dx = pos[i]-pX;
        dy = pos[i+1]-pY;
        dz = pos[i+2]-pZ;

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