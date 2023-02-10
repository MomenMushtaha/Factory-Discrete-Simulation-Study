import Inspectors.*;
import Workstations.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class main {
    public double avg1, avg22, avg23;   //Inspector Average Values
    public double ws1, ws2, ws3;        //Workstation Average Values 

    public main(){
        // Setting Average times of each '.dat' file
        avg1 = AvgTime("servinsp1.dat");
        avg22 = AvgTime("servinsp22.dat");
        avg23 = AvgTime("servinsp23.dat");

        ws1 = AvgTime("ws1.dat");
        ws2 = AvgTime("ws2.dat");
        ws3 = AvgTime("ws3.dat");

        // Assigning respective times 
        inspector1 in1 = new inspector1(getAvg1());
        inspector2 in2 = new inspector2(getAvg22(), getAvg23());
        
        Workstation1 w1 = new Workstation1(getWs1());
        Workstation2 w2 = new Workstation2(getWs2());
        Workstation3 w3 = new Workstation3(getWs3());
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
