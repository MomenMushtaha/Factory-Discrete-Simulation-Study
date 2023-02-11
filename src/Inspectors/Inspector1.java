package Inspectors;
import java.nio.Buffer;
import java.util.Random;

import Buffers_Product.Buffers;
//Created by Mohamed Abdalla
//Modified by Momin Mushtaha
public class Inspector1 implements Runnable{
    /*
     * Calculate average inspection time.
     * Randomly choose betwwen c2 or c3
     * Send to Buffer class
     */
    private Buffers buffer;
    private double averageTime;
    private Integer Id;


    //constructor for inspector1
    public Inspector1(Integer Id, double averageTime, Buffers buffer){
        this.Id = Id;
        this.averageTime = averageTime;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            //Sends component to buffer C1 of workstation 1
            try {
              buffer.Iput(1);
              Thread.sleep((long)averageTime);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }

            //Sends component to buffer C1 of workstation 2
            try {
                buffer.Iput(12);
                Thread.sleep((long)averageTime);
                } catch (InterruptedException e) {
                e.printStackTrace();
                }

            //Sends component to buffer C1 workstation 3
            try {
              buffer.Iput(13);
              Thread.sleep((long)averageTime);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
        }
        
    }
}
