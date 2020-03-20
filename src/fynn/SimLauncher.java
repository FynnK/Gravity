package fynn;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SimLauncher {

    public static void main(String[] args) throws Exception{

        int numParticles = 3;
        //BlockingQueue<Instance> bq = new ArrayBlockingQueue<Instance>(10);
        //Simulation simulation = new Simulation(numParticles, bq);

        SimRenderer renderer = new SimRenderer();
        SimRenderer r2 = new SimRenderer();
        renderer.start();
        r2.start();

        System.out.println("heloo");


    }


}


