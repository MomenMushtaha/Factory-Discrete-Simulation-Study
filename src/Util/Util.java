package Util;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Util {

    // This method reads the .dat files and calculates the average processing times
    public static double calculateAverageTime(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            double sum = 0;
            double count = 0;
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();

                double nextDub = Double.parseDouble(nextLine);
                sum += nextDub;
                count++;
            }
            scanner.close();

            return sum / count;
        } catch (FileNotFoundException e) {
            System.out.println("The file could not be found.");
        } catch (NumberFormatException e) {
            System.out.println("The file contains an invalid number.");
        }
        return 0;
    }

    public static double getTValue(double alpha, int n) {
        // Calculate the degrees of freedom
        int degreesOfFreedom = n - 1;

        // Create a t-distribution object with the calculated degrees of freedom
        TDistribution tDistribution = new TDistribution(degreesOfFreedom);

        // Calculate the t-value using the t-distribution object and the given alpha

        return tDistribution.inverseCumulativeProbability(1 - alpha / 2);
    }
    public static int getRequiredSampleSize(double alpha, double beta, double maxError, double estimatedValue, double populationVariance) {
        double marginOfError = maxError * estimatedValue;

        NormalDistribution normalDist = new NormalDistribution();
        double zAlpha = normalDist.inverseCumulativeProbability(1 - alpha / 2);
        double zBeta = normalDist.inverseCumulativeProbability(1 - beta);

        double requiredSampleSize = Math.pow(zAlpha + zBeta, 2) * (populationVariance / Math.pow(marginOfError, 2));
        return (int) Math.ceil(requiredSampleSize);
    }
}