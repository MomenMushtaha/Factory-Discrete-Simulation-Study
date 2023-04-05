import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class main {
    public double avg1, avg22, avg23;   //Inspector Average Values
    public double ws1, ws2, ws3;        //Workstation Average Values //

    public main(){
        // Setting Average times of each '.dat' file
        avg1 = AvgTime("servinsp1.dat");
        avg22 = AvgTime("servinsp22.dat");
        avg23 = AvgTime("servinsp23.dat");

        ws1 = AvgTime("ws1.dat");
        ws2 = AvgTime("ws2.dat");
        ws3 = AvgTime("ws3.dat");

    }

    /*
     * Reads each line the '.dat' files. The values stored in each line are parsed into doubles.
     * Average is calcualted
     * 
     * @param filename    Name of the '.dat' file to be read
     * @return            Average of the files values in double form 
    */
    public double AvgTime(String filename){
        try {
            // Create a Scanner to read from the file
            Scanner scanner = new Scanner(new File(filename));
            double sum = 0;
            double count = 0;
            while (scanner.hasNextLine()) {
              // Read the next line from the file
              String nextLine = scanner.nextLine();
            
              double nextDub = Double.parseDouble(nextLine);
              // Add the number to the sum
              sum += nextDub;
              // Increment the count of numbers
              count++;
            }
            // Close the scanner
            scanner.close();
            double average = sum / count;
            
            return average;
          } catch (FileNotFoundException e) {
            System.out.println("The file could not be found.");
          } catch (NumberFormatException e) {
            System.out.println("The file contains an invalid number.");
          }
        return 0;
          
    }



    //Getters
    public double getAvg1(){
        
        return avg1;
    }

    public double getAvg22(){
       
        return avg22;
    }

    public double getAvg23(){
        
        return avg23;
    }

    public double getWs1() {
        return ws1;
    }

    public double getWs2() {
        return ws2;
    }

    public double getWs3() {
        return ws3;
    }

    public static void main(String[] args) {
        main mn = new main();
        
    }



}
// import java.util.ArrayList;
// import java.util.PriorityQueue;

// public class Simulation {
//     private ArrayList<Component> components;
//     private Inspector[] inspectors;
//     private Workstation[] workstations;
//     private double simulationTime;
//     private PriorityQueue<Event> eventList;
//     private double[] bufferSizes;
//     private int[] numBlockedInspectors;

//     public Simulation() {
//         // Read in input files and initialize data structures
//         // Initialize Future event list with arrival events
//     }

//     public void run() {
//         while (!eventList.isEmpty()) {
//             Event event = eventList.poll();
//             if (event.getTime() > simulationTime) {
//                 break;
//             }
//         double elapsedTime = event.getTime() - startTime;
//         startTime = event.getTime();

//             if (event.getType() == EventType.ARRIVAL) {
//                 handleArrivalEvent(event.getComponent());
//             } else if (event.getType() == EventType.INSPECTION) {
//                 handleInspectionEvent(event.getInspector(), event.getComponent());
//             } else if (event.getType() == EventType.PROCESSING) {
//                 handleProcessingEvent(event.getWorkstation(), event.getComponent());
//             }
// 	double[] workstationUtilization = calculateWorkstationUtilization();
//         double[] bufferOccupancy = calculateBufferOccupancy();
//         double[] inspectorBlockage = calculateInspectorBlockage();
//         // Output statistics
//         System.out.println("Elapsed Time: " + elapsedTime);
//         System.out.println("Throughput: " + calculateThroughput());
//         System.out.println("Workstation Utilization: ");
//         for (int i = 0; i < workstations.length; i++) {
//             System.out.println("W" + (i+1) + ": " + workstationUtilization[i]);
//         }
//         System.out.println("Buffer Occupancy: ");
//         for (int i = 0; i < bufferSizes.length; i++) {
//             System.out.println("B" + (i+1) + ": " + bufferOccupancy[i]);
//         }
//         System.out.println("Inspector Blockage: ");
//         for (int i = 0; i < inspectors.length; i++) {
//             System.out.println("I" + (i+1) + ": " + inspectorBlockage[i]);
//         }

//         }
        
//     }

//     private void handleArrivalEvent(Component component) {
//         // Assign component to appropriate buffer according to shortest queue policy
//         // Update buffer size
//         // Schedule inspection event for appropriate inspector
//         // Schedule next arrival event
//     }

//     private void handleInspectionEvent(Inspector inspector, Component component) {
//         // Assign component to appropriate buffer based on type
//         // If buffer is empty, schedule processing event for appropriate workstation
//         // Update inspector and buffer information
//     }

//     private void handleProcessingEvent(Workstation workstation, Component component) {
//         // Remove component from buffer
//         // Update buffer size
//         // If buffer is not empty, schedule processing event for next component
//         // Otherwise, workstation becomes idle
//     }

//     private double calculateThroughput() {
//         // Calculate total number of completed components during simulation time
//         // Divide by simulation time to get throughput
//     }

//     private double[] calculateWorkstationUtilization() {
//         // Calculate total busy time for each workstation
//         // Divide by simulation time to get utilization
//         // Return array of utilization values for each workstation
//     }

//     private double[] calculateBufferOccupancy() {
//         // Calculate average occupancy for each buffer during simulation time
//         // Return array of occupancy values for each buffer
//     }

//     private double[] calculateInspectorBlockage() {
//         // Calculate proportion of time each inspector is blocked during simulation time
//         // Return array of blockage values for each inspector
//     }
// }