package fynn.model;

import org.joml.Vector3f;


public class Particle extends centerOM{


    private Vector3f pos;
    private Vector3f vel;

    public Particle(Vector3f pos, Vector3f vel, float mass){
        super(pos, mass);
        this.vel = vel;
    }

    public Particle(Vector3f pos, Vector3f vel) {
        super(pos, 1);
        this.vel = vel;
    }

    public Particle(Vector3f pos) {
        super(pos, 1);
        this.vel = new Vector3f(0);

    }

    public void move(float dt) {
        setPos(pos.add(vel.mul(dt)));
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
