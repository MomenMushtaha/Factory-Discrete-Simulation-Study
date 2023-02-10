package Inspectors;


import java.util.Random;
import Buffers_Product.*;

public class inspector2 {
    /*
     * Calculate average inspection time.
     * Randomly choose betwwen c2 or c3
     * Send to Buffer class
    */

    private double averageTime22;
    private double averageTime23;

    private boolean buffer;
    
    public inspector2(double averageTime22, double averageTime23){
        this.averageTime22 = averageTime22;
        this.averageTime23 = averageTime23;
        getAverageTime22();
        getAverageTime23();
        buffer = c2Orc3();
    }

    private boolean c2Orc3() {
        Random rand = new Random();
        return rand.nextBoolean();
        
    }

    public double getAverageTime22() {
        System.out.println("ins2:buf3 avg = " + averageTime22);
        return averageTime22;
    }

    public double getAverageTime23() {
        System.out.println("ins2:buf3 avg = " + averageTime23 + "\n");
        return averageTime23;
    }
}
