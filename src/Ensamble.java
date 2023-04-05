public class Ensamble {

    public static void main(String[] args) {
        // Create a simulation instance
        Simulation simulationInstance = new Simulation();
        
        // Ensemble averaging loop
        int numberOfSimulations = 10; 
        double[] ensembleAverages = new double[101];
        
        // Initialization phase
        int initializationTime = 10000;
        for (int i = 0; i < initializationTime; i++) {
            Simulation initializationSimulation = new Simulation();
            initializationSimulation.simulate();
        }
        
        // Ensemble averaging loop
        for (int i = 0; i < numberOfSimulations; i++) {
            Simulation simulation = new Simulation();
            simulation.simulate();
            ensembleAverages[i] = simulation.completedP1;
        }
        
        // Calculate ensemble averages
        double[] timeStepAverages = new double[101];
        
        for (int i = 0; i < timeStepAverages.length; i++) {
            double sum = 0;
        
            for (double ensembleAverage : ensembleAverages) {
                Simulation simulation = new Simulation();
                simulation.simulate();
                sum += ensembleAverage;
            }
        
            timeStepAverages[i] = sum / numberOfSimulations;
        }
        
        // Calculate standard deviation
        double[] timeStepStandardDeviations = new double[timeStepAverages.length];
        
        for (int i = 0; i < timeStepAverages.length; i++) {
            double sumOfSquares = 0;
        
            for (double ensembleAverage : ensembleAverages) {
                Simulation simulation = new Simulation();
                simulation.simulate();
                sumOfSquares += Math.pow(ensembleAverage - timeStepAverages[i], 2);
            }
        
            timeStepStandardDeviations[i] = Math.sqrt(sumOfSquares / (numberOfSimulations - 1));
        }
        
        // Print time step ensemble averages and standard deviations
        for (int i = 0; i < timeStepAverages.length; i++) {
            System.out.printf("Time: %d, Ensemble Average: %.2f, Standard Deviation: %.2f%n", i, timeStepAverages[i], timeStepStandardDeviations[i]);
        }
    }
    
    
}
