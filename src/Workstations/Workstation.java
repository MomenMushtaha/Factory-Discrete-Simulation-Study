package Workstations;

import Buffers_Product.PTypes;
import java.util.ArrayList;

//Created by Mohamed Abdalla
public class Workstation {
    int workstationId;
    private double averageTime;
    Boolean W1 = false;
    Boolean W2 = false;
    Boolean W3 = false;
    public States state;
    ArrayList<Integer> P1;
    ArrayList<Integer> P2;
    ArrayList<Integer> P3;
    private int totalP1;
    private int totalP2;
    private int totalP3;









    //W1, W2, W3 constructors
    public Workstation(double averageTime, PTypes workstationProduct){
      this.averageTime = averageTime;
      this.state = States.IDLE;
      if (workstationProduct.equals(PTypes.P1)){
          workstationId = 1;
          W1 = true;
          totalP1 = 0;
      } else if (workstationProduct.equals(PTypes.P2)) {
          workstationId = 2;
          W2 = true;
          totalP2 = 0;
      } else if (workstationProduct.equals(PTypes.P3)) {
          workstationId = 3;
          W3 = true;
          totalP3 = 0;

      }
    }



    public void produce(){
        state = States.BUSY;
        if (W1){
            //time it takes here
            incrementP1();}
        else if (W2) {
            //time it takes here
            incrementP2();}
        else if (W3) {
            //time it takes here
            incrementP3();}
        state = States.IDLE;
    }

    private void incrementP1(){
            totalP1++;
    }
    private void incrementP2(){
        totalP2++;
    }
    private void incrementP3(){
        totalP3++;
    }
    public double getAverageTime(int workstationId) {
        System.out.println("W" + workstationId + " avg = " + averageTime);
        return averageTime;
    }


}
