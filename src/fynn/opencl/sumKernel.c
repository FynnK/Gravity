
kernel void sum(global const float* a, global const float* b, global float* result, int const size) {

    const int itemId = get_global_id(0)*3;

    if(itemId < size-2) {
        result[itemId] = a[itemId] + b[itemId];
        result[itemId + 1] = a[itemId + 1] + b[itemId + 1];
        result[itemId + 2] = a[itemId + 2] + b[itemId + 2];

    }

}