package Run;

import Model.*;
import Stats.WorkstationStatistics;
import Util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Simulation {

    public Workstation workstation1, workstation2, workstation3;
    public Inspector inspector1, inspector2;
    public double avg1, avg22, avg23; // Inspector Average Values
    public double ws1, ws2, ws3; // Workstation Average Values
    public int completedP1, completedP2, completedP3; // Completed Components
    public double totalTimeSpentInQueueWS1 = 0, totalTimeSpentInQueueWS2 = 0, totalTimeSpentInQueueWS3 = 0;
    public double totalProcessingTimeWS1 = 0, totalProcessingTimeWS2 = 0, totalProcessingTimeWS3 = 0;

    private final HashMap<String, Double> timeComponentEnteredQueue = new HashMap<>();
    private final HashMap<String, Double> timeComponentLeftQueue = new HashMap<>();
    public PriorityQueue<Event> eventQueue;
    public WorkstationStatistics workstationStats;
    public double simulationTime = 10000;


    public Simulation() {
        // Setting Average times of each '.dat' file
        avg1 = Util.calculateAverageTime("ProjectFiles/servinsp1.dat");
        avg22 = Util.calculateAverageTime("ProjectFiles/servinsp22.dat");
        avg23 = Util.calculateAverageTime("ProjectFiles/servinsp23.dat");
        ws1 = Util.calculateAverageTime("ProjectFiles/ws1.dat");
        ws2 = Util.calculateAverageTime("ProjectFiles/ws2.dat");
        ws3 = Util.calculateAverageTime("ProjectFiles/ws3.dat");
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
        Workstation workstation1 = new Workstation(1);
        Workstation workstation2 = new Workstation(2);
        Workstation workstation3 = new Workstation(3);

        workstationStats = new WorkstationStatistics();

        workstationStats.addWorkstation(workstation1);
        workstationStats.addWorkstation(workstation2);
        workstationStats.addWorkstation(workstation3);

        // Initialize event queue
        eventQueue = new PriorityQueue<>();
        //eventQueueNew = new PriorityQueue<>();

        // Add initial inspection finished events
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, avg1, c1, inspector1, null));
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, avg22, c2, inspector2, null));
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, avg23, c3, inspector2, null));
        // Run the simulation for a fixed time (10,000 seconds in this case)
        int assembledProducts = 0;
        while (!eventQueue.isEmpty() && eventQueue.peek().getTime() <= simulationTime) {
            Event event = eventQueue.poll();
            //eventQueueNew.add(event);

            switch (event.getType()) {
                case INSPECTION_FINISHED ->
                        handleInspectionFinished(event, workstation1, workstation2, workstation3, eventQueue);
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

        System.out.println("Little's Law Results:");
        System.out.printf("Workstation 1 - Avg time in system (W): %.2f, Avg arrival rate (λ): %.4f, Avg # of components in system (L): %.2f%n", avgTimeInSystemWS1, lambda1, L1);
        System.out.printf("Workstation 2 - Avg time in system (W): %.2f, Avg arrival rate (λ): %.4f, Avg # of components in system (L): %.2f%n", avgTimeInSystemWS2, lambda2, L2);
        System.out.printf("Workstation 3 - Avg time in system (W): %.2f, Avg arrival rate (λ): %.4f, Avg # of components in system (L): %.2f%n", avgTimeInSystemWS3, lambda3, L3);
        System.out.printf("Facility - Avg time in system (W): %.2f, Avg arrival rate (λ): %.4f, Avg # of components in system (L): %.2f%n", avgTimeInSystemFacility, lambdaFacility, LFacility);
    }

    public static void main(String[] args) {
        int n = 10; // Initial number of replications
        double alpha = 0.05;
        double beta = 0.1;
        double maxConfidenceIntervalWidthPercentage = 0.2;
        boolean areConfidenceIntervalsAcceptable = false;

        while (!areConfidenceIntervalsAcceptable) {
            List<Simulation> replications = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                Simulation sim = new Simulation();
                sim.simulate();
                replications.add(sim);
            }
            // Calculate average values for each workstation across all replications
            double avgP1 = replications.stream().mapToDouble(s -> s.completedP1).average().orElse(0);
            double avgP2 = replications.stream().mapToDouble(s -> s.completedP2).average().orElse(0);
            double avgP3 = replications.stream().mapToDouble(s -> s.completedP3).average().orElse(0);

            // Calculate standard deviations for each workstation across all replications
            double stdDevP1 = Math.sqrt(replications.stream().mapToDouble(s -> Math.pow(s.completedP1 - avgP1, 2)).sum() / (n - 1));
            double stdDevP2 = Math.sqrt(replications.stream().mapToDouble(s -> Math.pow(s.completedP2 - avgP2, 2)).sum() / (n - 1));
            double stdDevP3 = Math.sqrt(replications.stream().mapToDouble(s -> Math.pow(s.completedP3 - avgP3, 2)).sum() / (n - 1));

            // Calculate confidence intervals for each workstation
            double tValue = Util.getTValue(alpha, n);
            double confidenceIntervalWidthP1 = tValue * stdDevP1 / Math.sqrt(n);
            double confidenceIntervalWidthP2 = tValue * stdDevP2 / Math.sqrt(n);
            double confidenceIntervalWidthP3 = tValue * stdDevP3 / Math.sqrt(n);

            // Check if confidence intervals do not exceed 20% of the estimated value
            if (confidenceIntervalWidthP1 / avgP1 <= maxConfidenceIntervalWidthPercentage &&
                    confidenceIntervalWidthP2 / avgP2 <= maxConfidenceIntervalWidthPercentage &&
                    confidenceIntervalWidthP3 / avgP3 <= maxConfidenceIntervalWidthPercentage) {
                areConfidenceIntervalsAcceptable = true;
            } else {
                n += 1; // Increase the number of replications if confidence intervals are not acceptable
            }
        }

        System.out.println("Number of replications needed: " + n);
    }

}
