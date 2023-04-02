import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Simulation {
    public double avg1, avg22, avg23; // Inspector Average Values
    public double ws1, ws2, ws3; // Workstation Average Values
    private int completedP1, completedP2, completedP3; // Completed Components

    public Simulation() {
        // Setting Average times of each '.dat' file
        avg1 = AvgTime("ProjectFiles/servinsp1.dat");
        avg22 = AvgTime("ProjectFiles/servinsp22.dat");
        avg23 = AvgTime("ProjectFiles/servinsp23.dat");
        ws1 = AvgTime("ProjectFiles/ws1.dat");
        ws2 = AvgTime("ProjectFiles/ws2.dat");
        ws3 = AvgTime("ProjectFiles/ws3.dat");

    }

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
        Inspector inspector1 = new Inspector(1, 0);
        Inspector inspector2 = new Inspector(2, 0);
        Workstation workstation1 = new Workstation(1);
        Workstation workstation2 = new Workstation(2);
        Workstation workstation3 = new Workstation(3);

        Component c1 = new Component("C1", 1, 0, 1, 1, 1);
        Component c2 = new Component("C2", 2, 0, 0, 1, 0);
        Component c3 = new Component("C3", 2, 0, 0, 0, 1);

        PriorityQueue<Event> eventQueue = new PriorityQueue<>();

        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, 0, c1, inspector1, null));
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, 0, c2, inspector2, null));
        eventQueue.add(new Event(EventType.INSPECTION_FINISHED, 0, c3, inspector2, null));

        double simulationTime = 10000.0;
        int assembledProducts = 0;
        while (!eventQueue.isEmpty() && eventQueue.peek().getTime() <= simulationTime) {
            Event event = eventQueue.poll();

            switch (event.getType()) {
                case INSPECTION_FINISHED -> handleInspectionFinished(event, workstation1, workstation2, workstation3, eventQueue);
                case ASSEMBLY_STARTED -> handleAssemblyStarted(event, eventQueue);
                case ASSEMBLY_FINISHED -> { handleAssemblyFinished(event);
                    assembledProducts++;
                }
            }
        }
        System.out.println("Assembled products: " + assembledProducts);
    }

    private void handleInspectionFinished(Event event, Workstation workstation1, Workstation workstation2, Workstation workstation3, PriorityQueue<Event> eventQueue) {
        Component component = event.getComponent();
        Inspector inspector = event.getInspector();
        Workstation targetWorkstation = null;

        if (component.getType().equals("C1")) {
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

        targetWorkstation.addComponentToBuffer(component);
        inspector.setQueue(targetWorkstation.isInspectorBlocked() ? 1 : 0);

        if (!targetWorkstation.isInspectorBlocked()) {
            double nextInspectionTime = event.getTime() + (inspector.getId() == 1 ? avg1 : (component.getType().equals("C2") ? avg22 : avg23));
            eventQueue.add(new Event(EventType.INSPECTION_FINISHED, nextInspectionTime, component, inspector, null));
        }

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



    private void handleAssemblyStarted(Event event, PriorityQueue<Event> eventQueue) {
        Workstation workstation = event.getWorkstation();
        Component component = event.getComponent();
        workstation.removeComponentFromBuffer(component);

        double assemblyTime;
        if (workstation.getId() == 1) {
            assemblyTime = ws1;
        } else if (workstation.getId() == 2) {
            assemblyTime = ws2;
        } else {
            assemblyTime = ws3;
        }

        double assemblyFinishTime = event.getTime() + assemblyTime;
        eventQueue.add(new Event(EventType.ASSEMBLY_FINISHED, assemblyFinishTime, component, null, workstation));
    }

    private void handleAssemblyFinished(Event event) {
        Workstation workstation = event.getWorkstation();
        String productType;

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
        Simulation sim = new Simulation();
        sim.simulate();
    }
}