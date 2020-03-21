package fynn;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SimLauncher {

    public static void main(String[] args) throws Exception {

        int numParticles = 1000;
        BlockingQueue<Instance> bq = new ArrayBlockingQueue<Instance>(10);

        Simulation simulation = new Simulation(numParticles, bq);
        SimRenderer renderer = new SimRenderer(bq);
        new Thread(renderer).start();
        new Thread(simulation).start();

      //  simulation.addParticles(30);

        System.out.println("jojo");


    }


}


