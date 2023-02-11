package Buffers_Product;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Buffers {

    //Timer for beggining of system operation
    public long genisis = System.currentTimeMillis();

    //Buffers capacity arraylist of size 2
    private ArrayList<Integer> bufC1 = new ArrayList<>(2);    
    private ArrayList<Integer> bufC12 = new ArrayList<>(2);    
    private ArrayList<Integer> bufC22 = new ArrayList<>(2);    
    private ArrayList<Integer> bufC13 = new ArrayList<>(2);    
    private ArrayList<Integer> bufC23 = new ArrayList<>(2);    
    
    //Semaphore of 2 permits fro each buffer
    private Semaphore sema1 = new Semaphore(2);                                         
    private Semaphore sema12 = new Semaphore(2);                                         
    private Semaphore sema22 = new Semaphore(2);                                         
    private Semaphore sema13 = new Semaphore(2);                                         
    private Semaphore sema23 = new Semaphore(2);                                         

    //Time Buffers are occupied
    private long busyTime1 = 0;                                                                       
    private long busyTime12 = 0;   
    private long busyTime22 = 0;   
    private long busyTime13 = 0;   
    private long busyTime23 = 0;   
    
    


    /*
     * Buffer has received an interrupt indicating that a component is received.
     * Checks which buffer to input component.
     * If no space, Inspector put on hold, time is tracked.
     * 
     * @param componentID    Component signifier used to indicate inspector and workstation assignment
     *  
    */
    public void Iput(int componentID) throws InterruptedException{
        long startTime;
        long endTime;
        
        //Checks which buffer to send to
        switch (componentID){
            case 1:
            //Starts Timer
            startTime = System.currentTimeMillis();
            //Holds semaphore
            sema1.acquire();
            //Adds component to buffer
            bufC1.add(componentID);
            //Calculates time elapsed
            endTime = System.currentTimeMillis();
            busyTime1 += (endTime - startTime);
            
            case 12:
            startTime = System.currentTimeMillis();
            sema12.acquire();
            bufC12.add(componentID);
            endTime = System.currentTimeMillis();
            busyTime12 += (endTime - startTime);
            
            
            case 22:
            startTime = System.currentTimeMillis();
            sema22.acquire();
            bufC22.add(componentID);
            endTime = System.currentTimeMillis();
            busyTime22 += (endTime - startTime);
            
            
            case 13:
            startTime = System.currentTimeMillis();
            sema13.acquire();
            bufC13.add(componentID);
            endTime = System.currentTimeMillis();
            busyTime13 += (endTime - startTime);
            
            
            case 23:
            startTime = System.currentTimeMillis();
            sema23.acquire();
            bufC23.add(componentID);
            endTime = System.currentTimeMillis();
            busyTime23 += (endTime - startTime);
            
        }    

    }



    /*
     * Buffer checks Workstation Number.
     * Based on number, buffer goes through its processing time.
     * Component is removed from respective buffer.
     * In the case of WS2 and WS3, Stations wait till both required components are ready before receiving
     * 
     * @param stationNum    Workstation Signifier to indicate which buffer to receive from
    */
    public void Wget(int stationNum){
        //Checks which station is requesting component
        switch( stationNum ){
            case 1:
            //Checks if all components required for a product are available
            while (bufC1.size() > 0) {
                //Goes through buffer time
                try {
                    Thread.sleep(28);
                } catch (InterruptedException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                //Removes component from buffer
                bufC1.remove(0);
                //Releases semaphore hold
                sema1.release();
            }
            
            case 2:
            while ( (bufC12.size() > 0) && (bufC22.size() > 0)) {
                //Goes through processing time of longer buffer
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                bufC12.remove(0);
                sema12.release();
                bufC22.remove(0);
                sema22.release();
            }
            
            case 3:
            while ((bufC13.size() > 0) && (bufC23.size() > 0)) {
                try {
                    Thread.sleep(175);
                } catch (InterruptedException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                bufC13.remove(0);
                sema13.release();
                bufC23.remove(0);
                sema23.release();
            }
            
        }
        
    }

    public long stop(){
        long arma = System.currentTimeMillis();
        long elapsedTIme = arma - genisis;
        System.out.println("Demo Stopped");

        System.out.println("Inspectors");
        System.out.println("1 - Blocked Chance: " + (busyTime1 + busyTime12 + busyTime13)/elapsedTIme * 100 +"%");
        System.out.println("2 - Blocked Chance: " + (busyTime22 + busyTime23)/elapsedTIme * 100 +"%");

        System.out.println("Buffers");
        System.out.println("1 - Occupied Chance: " + (busyTime1)/elapsedTIme * 100 +"%");
        System.out.println("12 - Occupied Chance: " + (busyTime12)/elapsedTIme * 100 +"%");
        System.out.println("22 - Occupied Chance: " + (busyTime22)/elapsedTIme * 100 +"%");
        System.out.println("13 - Occupied Chance: " + (busyTime13)/elapsedTIme * 100 +"%");
        System.out.println("33 - Occupied Chance: " + (busyTime23)/elapsedTIme * 100 +"%");

        

        return elapsedTIme;
    }

    
}
