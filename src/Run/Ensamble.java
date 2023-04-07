package Run;

import java.util.Arrays;

public class Ensamble {

    public static void main(String[] args) {
        double simTime = 0.0;
        double[] ensemble = new double[11];
        int numSims = 11;
        for(int y = 0; y <=10;y++){
        // Ensemble averaging loop
        
        double[] ensembleAvgs = new double[11];
        

        // Ensemble averaging loop
        for (int i = 0; i < numSims; i++) {    
            Simulation simulation = new Simulation();
            simulation.simulate(simTime);
            ensembleAvgs[i] = simulation.finished;
                  
        }

        // Calculate ensemble averages
        double sum = 0;

        for (double ensembleAvg : ensembleAvgs) {
            sum += ensembleAvg;
        }

        ensemble[y] = sum / numSims;
        simTime +=1000;

        }
        System.out.println("\n------------------------------------------------------------------------------------------------\n");
        System.out.println("Ensemble Averages: "+ Arrays.toString(ensemble));

        // Calculate standard deviation and confidence interval
        double sumOfSquares = 0;
        double mean = 0;

        for (double ensembleAvg : ensemble) {
            mean += ensembleAvg;
        }

        mean /= numSims;

        for (double ensembleAvg : ensemble) {
            sumOfSquares += Math.pow(ensembleAvg - mean, 2);
        }

        double stdDev = Math.sqrt(sumOfSquares / (numSims - 1));
        double t_val = 2.228; // for a two-sided t-test with 11 degrees of freedom and alpha=0.05
        double width = t_val * stdDev / Math.sqrt(numSims); //Depicts Equation on Slide 24 
        double lowerBound = mean - width;
        double upperBound = mean + width;

        
        System.out.println("Mean: "+String.format("%.3f", mean)+", Standard Deviation: "+String.format("%.3f", stdDev)+", 95% Confidence Interval: ("+String.format("%.3f", lowerBound)+", " +String.format("%.3f", upperBound)+")");

        // Not Exceed 10%
        double maxAllowableWidth = 0.1 * mean;
        int requiredNumSims = (int) Math.ceil(Math.pow(t_val * stdDev / maxAllowableWidth, 2)); //Always Round up
        System.out.println("Number of simulations: "+ requiredNumSims);
        
    }

}
