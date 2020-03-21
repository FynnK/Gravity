package fynn;

import org.joml.Random;
import org.joml.Vector3f;

import java.util.ArrayList;

public class particleFactory {
    Random r;

    public particleFactory() {
        r = new Random(System.currentTimeMillis());

    }

    public Instance createInstance(int num, float pScale, float vScale) {

        ArrayList<Particle> pList = new ArrayList<Particle>();

        pList.addAll(createParticles(num, pScale, vScale));

        Instance I = new Instance(pList, System.currentTimeMillis());

        return I;
    }

    public Vector3f randomVector3() {
        return new Vector3f(r.nextFloat() - 0.5f, r.nextFloat() - 0.5f, r.nextFloat() - 0.5f);
    }

    public ArrayList createParticles(int num, float pScale, float vScale){
        ArrayList<Particle> pList = new ArrayList<Particle>();

        while (pList.size() < num) {
            pList.add(new Particle(randomVector3().mul(pScale), randomVector3().mul(vScale)));
        }
        return pList;
    }
}
