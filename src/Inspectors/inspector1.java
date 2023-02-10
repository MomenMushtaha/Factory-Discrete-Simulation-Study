package Inspectors;
public class inspector1 {
    /*
     * Calculate average inspection time.
     * Send to Buffer class
    */

    private double averageTime;

    public inspector1(double averageTime){
        this.averageTime = averageTime;
        getAverageTime();
    }

    public double getAverageTime() {
        System.out.println("ins1 avg = " + averageTime);
        return averageTime;
    }
}
