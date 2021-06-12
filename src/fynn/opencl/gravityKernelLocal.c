#define WORKGROUP_SIZE 128
kernel void gravity(global const float* pos, global const float* vel, global float* result, int const size, const float G) {

    const int id = get_global_id(0)*3;
    int i;

    int N=size; // this is total number of masses for this example
    int LN=WORKGROUP_SIZE;  // this is length of each chunk in local memory,
              // means 256 masses per compute unit

    int L=get_local_id(0);   // local thread id keys 0...255 for each group

     float cumulX;
     float cumulY;
     float cumulZ;

     cumulX = vel[id];
     cumulY = vel[id + 1];
    cumulZ = vel[id + 2];

     float pX = pos[id];
     float pY = pos[id+1];
     float pZ = pos[id+2];
     local xL[WORKGROUP_SIZE]; local yL[WORKGROUP_SIZE]; local zL[WORKGROUP_SIZE];
   //declare local mem array with constant length


   for(int k=0;k<N/LN;k++) // number of chunks to fetch from global to local
       {
           barrier(CLK_LOCAL_MEM_FENCE);  //synchronization
           xL[L]=pos[k*LN*3+L]; yL[L]=pos[3*k*LN+L+1]; zL[L]=pos[3*k*LN+L+2]; //get 256-element chunks into local mem
           barrier(CLK_LOCAL_MEM_FENCE);  //synchronization

           for(int j=0;j<LN;j++)          //start processing local/private variables
           {

               float dx;
               float dy;
               float dz;

               float distsqr;
               float mag;

               float dirX;
               float dirY;
               float dirZ;

               dx = xL[j]-pX;
               dy = yL[j]-pY;
               dz = zL[j]-pZ;

               distsqr = dx*dx + dy*dy + dz*dz;

               mag = sqrt(distsqr);
               float k = mag*distsqr;
               float k2 = distsqr >= 1;

                cumulX += (G * dx/k)*k2;
                cumulY += (G * dy/k)*k2;
                cumulZ += (G * dz/k)*k2;

           }
        }


    result[id] =    cumulX;
    result[id + 1] = cumulY;
    result[id + 2] = cumulZ;



}