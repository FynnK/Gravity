
kernel void gravity(global const float* pos, global const float* vel, global float* result, int const size) {

    const int itemId = get_global_id(0)*1000;
    int index;

    for( index = 0; index < 1000;index++){
    int id = itemId+index;
        if(id < size){
            result[id] = pos[id] + vel[id];
        }
    }
}