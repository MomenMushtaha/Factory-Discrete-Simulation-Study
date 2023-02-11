package Inspectors;
//Created by Mohamed Abdalla
//Modified by Momin Mushtaha

import Buffers_Product.Buffers;
import java.nio.Buffer;
import java.util.Random;

public class Inspector2 implements Runnable{
    /*
     * Calculate average inspection time.
     * Randomly choose betwwen c2 or c3
     * Send to Buffer class
     */
    private Buffers buffer;
    private double averageTime22;
    private double averageTime23;
    private Integer Id;


    //constructor for inspector1
    public Inspector2(Integer Id, double averageTime22, double averageTime23, Buffers buffer){
        this.Id = Id;
        this.averageTime22 = averageTime22;
        this.averageTime23 = averageTime23;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            //Random boolean
            Random rand = new Random();
            //If value of random boolean is true, send components to buffer C2 workstation 2
            while (true){
                if (rand.nextBoolean()){
                    try {
                        Thread.sleep((long)averageTime22);
                        buffer.Iput(22);        
                        } catch (InterruptedException e) {
                        e.printStackTrace();
                    } 
                }else{ //Else, send components to buffer C3 workstation 3
                    
                    try {
                        Thread.sleep((long)averageTime23);
                        buffer.Iput(23); 
                        } catch (InterruptedException e) {
                        e.printStackTrace();
                    }  
                } 
            }
        }
        
    }
}
