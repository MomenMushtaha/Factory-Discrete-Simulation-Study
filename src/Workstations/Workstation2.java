package Workstations;
public class Workstation2 {
    /*
     * Creates product when buffers c12 and c22 send
    */
    private double averageTime;

    public Workstation2(double averageTime){
        this.averageTime = averageTime;
        getAverageTime();
    }

    public double getAverageTime() {
        System.out.println("ws1 avg = " + averageTime);
        return averageTime;
    }
}
