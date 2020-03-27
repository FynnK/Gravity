package fynn.util;

import fynn.model.Instance;
import fynn.model.Particle;
import org.joml.Random;
import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;

import static java.lang.StrictMath.*;


public class particleFactory {
    Random r;

    public particleFactory() {
        r = new Random(System.currentTimeMillis());

    }

    public Instance createInstance(int num, float pScale, float vScale) {
        Instance I = new Instance(createParticles(num, pScale, vScale));
        return I;
    }


    public Instance createSpiralInstance(int num, float pScale, float vScale){
        Instance I = new Instance(createParticlesSpiral(num, pScale, vScale));
        return I;
    }

    private ArrayList<Particle> createParticlesSpiral(int num, float pScale, float vScale) {
        ArrayList<Particle> pList = new ArrayList<>();

        while (pList.size() < num) {
            float angle = r.nextFloat()* 2.0f * 3.1415927f;
            float dist = r.nextFloat() * pScale;
            dist += 20;

            Vector3f pos = new Vector3f(dist * (float)sin(angle), r.nextFloat() * pScale/10,  dist * (float)cos(angle));
            angle += 3.1415927f/2;

            Vector3f vel = new Vector3f((float)sqrt(dist) * (float)sin(angle), r.nextFloat() * vScale/20, (float) sqrt(dist) * (float)cos(angle));

            pList.add(new Particle(pos,vel.mul(0.1f)));
        }

        return pList;
    }

    public Vector3f randomVector3() {
        return new Vector3f(r.nextFloat() - 0.5f, r.nextFloat() - 0.5f, r.nextFloat() - 0.5f).mul(2);
    }

    public ArrayList createParticles(int num, float pScale, float vScale){
        ArrayList<Particle> pList = new ArrayList<>();
        while (pList.size() < num) {
            pList.add(new Particle(randomVector3().mul(pScale),randomVector3().mul(vScale)));
        }
        return pList;
    }
}
