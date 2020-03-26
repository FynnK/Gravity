package fynn.model;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Instance {
    private ArrayList<Particle> particles;

    private FloatBuffer posBuffer;
    private FloatBuffer velBuffer;

    public Instance(ArrayList<Particle> pList) {
        this.particles = pList;
        updateBuffers();
    }

    private void updateBuffers(){
        float[] pos = new float[particles.size() * 3];
        float[] vel = new float[particles.size() * 3];


        for (int i = 0; i < particles.size(); i++) {
            Vector3f posV = particles.get(i).getPos();
            Vector3f velV = particles.get(i).getVel();

            pos[i * 3 + 0] = posV.x;
            pos[i * 3 + 1] = posV.y;
            pos[i * 3 + 2] = posV.z;

            vel[i * 3 + 0] = velV.x;
            vel[i * 3 + 1] = velV.y;
            vel[i * 3 + 2] = velV.z;
        }
        FloatBuffer posBufferTemp = BufferUtils.createFloatBuffer(pos.length);
        FloatBuffer velBufferTemp = BufferUtils.createFloatBuffer(vel.length);

        posBufferTemp.put(pos).flip();
        velBufferTemp.put(vel).flip();

        posBuffer = posBufferTemp;
        velBuffer = velBufferTemp;
    }



    public void update(ClManager cl) {
        posBuffer = cl.runSum(posBuffer,velBuffer);
    }


    public FloatBuffer getPosBuffer() {
        return this.posBuffer.rewind();
    }

    public int getNumParticles() {
        return particles.size();
    }
}
