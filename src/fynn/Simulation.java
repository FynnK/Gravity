package fynn;

public class Simulation {
    private Instance renderInstance;
    private Instance workInstance;
    int j = 3;

    public Simulation(int numberOfParticles) {
        particleFactory pFact = new particleFactory();


        workInstance = pFact.createInstance(numberOfParticles, 100, 0.1f);
        renderInstance = workInstance;

    }

    public float[] getVertices() {
        return renderInstance.getVertices();


    }



    public void update(float dt) {
       workInstance.update(dt);
       renderInstance = workInstance;
      // System.out.println(dt);



    }


}
