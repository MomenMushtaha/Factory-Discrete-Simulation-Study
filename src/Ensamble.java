public class Ensamble {

    public static void main(String[] args) {
        // Ensemble averaging loop
        int numSims = 10; 
        double[] ensembleAvgs = new double[101];
        
        // Initialization phase
        int initializationTime = 10000;
        for (int i = 0; i < initializationTime; i++) {
            Simulation initializationSimulation = new Simulation();
            initializationSimulation.simulate();
        }
        
        // Ensemble averaging loop
        for (int i = 0; i < numSims; i++) {
            Simulation simulation = new Simulation();
            simulation.simulate();
            ensembleAvgs[i] = simulation.completedP1;
        }
        
        // Calculate ensemble averages
        double[] steps = new double[100];
        
        for (int i = 0; i < steps.length; i++) {
            double sum = 0;
        
            for (double ensembleAvg : ensembleAvgs) {
                sum += ensembleAvg;
            }
        
            steps[i] = sum / numSims;
        }
        
        // Calculate standard deviation
        double[] deviation = new double[steps.length];

        for (int i = 0; i < steps.length; i++) {
            double sumOfSquares = 0;

            for (double ensembleAvg : ensembleAvgs) {
                sumOfSquares += Math.pow(ensembleAvg - steps[i], 2);
            }

            deviation[i] = Math.sqrt(sumOfSquares / (numSims - 1));
        }


        // Print time step ensemble averages and standard deviations
        for (int i = 0; i < steps.length; i++) {
            System.out.printf("Time: "+i+", Ensemble Average: "+steps[i]+ ", Standard Deviation:" +deviation[i]+ "\n"  );
        }
    }
    
    
}
