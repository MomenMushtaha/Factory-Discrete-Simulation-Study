public class Event implements Comparable<Event> {
    private EventType type;
    private double time;
    private Component component;
    private Inspector inspector;
    private Workstation workstation;

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

