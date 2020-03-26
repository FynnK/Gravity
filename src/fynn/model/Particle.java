package fynn.model;

import org.joml.Vector3f;

import java.util.ArrayList;

import static fynn.MagicNumbers.bigG;
import static fynn.MagicNumbers.dT;


public class Particle extends centerOM{


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

    public void calculateForce(ArrayList<Particle> particles) {
        Vector3f cumulForce = new Vector3f(0.0f);
        for (Particle p2 : particles) {
            if (p2 == this) {
                continue;
            }

            Vector3f dir = new Vector3f(0.0f);

            float distsqr = p2.getPos().distanceSquared(getPos());

            p2.getPos().sub(getPos(), dir);
            dir.normalize();
            dir.mul(bigG / distsqr);
            cumulForce.add(dir);

        }
        accel(cumulForce, dT);
    }

    public void doTimeStep() {

    }
}
