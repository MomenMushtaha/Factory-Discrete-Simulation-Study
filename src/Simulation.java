import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Simulation {
    public double avg1, avg22, avg23; // Inspector Average Values
    public double ws1, ws2, ws3; // Workstation Average Values
    int completedP1;
    int completedP2;
    int completedP3; // Completed Components
    double simulationTime ; // Add this line
    double totalTimeSpentInQueueWS1 = 0;
    double totalTimeSpentInQueueWS2 = 0;
    double totalTimeSpentInQueueWS3 = 0;
    double totalProcessingTimeWS1 = 0;
    double totalProcessingTimeWS2 = 0;
    double totalProcessingTimeWS3 = 0;

    HashMap<String, Double> timeComponentEnteredQueue = new HashMap<>();
    HashMap<String, Double> timeComponentLeftQueue = new HashMap<>();

    PriorityQueue<Event> eventQueue;
    //PriorityQueue<Event> eventQueueNew;


    public Simulation() {
        // Setting Average times of each '.dat' file
        avg1 = AvgTime("ProjectFiles/servinsp1.dat");
        avg22 = AvgTime("ProjectFiles/servinsp22.dat");
        avg23 = AvgTime("ProjectFiles/servinsp23.dat");
        ws1 = AvgTime("ProjectFiles/ws1.dat");
        ws2 = AvgTime("ProjectFiles/ws2.dat");
        ws3 = AvgTime("ProjectFiles/ws3.dat");
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
            double average = sum / count;

            return average;
        } catch (FileNotFoundException e) {
            System.out.println("The file could not be found.");
        } catch (NumberFormatException e) {
            System.out.println("The file contains an invalid number.");
        }
        return 0;
    }

    public void simulate() {
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

        // Run the simulation for a fixed time (10,000 seconds in this case)
        simulationTime = 10000.0;
        int assembledProducts = 0;
        while (!eventQueue.isEmpty() && eventQueue.peek().getTime() <= simulationTime) {
            Event event = eventQueue.poll();
            //eventQueueNew.add(event);

            switch (event.getType()) {
                case INSPECTION_FINISHED -> handleInspectionFinished(event, workstation1, workstation2, workstation3, eventQueue);
                case ASSEMBLY_STARTED -> handleAssemblyStarted(event, eventQueue);
                case ASSEMBLY_FINISHED -> {
                    handleAssemblyFinished(event);
                    assembledProducts++;
                }

            }


        }

        System.out.println("Assembled products: " + assembledProducts);


        printLittlesLawResults();
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
            double timeSpentInQueue = event.getTime() - timeComponentEnteredQueue.get(key);
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

    public void printLittlesLawResults() {
        double averageTimeInSystemWS1 = (totalTimeSpentInQueueWS1 + totalProcessingTimeWS1) / completedP1;
        double averageTimeInSystemWS2 = (totalTimeSpentInQueueWS2 + totalProcessingTimeWS2) / completedP2;
        double averageTimeInSystemWS3 = (totalTimeSpentInQueueWS3 + totalProcessingTimeWS3) / completedP3;


        double lambda1 = (double) completedP1 / simulationTime;
        double lambda2 = (double) completedP2 / simulationTime;
        double lambda3 = (double) completedP3 / simulationTime;

        double L1 = lambda1 * averageTimeInSystemWS1;
        double L2 = lambda2 * averageTimeInSystemWS2;
        double L3 = lambda3 * averageTimeInSystemWS3;

        System.out.printf("Little's Law Results:%n");
        System.out.printf("Workstation 1 - Average time in system (W): %.2f, Average arrival rate (λ): %.4f, Average number of components in system (L): %.2f%n", averageTimeInSystemWS1, lambda1, L1);
        System.out.printf("Workstation 2 - Average time in system (W): %.2f, Average arrival rate (λ): %.4f, Average number of components in system (L): %.2f%n", averageTimeInSystemWS2, lambda2, L2);
        System.out.printf("Workstation 3 - Average time in system (W): %.2f, Average arrival rate (λ): %.4f, Average number of components in system (L): %.2f%n", averageTimeInSystemWS3, lambda3, L3);
    }

    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.simulate();
    }
}
