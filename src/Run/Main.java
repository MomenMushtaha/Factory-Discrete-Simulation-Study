
package Run;


import Util.Util;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        double alpha = 0.05, beta = 0.1, maxConfidenceIntervalWidthPercentage = 0.2, maxError = 0.2, estimatedValue = 1;
        int initializationTime = 200; // Adjust this value based on your specific system requirements
        int varianceReplications = 100; // The number of replications to estimate the population variance
        Simulation sm = new Simulation();
        sm.simulate();

        // Calculate the population variance using the ensemble averages from varianceReplications
        double[] ensembleAvgs = new double[varianceReplications];
        for (int i = 0; i < varianceReplications; i++) {
            Simulation sim = new Simulation();
            sim.simulate(initializationTime);
            ensembleAvgs[i] = sim.finished;
        }
        double sum = 0;
        for (double ensembleAvg : ensembleAvgs) {
            sum += ensembleAvg;
        }
        double ensembleAverage = sum / varianceReplications;
        double sumOfSquares = 0;
        for (double ensembleAvg : ensembleAvgs) {
            sumOfSquares += Math.pow(ensembleAvg - ensembleAverage, 2);
        }
        double populationVariance = sumOfSquares / (varianceReplications - 1);

        boolean areConfidenceIntervalsAcceptable = false;

        // Calculate the required sample size using OC curves
        int n = Util.getRequiredSampleSize(alpha, beta, maxError, estimatedValue, populationVariance);

        double stdDev = 0;
        while (!areConfidenceIntervalsAcceptable) {
            List<Simulation> replications = new ArrayList<>();
            double[] mainEnsembleAvgs = new double[n];

            for (int i = 0; i < n; i++) {
                Simulation sim = new Simulation();
                sim.simulate(initializationTime);
                replications.add(sim);
                mainEnsembleAvgs[i] = sim.finished;
            }

            // Calculate ensemble averages
            double mainSum = 0;
            for (double ensembleAvg : mainEnsembleAvgs) {
                mainSum += ensembleAvg;
            }
            double mainEnsembleAverage = mainSum / n;

            // Calculate standard deviation
            stdDev = Math.sqrt(replications.stream().mapToDouble(s -> Math.pow(s.finished - mainEnsembleAverage, 2)).sum() / (n - 1));

            // Calculate confidence interval
            double tValue = Util.getTValue(alpha, n);
            double confidenceIntervalWidth = tValue * stdDev / Math.sqrt(n);

            // Check if confidence interval does not exceed 20% of the estimated value
            if (confidenceIntervalWidth / mainEnsembleAverage <= maxConfidenceIntervalWidthPercentage) {
                areConfidenceIntervalsAcceptable = true;
            } else {
                n += 1; // Increase the number of replications if confidence intervals are not acceptable
            }
        }
        System.out.println("-------------------------------------4. THE # OF REPLICATIONS-------------------------------------");
        System.out.println("Number of replications needed: " + n);
        System.out.println("-------------------------------------5. INITIALIZATION PHASE IMPLEMENTED-------------------------------------");
        System.out.println("Initialization time: " + initializationTime);
        System.out.println("The Standard Deviation is: " + stdDev);
        System.out.println("The Ensamble Average is: " + ensembleAverage);

        System.out.println("-------------------------------------6. CONFIDENCE INTERVALS FOR FOR EACH QUANTITY-------------------------------------");
    }
    }