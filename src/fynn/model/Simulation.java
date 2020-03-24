package fynn.model;

import fynn.util.particleFactory;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class Simulation implements Runnable {
    private Instance renderInstance;
    private Instance workInstance;
    particleFactory pFact;

    BlockingQueue bq = null;
    boolean running = true;

    public Simulation(int numberOfParticles, BlockingQueue bq) {
        pFact = new particleFactory();
        workInstance = pFact.createInstance(numberOfParticles, 100, 0.01f);
        renderInstance = workInstance;
        this.bq = bq;
    }

    public Simulation(int numberOfParticles) {
        pFact = new particleFactory();
        workInstance = pFact.createInstance(numberOfParticles, 100, 0.01f);
        renderInstance = workInstance;

    }

    public void addParticles(int num){
        ArrayList<Particle> pL = pFact.createParticles(num, 100, 0.1f);
        workInstance.addParticles(pL);
    }

    public float[] getVertices() {
        return renderInstance.getVertices();
    }


    public void update(float dt) {
        if (bq.remainingCapacity() == 0) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        renderInstance = workInstance;

        try {
            bq.put(renderInstance);

        } catch (Exception e) {
            e.printStackTrace();
        }
        workInstance.update(dt);

    }

    @Override
    public void run() {
        while (running) {
            update(0.1f);
        }
    }

}

