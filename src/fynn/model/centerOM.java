package fynn.model;

import org.joml.Vector3f;

import java.util.ArrayList;

import static fynn.MagicNumbers.bigG;

public class centerOM {
    private Vector3f pos;
    private float mass;

    public centerOM(Vector3f pos, float mass) {
        this.pos = pos;
        this.mass = mass;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public void move(Vector3f dir, float dt){
        this.pos = pos.add(dir.mul(dt));
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }


}
