package fynn;


import fynn.model.*;
import fynn.opencl.*;
import fynn.renderer.*;

import java.util.concurrent.*;

public class SimLauncher {

	public static void main(String[] args) {

		BlockingQueue<Instance> bq = new ArrayBlockingQueue<>(10);

		ClManager clmanager = new ClManager();
		int numP = MagicNumbers.initalNumParticles;

		if(args.length != 0) {
            try {
                numP = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

		Simulation simulation = new Simulation(numP, bq, clmanager);
		SimRenderer renderer = new SimRenderer(bq);
		new Thread(renderer).start();
		new Thread(simulation).start();

		System.out.println("jojo");


	}


}


