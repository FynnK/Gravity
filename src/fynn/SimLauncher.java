package fynn;


import fynn.model.Instance;
import fynn.model.Simulation;
import fynn.renderer.SimRenderer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static fynn.MagicNumbers.initalNumParticles;

public class SimLauncher {

    public static void main(String[] args) throws Exception {

        BlockingQueue<Instance> bq = new ArrayBlockingQueue<Instance>(10);
        Simulation simulation = new Simulation(initalNumParticles, bq);
        SimRenderer renderer = new SimRenderer(bq);
        new Thread(renderer).start();
        new Thread(simulation).start();


        simulation.addParticles(30);

        System.out.println("jojo");


    }


}


