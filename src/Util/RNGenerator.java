package Util;


import java.io.*;

/**
 * This class implements a random number generator using a combined linear congruential generator (CLCG)
 * with L'Ecuyer's suggested parameters.
 *
 * The implementation is based on the paper "Tables of linear congruential generators of different sizes and
 * good lattice structure" by Pierre L'Ecuyer.
 *
 * These parameter values are chosen to have good spectral properties and to produce a sequence of pseudorandom numbers
 * with good statistical properties.
 *
 * 1. The generator is initialized with the current time in milliseconds and nanoseconds as the seeds.
 * 2. The seeds are then used to initialize the values array, which is used to generate the next random variable.
 * 3. The values array is updated with the new value after each call to the getNextUniform(), getNextExponential(), or
 * getNextNormal() methods.

 * Author: Momin Mushtaha
 * Student ID: 101114546
 */

public class RNGenerator {
    private static final long MODULUS = (long) Math.pow(2, 32);
    private static final long[] MULTIPLIERS = {1181783497276652981L, 4294957665L}; // L'Ecuyer's suggested multipliers
    private static final long[] INCREMENTS = {0, 1}; // L'Ecuyer's suggested increments
    private static final long[] SEEDS = {System.currentTimeMillis(), System.nanoTime()};
    private static final int K = MULTIPLIERS.length; // number of generators

    private final long[] seeds;
    private final long[] multipliers;
    private final long[] increments;
    private final long[] values;
    private int currentIndex;

    /**
     * Constructor for the random number generator. Initializes the seeds, multipliers, increments, and values arrays
     * based on L'Ecuyer's suggested parameter values.
     */
    public RNGenerator() {
        this.seeds = SEEDS;
        this.multipliers = MULTIPLIERS;
        this.increments = INCREMENTS;
        this.values = new long[K];
        System.arraycopy(seeds, 0, values, 0, K);
        this.currentIndex = 0;
    }

    /**
     * Generates the next uniform random variable using the current generator index to select the corresponding
     * multiplier and increment values, and updates the values array with the new value.
     *
     * @return the next uniform random variable
     */
    public double getNextUniform() {
        long x = values[currentIndex];
        currentIndex = (currentIndex + 1) % K;
        long next = (multipliers[currentIndex] * x + increments[currentIndex]) % MODULUS;
        values[currentIndex] = next;
        return (double) next / (double) MODULUS;
    }

    /**
     * Generates the next exponential random variable using the inverse-transform technique.
     *
     * @param lambda the rate parameter of the exponential distribution
     * @return the next exponential random variable
     */
    public double getNextExponential(double lambda) {
        double rand_uniform = getNextUniform();
        double next = Math.log(1 - rand_uniform) * -1;
        return next / lambda;
    }

    /**
     * Generates the next normal random variable using the Box-Muller transform.
     *
     * @return the next normal random variable
     */
    public double getNextNormal() {
        double u1 = getNextUniform();
        double u2 = getNextUniform();
        double z1 = Math.sqrt(-2 * Math.log(u1)) * Math.cos(2 * Math.PI * u2);
        return z1;
    }

    /**
     * Returns the next random variable, which is uniform by default.
     *
     * @return the next random variable
     */
    public double getNext() {
        return getNextUniform();
    }
    public void generateExponentialFile(String inputFilePath, String outputFilePath) {
        try {
            // Read the input .dat file
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFilePath));

            // Calculate the mean of input values
            String line;
            int count = 0;
            double sum = 0;
            while ((line = bufferedReader.readLine()) != null) {
                // Skip empty lines or lines containing only whitespaces
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Parse each value in the input file
                double inputValue = Double.parseDouble(line.trim());

                // Ignore negative input values
                if (inputValue < 0) {
                    continue;
                }

                sum += inputValue;
                count++;
            }
            double mean = sum / count;

            // Calculate lambda as the inverse of the mean
            double lambda = 1 / mean;

            // Reset the reader to the beginning of the file
            bufferedReader.close();
            bufferedReader = new BufferedReader(new FileReader(inputFilePath));

            // Create a new output .dat file to store the exponential random variables
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFilePath));

            while ((line = bufferedReader.readLine()) != null) {
                // Skip empty lines or lines containing only whitespaces
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Parse each value in the input file
                double inputValue = Double.parseDouble(line.trim());

                // Ignore negative input values
                if (inputValue < 0) {
                    continue;
                }

                // Generate the exponential random variable using the calculated lambda
                double exponentialValue = getNextExponential(lambda * inputValue);

                // Use the absolute value of the generated exponential random variable
                double absoluteExponentialValue = Math.abs(exponentialValue);

                // Format the output number with a fixed number of decimal places (e.g., 4)
                String formattedExponentialValue = String.format("%.4f", absoluteExponentialValue);

                // Write the generated exponential random variable to the output file
                bufferedWriter.write(formattedExponentialValue);
                bufferedWriter.newLine();
            }

            // Close the input and output files
            bufferedReader.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}