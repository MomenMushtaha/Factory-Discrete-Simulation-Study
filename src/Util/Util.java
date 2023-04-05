package Util;

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
        double tValue = tDistribution.inverseCumulativeProbability(1 - alpha / 2);

        return tValue;
    }

    public static int getRequiredSampleSize(double alpha, double beta, double maxError, double estimatedValue) {
        // Implement the formula from slides to calculate the required sample size
        // based on the desired values of alpha, beta, maxError, and estimatedValue.
    return 0;}
}
