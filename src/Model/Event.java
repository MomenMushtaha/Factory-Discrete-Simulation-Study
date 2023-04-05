package Model;
// Represents an event with a type, time, associated component, inspector, and workstation
public class Event implements Comparable<Event> {
    private final EventType type;
    private final double time;
    private final Component component;
    private final Inspector inspector;
    private final Workstation workstation;

    public Event(EventType type, double time, Component component, Inspector inspector, Workstation workstation) {
        this.type = type;
        this.time = time;
        this.component = component;
        this.inspector = inspector;
        this.workstation = workstation;
    }


    public EventType getType() {
        return type;
    }

    public double getTime() {
        return time;
    }

    public Component getComponent() {
        return component;
    }

    public Inspector getInspector() {
        return inspector;
    }

    public Workstation getWorkstation() {
        return workstation;
    }

    public int compareTo(Event other) {
        return Double.compare(time, other.time);
    }
}

