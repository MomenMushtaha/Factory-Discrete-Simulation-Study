package Workstations;

import Buffers_Product.Buffers;

public class Workstation implements Runnable{
    private double averageTime;
    private int Id;
    private Buffers buffer;


    public Workstation(int Id, double averageTime, Buffers buffer){
        this.Id = Id;
        this.averageTime = averageTime;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
              
        while (true){
            try {
                buffer.Wget(getId());
                Thread.sleep((long)averageTime);
                } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }  
        
    }

    public int getId() {
        return Id;
    }
    
}
