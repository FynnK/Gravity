#define G 0.006643f
kernel void gravity(global const float* pos, global const float* vel, global float* result, int const size) {


// global memory access is only 257 times per item, 1 for private save
//                                                  256 for global broadcast
//                                                  for global-to-local copy
// unoptimized version accesses 65537 times per item.
__kernel void nBodyF(__global float *x, __global float *y,
                     __global float *vx, __global float *vy,
                     __global float *fx, __global float *fy)
{
     int N=65536; // this is total number of masses for this example
     int LN=256;  // this is lengthjo
      of each chunk in local memory,
                  // means 256 masses per compute unit
    int i=get_global_id(0);  // global thread id keys 0....65535
    int L=get_local_id(0);   // local thread id keys 0...255 for each group
    float2 Fi=(float2)(0,0); // init
    float xi=x[i]; float yi=y[i]; // re-use for 65536 times
    __local xL[256]; __local yL[256]; //declare local mem array with constant length


    for(int k=0;k<N/LN;k++) // number of chunks to fetch from global to local
    {
        barrier(CLK_LOCAL_MEM_FENCE);  //synchronization
        xL[L]=x[k*LN+L]; yL[L]=y[k*LN+L]; //get 256-element chunks into local mem
        barrier(CLK_LOCAL_MEM_FENCE);  //synchronization
        for(int j=0;j<LN;j++)          //start processing local/private variables
        {
            float2 F=(float2)(0,0);          // private force vector init
            float2 r1=(float2)(xi,yi);       // private vector
            float2 r2=(float2)(xL[j],yL[j]); // use local mem to get r2 vector
            float2 dr=r1-r2;                 // private displacement
            F=dr/(0.01f+dot(dr,dr));         // private force calc.
            Fi.x-=F.x; Fi.y-=F.y;            // private force add to private
        }
     }
     fx[i]=Fi.x; fy[i]=Fi.y; //write result to global mem only once
}