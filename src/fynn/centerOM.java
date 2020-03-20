package fynn;

import org.joml.Vector3f;

public class centerOM {
    Vector3f pos;
    float mass;

    public centerOM(Vector3f pos, float mass){
        this.pos = pos;
        this.mass = mass;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }
}
