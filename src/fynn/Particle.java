package fynn;

import org.joml.Vector3f;


public class Particle {


    private Vector3f pos;
    private Vector3f vel;

    public Particle(Vector3f pos, Vector3f vel) {
        this.pos = pos;
        this.vel = vel;
    }

    public Particle(Vector3f pos) {

        this.pos = pos;
        this.vel = new Vector3f(0);

    }

    public Vector3f getPos() {
        return pos;
    }

    public void move(float dt) {
        setPos(pos.add(vel.mul(dt)));
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector3f getVel() {
        return vel;
    }


    public void setVel(Vector3f vel) {
        this.vel = vel;
    }

    public void accel(Vector3f cumulForce, float dt) {
        cumulForce.mul(dt);
        vel.add(cumulForce, vel);
    }
}
