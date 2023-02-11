import Buffers_Product.Buffers;
import Buffers_Product.PTypes;
import Buffers_Product.Product;
import Components.CTypes;
import Workstations.Workstation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static Components.CTypes.C1;

public class Simulator {
    private int clock; // Clock time in thousandths of minutes
    private final HashMap<PTypes, Integer> production; // Final product counts
    private ArrayList<Buffers> workstationBuffers;


    public Simulator() {
        this.clock = 0;
        this.production = new HashMap<PTypes, Integer>(); // this is supposed to keep count of the products created
        for (PTypes product : PTypes.values()) {
            production.put(product, 0);
        }
    }

    public void simulate() {
        // Initialize workstations 1-3
        Workstation[] workstations = new Workstation[3];
        Workstation workstation1 = new Workstation(new CTypes[]{CTypes.C1});
        Workstation workstation2 = new Workstation(new CTypes[]{CTypes.C1, CTypes.C2});
        Workstation workstation3 = new Workstation(new CTypes[]{CTypes.C1, CTypes.C3});
        workstations[0] = workstation1;
        workstations[1] = workstation2;
        workstations[2] = workstation3;






















        // Initialize Inspectors 1 & 2
        Inspector[] inspectors = new Inspector[2];
        LinkedHashMap<ComponentType, String> insp1data = new LinkedHashMap<>();
        LinkedHashMap<ComponentType, String> insp2data = new LinkedHashMap<>();
        insp1data.put(ComponentType.C1, "servinsp1.dat");
        insp2data.put(ComponentType.C2, "servinsp22.dat");
        insp2data.put(ComponentType.C3, "servinsp23.dat");
        Inspector inspector1 = new Inspector(insp1data);
        Inspector inspector2 = new Inspector(insp2data);
        inspectors[0] = inspector1;
        inspectors[1] = inspector2;

        this.eventList = initialize(inspectors);
        // Action loop
        while(!eventList.isEmpty()) {
            Event currEvent = eventList.popEvent();
            this.clock = currEvent.getTime();
            System.out.println("\n"+this.clock + ": Current[" + currEvent.toString() +"] FEL=" + this.eventList.toString());
            switch(currEvent.getType()) { // Yield?
                case INSPECTOR_FINISH -> {
                    Inspector inspectorSource = currEvent.getInspectorSource();
                    Workstation workstation = getWorkstationWithSmallestBuffer(workstations,
                            inspectorSource.getCurrentComponent());
                    // Workstation has space
                    if(workstation != null) {
                        workstation.addComponentToBuffer(inspectorSource.getCurrentComponent()); // Workstation gets component
                        generateInspectorEvent(inspectorSource);
                        // Check workstations if they can start new product job
                        if(workstation.canStartNewProduct()) {
                            generateWorkstationEvent(workstation);
                            for(Inspector inspector : inspectors) {
                                if(inspector.getState().equals(State.IDLE) &&
                                        workstation.needsComponent(inspector.getCurrentComponent())) {
                                    workstation.addComponentToBuffer(inspector.getCurrentComponent());
                                    generateInspectorEvent(inspector);
                                }
                            }
                        }
                    }
                    // No workstations have space
                    else {
                        inspectorSource.setState(State.IDLE);
                    }
                }
                case WORKSTATION_FINISH -> {
                    Workstation workstationSource = currEvent.getWorkstationSource();
                    production.put(workstationSource.getProduct(), production.get(workstationSource.getProduct()) + 1);
                    workstationSource.setState(State.IDLE);
                    // Workstation can start a new product (has all components) -> can open space in buffers
                    if(workstationSource.canStartNewProduct()) {
                        generateWorkstationEvent(workstationSource);
                        // Check idle inspectors for components, add to workstation
                        for(Inspector inspector : inspectors) {
                            if(inspector.getState().equals(State.IDLE) &&
                                    workstationSource.needsComponent(inspector.getCurrentComponent())) {
                                workstationSource.addComponentToBuffer(inspector.getCurrentComponent());
                                generateInspectorEvent(inspector);
                            }
                        }
                    }
                    // Workstation cannot start a new product (missing required component)
                    else {
                        // Don't think anything needs to happen
                    }

                }
            }
        }
        System.out.println("Final Production: " + production.toString());
    }

    private void generateInspectorEvent(Inspector inspector) {
        if(inspector.isInspectionTimesEmpty()) return;
        int time = inspector.startNewComponent(); // Instructor starts new component
        Event newEvent = new Event(clock + time, EventType.INSPECTOR_FINISH, inspector);
        eventList.scheduleEvent(newEvent);
    }

    private void generateWorkstationEvent(Workstation workstation) {
        if(workstation.isProductionTimesEmpty()) return;
        int time = workstation.startNewProduct();
        Event newEvent = new Event(clock + time, EventType.WORKSTATION_FINISH, workstation);
        eventList.scheduleEvent(newEvent);
    }

    private FutureEventList initialize(Inspector[] inspectors) {
        // Should return a future event list of the initial inspector events
        FutureEventList initialList = new FutureEventList();
        for(Inspector inspector : inspectors) {
            Event newEvent = new Event(this.clock + inspector.startNewComponent(), EventType.INSPECTOR_FINISH, inspector);
            initialList.scheduleEvent(newEvent);
        }
        System.out.println(initialList);
        return initialList;
    }

    private Workstation getWorkstationWithSmallestBuffer(Workstation[] workstations, ComponentType component) {
        // Return the workstation with the smallest buffer / or highest priority if tied
        // Assumes that components will always have at least 1 assigned workstation
        // Returns null if no workstations available
        Workstation assignedWorkstation = null;
        int currWorkstationBufferOccupancy = Integer.MAX_VALUE;
        int currWorkstationPriority = -1;
        for(Workstation workstation : workstations) {
            if(workstation.needsComponent(component)) {
                if(workstation.getBufferOccupancy(component) <  currWorkstationBufferOccupancy &&
                        workstation.getPriority() > currWorkstationPriority) {
                    assignedWorkstation = workstation;
                }
            }
        }
        return assignedWorkstation;
    }

}