package Run;
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
            System.out.printf("Time: " + i + ", Ensemble Average: " + steps[i] + ", Standard Deviation:" + deviation[i] + "\n");
        }
    }


//    Initialization Time: X.XX (a value for the initialization time)
    //Assembled products: Y (a value for the number of assembled products)
    //Workstation 1 finished assembling product P1 at time Z.ZZ (values for Workstation 1)
    //Workstation 2 finished assembling product P2 at time Z.ZZ (values for Workstation 2)
    //Workstation 3 finished assembling product P3 at time Z.ZZ (values for Workstation 3)

    //Little's Law Results:
    //Workstation 1 - Avg time in system (W): X.XX, Avg arrival rate (λ): X.XXXX, Avg # of components in system (L): X.XX
    //  Workstation 2 - Avg time in system (W): X.XX, Avg arrival rate (λ): X.XXXX, Avg # of components in system (L): X.XX
    //Workstation 3 - Avg time in system (W): X.XX, Avg arrival rate (λ): X.XXXX, Avg # of components in system (L): X.XX
//    Facility - Avg time in system (W): X.XX, Avg arrival rate (λ): X.XXXX, Avg # of components in system (L): X.XX


}
