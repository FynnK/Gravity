package fynn.model;

import fynn.opencl.ClManager;
import fynn.util.particleFactory;

import java.util.concurrent.BlockingQueue;

import static fynn.MagicNumbers.*;

public class Simulation implements Runnable {
    private Instance renderInstance;
    private Instance workInstance;
    particleFactory pFact;
    ClManager clmgr;

    BlockingQueue<Instance> bq;
    boolean running = true;

    public Simulation(int numberOfParticles, BlockingQueue<Instance> bq, ClManager cl) {
        this.clmgr = cl;
        pFact = new particleFactory();
        workInstance = pFact.createInstance(numberOfParticles,pScale, vScale);
        renderInstance = workInstance;
        this.bq = bq;
    }

    public void stop(){
        running = false;
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

        workInstance.update(clmgr);

    }

    @Override
    public void run() {
        clmgr.init(renderInstance.getNumParticles());

        while (running) {
            update(dT);
        }
        clmgr.destroy();
    }

}

