package Run;

import Model.*;
import Util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Simulation {
    public RNGenerator randomGenerator;
    public Workstation workstation1, workstation2, workstation3;
    public Inspector inspector1, inspector2;
    public double avg1, avg22, avg23; // Inspector Average Values
    public double ws1, ws2, ws3; // Workstation Average Values
    public int completedP1, completedP2, completedP3; // Completed Components
    public double totalTimeSpentInQueueWS1, totalTimeSpentInQueueWS2, totalTimeSpentInQueueWS3;
    public double totalProcessingTimeWS1, totalProcessingTimeWS2, totalProcessingTimeWS3;
    public HashMap<String, Double> timeComponentEnteredQueue = new HashMap<>();
    public HashMap<String, Double> timeComponentLeftQueue = new HashMap<>();
    public PriorityQueue<Event> eventQueue;
    public WorkstationStatistics workstationStats;
    public double simulationTime = 10000;
    public long finished;
    public double assemblyTime;
    public double I1, I22, I23, W1, W2, W3;
    public double n;




    public Simulation() {
        randomGenerator = new RNGenerator();
        workstationStats = new WorkstationStatistics();
        // Setting Average times of each '.dat' file
        avg1 = AvgTime("ProjectFiles/servinsp1.dat");
        avg22 = AvgTime("ProjectFiles/servinsp22.dat");
        avg23 =AvgTime("ProjectFiles/servinsp23.dat");
        ws1 = AvgTime("ProjectFiles/ws1.dat");
        ws2 = AvgTime("ProjectFiles/ws2.dat");
        ws3 =AvgTime("ProjectFiles/ws3.dat");
        // Initialize the random number generator
        randomGenerator.generateExponentialFile("ProjectFiles/servinsp1.dat", "ProjectFiles/I1.dat");
        randomGenerator.generateExponentialFile("ProjectFiles/servinsp22.dat", "ProjectFiles/I22.dat");
        randomGenerator.generateExponentialFile("ProjectFiles/servinsp23.dat", "ProjectFiles/I23.dat");
        randomGenerator.generateExponentialFile("ProjectFiles/ws1.dat", "ProjectFiles/W1.dat");
        randomGenerator.generateExponentialFile("ProjectFiles/ws2.dat", "ProjectFiles/W2.dat");
        randomGenerator.generateExponentialFile("ProjectFiles/ws3.dat", "ProjectFiles/W3.dat");
        I1 = AvgTime("ProjectFiles/I1.dat");
        I22 = AvgTime("ProjectFiles/I22.dat");
        I23 = AvgTime("ProjectFiles/I23.dat");
        W1 = AvgTime("ProjectFiles/W1.dat");
        W2 = AvgTime("ProjectFiles/W2.dat");
        W3 = AvgTime("ProjectFiles/W3.dat");
    }

    // This method reads the .dat files and calculates the average processing times
    public double AvgTime(String filename) {
        try{
            Scanner scanner = new Scanner(new File(filename));
            double sum = 0;
            double count = 0;
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                double nextDub = Double.parseDouble(nextLine);
                sum += nextDub;
                count++;
            }
            scanner.close();
            return sum / count;
        } catch (FileNotFoundException e) {
            System.out.println("The file could not be found.");
        } catch (NumberFormatException e) {
            System.out.println("The file contains an invalid number.");
        }
        return 0;
    }

    public void simulate() {
        // Initialize inspectors with their respective average processing times
        inspector1 = new Inspector(1, 0);
        inspector2 = new Inspector(2, 0);
        // Create components
        Component c1 = new Component("C1", 1, 1, 1, 1);
        Component c2 = new Component("C2", 2, 0, 1, 0);
        Component c3 = new Component("C3", 2, 0, 0, 1);
        // Initialize workstations
        workstation1 = new Workstation(1);
        workstation2 = new Workstation(2);
        workstation3 = new Workstation(3);
        // Initialize event queue
        eventQueue = new PriorityQueue<>();
        // Add initial inspection finished events
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, avg1, c1, inspector1, null));
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, avg22, c2, inspector2, null));
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, avg23, c3, inspector2, null));
        // Run the simulation for a fixed time (10,000 seconds in this case)
        simulationTime = 10000.0;
        int assembledProducts = 0;
        while (!eventQueue.isEmpty() && eventQueue.peek().getTime() <= simulationTime) {
            Event event = eventQueue.poll();
            if (event != null) {
                switch (event.getType()) {
                    case INSPECTION_FINISHED -> handleInspectionFinished(event, workstation1, workstation2, workstation3, eventQueue);
                    case ASSEMBLY_STARTED -> handleAssemblyStarted(event, eventQueue);
                    case ASSEMBLY_FINISHED -> {
                        handleAssemblyFinished(event);
                        assembledProducts++;
                    }
                }
            }
        }
        System.out.println("Assembled products: " + assembledProducts);
        printResults();
    }

    // This method handles the inspection finished events
    private void handleInspectionFinished(Event event, Workstation workstation1, Workstation workstation2, Workstation workstation3, PriorityQueue<Event> eventQueue) {
        Component component = event.getComponent();
        Inspector inspector = event.getInspector();
        Workstation targetWorkstation = null;

        // Determine the target workstation for the component based on the component type
        if (component.getType().equals("C1")) {
            // Inspector 1 policy: deliver to the workstation with the lowest C1 buffer occupancy
            if (workstation1.getBufferCountForComponentType("C1") < workstation2.getBufferCountForComponentType("C1") &&
                    workstation1.getBufferCountForComponentType("C1") < workstation3.getBufferCountForComponentType("C1")) {
                targetWorkstation = workstation1;
            } else if (workstation2.getBufferCountForComponentType("C1") < workstation1.getBufferCountForComponentType("C1") &&
                    workstation2.getBufferCountForComponentType("C1") < workstation3.getBufferCountForComponentType("C1")) {
                targetWorkstation = workstation2;
            } else {
                targetWorkstation = workstation3;
            }
        } else if (component.getType().equals("C2")) {
            targetWorkstation = workstation2;
        } else {
            targetWorkstation = workstation3;
        }

        timeComponentEnteredQueue.put(component.getType(), event.getTime());
        if (eventQueue.peek() == null || eventQueue.peek().getTime() > simulationTime) {
            double remainingTime = simulationTime - event.getTime();

            totalTimeSpentInQueueWS1 += targetWorkstation.getBufferCountForComponentType("C1") * remainingTime;
            totalTimeSpentInQueueWS2 += targetWorkstation.getBufferCountForComponentType("C2") * remainingTime;
            totalTimeSpentInQueueWS3 += targetWorkstation.getBufferCountForComponentType("C3") * remainingTime;
        }

        // Add the component to the target workstation's buffer
        targetWorkstation.addComponentToBuffer(component);
        inspector.setQueue(targetWorkstation.isInspectorBlocked() ? 1 : 0);

        // Add the component type and workstation ID to the timeComponentEnteredQueue map
        timeComponentEnteredQueue.put(component.getType() + "-" + targetWorkstation.getId(), event.getTime());


        // Schedule the next inspection finished event if the inspector is not blocked
        if (!targetWorkstation.isInspectorBlocked()) {
            double nextInspectionTime = event.getTime() + (inspector.getId() == 1 ? avg1 : (component.getType().equals("C2") ? avg22 : avg23));
            Component nextComponent = new Component(component.getType(), component.getInspectorId(), component.getRequiredForP1(), component.getRequiredForP2(), component.getRequiredForP3());
            eventQueue.add(new Event(EventType.INSPECTION_FINISHED, nextInspectionTime, nextComponent, inspector, null));
        }

        // Schedule assembly started events for each workstation if they have enough components in their buffers
        if (workstation1.getBufferCountForComponentType("C1") >= component.getRequiredForP1()) {
            eventQueue.add(new Event(EventType.ASSEMBLY_STARTED, event.getTime(), component, null, workstation1));
        }

        if (workstation2.getBufferCountForComponentType("C1") >= component.getRequiredForP1() &&
                workstation2.getBufferCountForComponentType("C2") >= component.getRequiredForP2()) {
            eventQueue.add(new Event(EventType.ASSEMBLY_STARTED, event.getTime(), component, null, workstation2));
        }

        if (workstation3.getBufferCountForComponentType("C1") >= component.getRequiredForP1() &&
                workstation3.getBufferCountForComponentType("C3") >= component.getRequiredForP3()) {
            eventQueue.add(new Event(EventType.ASSEMBLY_STARTED, event.getTime(), component, null, workstation3));
        }
    }


    // This method handles the assembly started events
    private void handleAssemblyStarted(Event event, PriorityQueue<Event> eventQueue) {
        Workstation workstation = event.getWorkstation();
        Component component = event.getComponent();

        // Increment the total time spent in the queue for the corresponding workstation
        String key = component.getType();
        if (timeComponentEnteredQueue.containsKey(key)) {
            double timeSpentInQueue = event.getTime()- timeComponentEnteredQueue.get(key).longValue();
            if (workstation.getId() == 1) {
                totalTimeSpentInQueueWS1 += timeSpentInQueue;
            } else if (workstation.getId() == 2) {
                totalTimeSpentInQueueWS2 += timeSpentInQueue;
            } else {
                totalTimeSpentInQueueWS3 += timeSpentInQueue;
            }
        }

        // Store the time when the component leaves the queue
        timeComponentLeftQueue.put(key, event.getTime());

        workstation.removeComponentFromBuffer(component);

        // Calculate assembly time based on the workstation
        double assemblyTime;
        if (workstation.getId() == 1) {
            assemblyTime = ws1;
        } else if (workstation.getId() == 2) {
            assemblyTime = ws2;
        } else {
            assemblyTime = ws3;
        }

        // Schedule assembly finished event
        double assemblyFinishTime = event.getTime() + assemblyTime;
        eventQueue.add(new Event(EventType.ASSEMBLY_FINISHED, assemblyFinishTime, component, null, workstation));
    }



    //This method handles the assembly finished events
    private void handleAssemblyFinished(Event event) {
        Workstation workstation = event.getWorkstation();
        String productType;
        // Increment the number of completed products for the corresponding workstation
        double assemblyTime;
        if (workstation.getId() == 1) {
            assemblyTime = ws1;
            productType = "P1";
            totalProcessingTimeWS1 += assemblyTime;
            completedP1++; // Increment completed products counter for Workstation 1
        } else if (workstation.getId() == 2) {
            assemblyTime = ws2;
            productType = "P2";
            totalProcessingTimeWS2 += assemblyTime;
            completedP2++; // Increment completed products counter for Workstation 2
        } else {
            assemblyTime = ws3;
            productType = "P3";
            totalProcessingTimeWS3 += assemblyTime;
            completedP3++; // Increment completed products counter for Workstation 3
        }


        System.out.printf("Workstation %d finished assembling product %s at time %.2f%n", workstation.getId(), productType, event.getTime());
    }




    public void printResults() {
        System.out.println("---------------------------------------------1. SIMULATION RESULTS-------------------------------------");
        System.out.printf("Total time: %.2f%n", simulationTime);
        System.out.printf("Completed P1: %d%n", completedP1);
        System.out.printf("Completed P2: %d%n", completedP2);
        System.out.printf("Completed P3: %d%n", completedP3);
        System.out.printf("Total completed products: %d%n", completedP1 + completedP2 + completedP3);
        System.out.printf("Total time spent in queue for WS1: %.2f%n", totalTimeSpentInQueueWS1);
        System.out.printf("Total time spent in queue for WS2: %.2f%n", totalTimeSpentInQueueWS2);
        System.out.printf("Total time spent in queue for WS3: %.2f%n", totalTimeSpentInQueueWS3);
        System.out.printf("Total time spent in queue for facility: %.2f%n", totalTimeSpentInQueueWS1 + totalTimeSpentInQueueWS2 + totalTimeSpentInQueueWS3);
        System.out.printf("Total processing time for WS1: %.2f%n", totalProcessingTimeWS1);
        System.out.printf("Total processing time for WS2: %.2f%n", totalProcessingTimeWS2);
        System.out.printf("Total processing time for WS3: %.2f%n", totalProcessingTimeWS3);
        System.out.printf("Total processing time for facility: %.2f%n", totalProcessingTimeWS1 + totalProcessingTimeWS2 + totalProcessingTimeWS3);
        System.out.printf("Total time spent in system for WS1: %.2f%n", totalTimeSpentInQueueWS1 + totalProcessingTimeWS1);
        System.out.printf("Total time spent in system for WS2: %.2f%n", totalTimeSpentInQueueWS2 + totalProcessingTimeWS2);
        System.out.printf("Total time spent in system for WS3: %.2f%n", totalTimeSpentInQueueWS3 + totalProcessingTimeWS3);
        System.out.printf("Total time spent in system for facility: %.2f%n", totalTimeSpentInQueueWS1 + totalTimeSpentInQueueWS2 + totalTimeSpentInQueueWS3
                + totalProcessingTimeWS1 + totalProcessingTimeWS2 + totalProcessingTimeWS3);
        printLittlesLawResults();
        printIOValidation();
}
    public void printLittlesLawResults() {
        double avgTimeInSystemWS1 = (totalTimeSpentInQueueWS1 + totalProcessingTimeWS1) / completedP1;
        double avgTimeInSystemWS2 = (totalTimeSpentInQueueWS2 + totalProcessingTimeWS2) / completedP2;
        double avgTimeInSystemWS3 = (totalTimeSpentInQueueWS3 + totalProcessingTimeWS3) / completedP3;

        double lambda1 = (double) completedP1 / simulationTime;
        double lambda2 = (double) completedP2 / simulationTime;
        double lambda3 = (double) completedP3 / simulationTime;

        double L1 = lambda1 * avgTimeInSystemWS1;
        double L2 = lambda2 * avgTimeInSystemWS2;
        double L3 = lambda3 * avgTimeInSystemWS3;

        double avgTimeInSystemFacility = (totalTimeSpentInQueueWS1 + totalTimeSpentInQueueWS2 + totalTimeSpentInQueueWS3
                + totalProcessingTimeWS1 + totalProcessingTimeWS2 + totalProcessingTimeWS3) / (completedP1 + completedP2 + completedP3);

        double lambdaFacility = (double) (completedP1 + completedP2 + completedP3) / simulationTime;

        double LFacility = lambdaFacility * avgTimeInSystemFacility;

        System.out.println("-------------------------------------2. LITTLE LAW-------------------------------------");
        System.out.printf("Workstation 1 - Avg time in system (W): %.2f, Avg arrival rate (λ): %.4f, Avg # of components in system (L): %.2f%n", avgTimeInSystemWS1, lambda1, L1);
        System.out.printf("Workstation 2 - Avg time in system (W): %.2f, Avg arrival rate (λ): %.4f, Avg # of components in system (L): %.2f%n", avgTimeInSystemWS2, lambda2, L2);
        System.out.printf("Workstation 3 - Avg time in system (W): %.2f, Avg arrival rate (λ): %.4f, Avg # of components in system (L): %.2f%n", avgTimeInSystemWS3, lambda3, L3);
        System.out.printf("Facility - Avg time in system (W): %.2f, Avg arrival rate (λ): %.4f, Avg # of components in system (L): %.2f%n", avgTimeInSystemFacility, lambdaFacility, LFacility);
    }

    public void printIOValidation(){
        // The true buffer occupancy values provided in the project document
        double[] trueBufferOccupancyValues = {avg1, avg22, avg23, ws1, ws2, ws3};
        String[] variableNames = {"Inspector 1 Buffer 1", "Inspector 2 Buffer 2", "Inspector 2 Buffer 3", "Workstation 1", "Workstation 2", "Workstation 3"};
        // Add more variable names as needed
        // To Calculate the critical difference
        double criticalDifference = 0.02; // For example, 0.02

        // Get the simulated buffer occupancy values
        double[] simulatedBufferOccupancyValues = {I1, I22, I23, W1, W2, W3};
        // Check if the simulated values match the true values within the critical difference
        boolean[] matches = new boolean[simulatedBufferOccupancyValues.length];
        System.out.println("-------------------------------------3. INPUT/OUTPUT VALIDATION-------------------------------------");
        for (int i = 0; i < simulatedBufferOccupancyValues.length; i++) {
            double difference = Math.abs(simulatedBufferOccupancyValues[i] - trueBufferOccupancyValues[i]);
            System.out.println("In " + variableNames[i] +  " , the difference between the True Occupancy Values and the Simulated Occupancy Values is: " + difference);
            matches[i] = difference <= criticalDifference;
        }
        // Print the results
        for (int i = 0; i < matches.length; i++) {
            System.out.println("Buffer " + (i + 1) + ": " + (matches[i] ? "Match" : "No Match"));
        }
    }

    public void simulate(double simulationTime) {
        long startTime = System.currentTimeMillis();
        // Initialize inspectors with their respective average processing times
        Inspector inspector1 = new Inspector(1, 0);
        Inspector inspector2 = new Inspector(2, 0);
        // Create components
        Component c1 = new Component("C1", 1, 1, 1, 1);
        Component c2 = new Component("C2", 2, 0, 1, 0);
        Component c3 = new Component("C3", 2, 0, 0, 1);
        // Initialize workstations
        Workstation workstation1 = new Workstation(1);
        Workstation workstation2 = new Workstation(2);
        Workstation workstation3 = new Workstation(3);
        // Initialize event queue
        eventQueue = new PriorityQueue<>();
        //eventQueueNew = new PriorityQueue<>();
        // Add initial inspection finished events
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, avg1, c1, inspector1, null));
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, avg22, c2, inspector2, null));
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, avg23, c3, inspector2, null));
        // Run the simulation for a fixed time 10,000 seconds in this case
        while (!eventQueue.isEmpty() && eventQueue.peek().getTime() <= simulationTime) {
            Event event = eventQueue.poll();
            //eventQueueNew.add(event);

            if (event != null) {
                switch (event.getType()) {
                    case INSPECTION_FINISHED -> handleInspectionFinished(event, workstation1, workstation2, workstation3, eventQueue);
                    case ASSEMBLY_STARTED -> handleAssemblyStarted(event, eventQueue);
                    case ASSEMBLY_FINISHED -> handleAssemblyFinished(event);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        finished = endTime - startTime;
        printResults();
    }



}
