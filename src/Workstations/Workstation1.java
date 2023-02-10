package Workstations;
public class Workstation1 {
    /*
     * Creates product when buffer c11 sends
    */

    private double averageTime;

    public Workstation1(double averageTime){
        this.averageTime = averageTime;
        getAverageTime();
    }

    public double getAverageTime() {
        System.out.println("ws1 avg = " + averageTime);
        return averageTime;
    }
}
