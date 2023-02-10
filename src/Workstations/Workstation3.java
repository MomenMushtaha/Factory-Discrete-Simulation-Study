package Workstations;
public class Workstation3 {
    /*
     * Creates product when buffers c13 and c33 send
    */
    private double averageTime;

    public Workstation3(double averageTime){
        this.averageTime = averageTime;
        getAverageTime();
    }

    public double getAverageTime() {
        System.out.println("ws1 avg = " + averageTime);
        return averageTime;
    }
}
