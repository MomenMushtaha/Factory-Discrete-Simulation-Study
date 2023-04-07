//package Run;
//import Model.*;
//import Stats.WorkstationStatistics;
//import Util.Util;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.HashMap;
//import java.util.PriorityQueue;
//import java.util.Scanner;
//
//import static Util.Util.calculateAverageTime;
//
//public class Sim {
//
//    public Workstation workstation1, workstation2, workstation3;
//    public Inspector inspector1, inspector2;
//    public WorkstationStatistics workstationStats;
//    public double averageInsProcessingTime1, averageInsProcessingTime22, averageInsProcessingTime23; // Inspector Average Values
//    public double workstationProcessingTime1, workstationProcessingTime2, workstationProcessingTime3; // Workstation Average Values
//    public int completedP1, completedP2, completedP3; // Completed Components
//    double simulationTime ; // Add this line
//    double totalTimeSpentInQueueWS1 = 0, totalTimeSpentInQueueWS2 = 0, totalTimeSpentInQueueWS3 = 0;
//    double totalProcessingTimeWS1 = 0, totalProcessingTimeWS2 = 0, totalProcessingTimeWS3 = 0;
//
//    HashMap<String, Double> timeComponentEnteredQueue = new HashMap<>();
//    HashMap<String, Double> timeComponentLeftQueue = new HashMap<>();
//
//    PriorityQueue<Event> eventQueue;
//
//    public Sim() {
//        // Setting Average times of each '.dat' file
//        averageInsProcessingTime1 = calculateAverageTime("ProjectFiles/servinsp1.dat");
//        averageInsProcessingTime22 = calculateAverageTime("ProjectFiles/servinsp22.dat");
//        averageInsProcessingTime23 = calculateAverageTime("ProjectFiles/servinsp23.dat");
//        workstationProcessingTime1 = calculateAverageTime("ProjectFiles/ws1.dat");
//        workstationProcessingTime2 = calculateAverageTime("ProjectFiles/ws2.dat");
//        workstationProcessingTime3 = calculateAverageTime("ProjectFiles/ws3.dat");
//    }
//
//    public void simulate() {
//        initializeSim();
//        processEvents();
//        printStats();
//    }
//
//    public void initializeSim() {
//        // Initialize inspectors with their respective average processing times
//        Inspector inspector1 = new Inspector(1, 0);
//        Inspector inspector2 = new Inspector(2, 0);
//
//        // Create components
//        Component c1 = new Component("C1", 1, 1, 1, 1);
//        Component c2 = new Component("C2", 2, 0, 1, 0);
//        Component c3 = new Component("C3", 2, 0, 0, 1);
//
//        // Initialize workstations
//        Workstation workstation1 = new Workstation(1);
//        Workstation workstation2 = new Workstation(2);
//        Workstation workstation3 = new Workstation(3);
//
//        workstationStats = new WorkstationStatistics();
//
//        workstationStats.addWorkstation(workstation1);
//        workstationStats.addWorkstation(workstation2);
//        workstationStats.addWorkstation(workstation3);
//
//        // Initialize event queue
//        eventQueue = new PriorityQueue<>();
//
//
//        // Initialize event queue with the first inspection finished event
//        double inspectionTime = averageInsProcessingTime1;
//        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, inspectionTime));
//
//        // Initialize event queue with the first assembly started event for each workstation
//        double assemblyStartTime1 = workstationProcessingTime1;
//        eventQueue.add(new Event(EventType.ASSEMBLY_STARTED, assemblyStartTime1, 1));
//
//        double assemblyStartTime2 = workstationProcessingTime2;
//        eventQueue.add(new Event(EventType.ASSEMBLY_STARTED, assemblyStartTime2, 2));
//
//        double assemblyStartTime3 = workstationProcessingTime3;
//        eventQueue.add(new Event(EventType.ASSEMBLY_STARTED, assemblyStartTime3, 3));
//    }
//
//    public void processEvents() {
//        while (!eventQueue.isEmpty() && eventQueue.peek().getTime() <= simulationTime) {
//            Event event = eventQueue.poll();
//
//            switch (event.getType()) {
//                case INSPECTION_FINISHED:
//                    handleInspectionFinished(event, workstation1, workstation2, workstation3, eventQueue);
//                    break;
//                case ASSEMBLY_STARTED:
//                    handleAssemblyStarted(event, eventQueue);
//                    break;
//                case ASSEMBLY_FINISHED:
//                    handleAssemblyFinished(event);
//                    break;
//            }
//        }
//    }
//
//    public void printStats() {
//        System.out.println("Assembled products: " + (completedP1 + completedP2 + completedP3));
//        System.out.println("Workstation 1 - Completed products: " + completedP1);
//        System.out.println("Workstation 2 - Completed products: " + completedP2);
//        System.out.println("Workstation 3 - Completed products: " + completedP3);
//
//        printLittlesLawResults();
//    }
//
//    // This method handles the inspection finished events
//    private void handleInspectionFinished(Event event, Workstation workstation1, Workstation workstation2, Workstation workstation3, PriorityQueue<Event> eventQueue) {
//        // ...
//        scheduleNextInspection(event, targetWorkstation, eventQueue);
//        scheduleAssemblyEvents(event, workstation1, workstation2, workstation3, eventQueue);
//    }
//
//    private void scheduleNextInspection(Event event, Workstation targetWorkstation, PriorityQueue<Event> eventQueue) {
//        // ...
//    }
//
//    private void scheduleAssemblyEvents(Event event, Workstation workstation1, Workstation workstation2, Workstation workstation3, PriorityQueue<Event> eventQueue) {
//        // ...
