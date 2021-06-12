package fynn.model;

import fynn.opencl.ClManager;
import fynn.util.*;

import java.util.concurrent.BlockingQueue;

import static fynn.MagicNumbers.*;
import static fynn.util.InputUtil.shouldReset;

public class Simulation implements Runnable {
    private Instance renderInstance;
    private Instance workInstance;
    private final int numberOfParticles;
    particleFactory pFact;
    ClManager clmgr;

    BlockingQueue<Instance> bq;
    boolean running = true;

    public Simulation(int numberOfParticles, BlockingQueue<Instance> bq, ClManager cl) {
        this.clmgr = cl;
        pFact = new particleFactory();
        this.numberOfParticles = numberOfParticles;
        workInstance = pFact.createInstance(numberOfParticles,pScale, vScale);
        renderInstance = workInstance;
        this.bq = bq;
    }

    public void stop(){
        running = false;
    }

    public void setWorkInstance(Instance i){
        this.workInstance = i;
    }


    public void update(float dt) {
        if(shouldReset) {
            workInstance = pFact.get(InputUtil.instanceType, numberOfParticles);
            shouldReset = false;
        }


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

