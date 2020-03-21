package fynn;

import org.joml.Vector3f;

import java.util.ArrayList;

public class Instance {
    private static ArrayList<Particle> particles;
    private static long timestamp;

    public Instance(ArrayList<Particle> pList, long timestamp) {
        this.particles = pList;
        this.timestamp = timestamp;


    }

    public static void addParticles(ArrayList<Particle> pList){
        particles.addAll(pList);
        System.out.println("added " +pList.size() + " particles");
    }

    public void setTimestamp(long t) {
        this.timestamp = t;
    }

    public long getTimestamp() {
        return timestamp;
    }


    public float[] getVertices() {
        float[] data = new float[particles.size() * 3];
        for (int i = 0; i < particles.size(); i++) {
            data[i * 3 + 0] = particles.get(i).getPos().x;
            data[i * 3 + 1] = particles.get(i).getPos().y;
            data[i * 3 + 2] = particles.get(i).getPos().z;
        }
        return data;
    }


    public void update(float dt) {
        for (Particle p : this.particles) {
            Vector3f cumulForce = new Vector3f(0.0f);

            for(Particle p2 : this.particles){

                if(p2 == p){continue;}

                Vector3f pos1 = p.getPos();
                Vector3f pos2 = p2.getPos();
                Vector3f dir = new Vector3f(0.0f);

                float distsqr = pos2.distanceSquared(pos1);

                pos2.sub(pos1, dir);
                dir.normalize();

                dir.mul(1/distsqr);
                cumulForce.add(dir);
            }
         p.accel(cumulForce, dt);
        }
        for (Particle p : this.particles) {
            p.setPos(p.getPos().add(p.getVel()));

        }
    }

    public int getNumParticles() {
        return particles.size();
    }
}
