import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Simulation {
    public double avg1, avg22, avg23; // Inspector Average Values
    public double ws1, ws2, ws3; // Workstation Average Values
    int completedP1;
    int completedP2;
    int completedP3; // Completed Components
    double simulationTime; // Add this line

    public Simulation() {
        // Setting Average times of each '.dat' file
        avg1 = AvgTime("ProjectFiles/servinsp1.dat");
        avg22 = AvgTime("ProjectFiles/servinsp22.dat");
        avg23 = AvgTime("ProjectFiles/servinsp23.dat");
        ws1 = AvgTime("ProjectFiles/ws1.dat");
        ws2 = AvgTime("ProjectFiles/ws2.dat");
        ws3 = AvgTime("ProjectFiles/ws3.dat");
    }

    public Simulation(double simulationTime) {
        this();
        this.simulationTime = simulationTime;
    }

    // This method reads the .dat files and calculates the average processing times
    public double AvgTime(String filename) {
        try {
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
        Component c1 = new Component("C1", 1, 0, 1, 1, 1);
        Component c2 = new Component("C2", 2, 0, 0, 1, 0);
        Component c3 = new Component("C3", 2, 0, 0, 0, 1);


        // Initialize workstations
        Workstation workstation1 = new Workstation(1);
        Workstation workstation2 = new Workstation(2);
        Workstation workstation3 = new Workstation(3);


        // Initialize event queue
        PriorityQueue<Event> eventQueue = new PriorityQueue<>();

        // Add initial inspection finished events
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, 0, c1, inspector1, null));
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, 0, c2, inspector2, null));
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, 0, c3, inspector2, null));

        // Run the simulation for a fixed time (10,000 seconds in this case)
        double simulationTime = 10000.0;
        int assembledProducts = 0;
        // Run the simulation for the given simulation time
        while (!eventQueue.isEmpty() && eventQueue.peek().getTime() <= simulationTime) {
            Event event = eventQueue.poll();

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

        // Add the component to the target workstation's buffer
        targetWorkstation.addComponentToBuffer(component);
        inspector.setQueue(targetWorkstation.isInspectorBlocked() ? 1 : 0);

        // Schedule the next inspection finished event if the inspector is not blocked
        if (!targetWorkstation.isInspectorBlocked()) {
            double nextInspectionTime = event.getTime() + (inspector.getId() == 1 ? avg1 : (component.getType().equals("C2") ? avg22 : avg23));
            eventQueue.add(new Event(EventType.INSPECTION_FINISHED, nextInspectionTime, component, inspector, null));
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
        if (workstation.getId() == 1) {
            completedP1++;
            productType = "P1";
        } else if (workstation.getId() == 2) {
            completedP2++;
            productType = "P2";
        } else {
            completedP3++;
            productType = "P3";
        }

        System.out.printf("Workstation %d finished assembling product %s at time %.2f%n", workstation.getId(), productType, event.getTime());
    }

    public static void main(String[] args) {
        // monte carlo replication -> Validation of the simulation model
        int numberOfReplications = 12; // Number of replications for Monte Carlo simulation
        double totalCompletedP1 = 0;
        double totalCompletedP2 = 0;
        double totalCompletedP3 = 0;

        for (int i = 0; i < numberOfReplications; i++) {
            Simulation simulation = new Simulation();
            simulation.simulate();
            totalCompletedP1 += simulation.completedP1;
            totalCompletedP2 += simulation.completedP2;
            totalCompletedP3 += simulation.completedP3;
        }

        double averageCompletedP1 = totalCompletedP1 / numberOfReplications;
        double averageCompletedP2 = totalCompletedP2 / numberOfReplications;
        double averageCompletedP3 = totalCompletedP3 / numberOfReplications;

        System.out.printf("Average assembled products for P1: %.2f%n", averageCompletedP1);
        System.out.printf("Average assembled products for P2: %.2f%n", averageCompletedP2);
        System.out.printf("Average assembled products for P3: %.2f%n", averageCompletedP3);
    }


}
