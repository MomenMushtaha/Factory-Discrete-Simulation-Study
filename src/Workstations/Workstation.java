package Workstations;
//Created by Mohamed Abdalla
public class Workstation {
    /*
     * Creates product when buffer c11 sends
    */

    private double averageTime;
    private Integer Id;




    //W1, W2, W3 constructors
    public Workstation(int Id, double averageTime){
        this.Id = Id;
        this.averageTime = averageTime;
        getAverageTime();
    }

    public double getAverageTime() {
        System.out.println("ws1 avg = " + averageTime);
        return averageTime;
    }
}
