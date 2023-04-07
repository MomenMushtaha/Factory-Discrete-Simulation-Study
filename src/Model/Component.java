package Model;

import java.util.Objects;
import java.util.UUID;

// Represents a component with its type, inspector ID, arrival time, and required quantities for products P1, P2, and P3
public class Component {
    private int id;
    private final String type;
    private final int inspectorId;
    private double arrivalTime;
    private final double requiredForP1;
    private final double requiredForP2;
    private final double requiredForP3;
    private double timeSpentInQueue;



    public Component(String type, int inspectorId, double requiredForP1, double requiredForP2, double requiredForP3) {
        this.type = type;
        this.inspectorId = inspectorId;
        this.requiredForP1 = requiredForP1;
        this.requiredForP2 = requiredForP2;
        this.requiredForP3 = requiredForP3;
    }

    public int getId() {
        return id;
    }

    //
    public void setId() {
        this.id = Integer.parseInt(generateUniqueId());
    }


    public static String generateUniqueId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
// Inside the Component class

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Component component = (Component) o;
        return inspectorId == component.inspectorId &&
                requiredForP1 == component.requiredForP1 &&
                requiredForP2 == component.requiredForP2 &&
                requiredForP3 == component.requiredForP3 &&
                Objects.equals(type, component.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, inspectorId, requiredForP1, requiredForP2, requiredForP3);
    }

    public String getType() {
        return type;
    }
    public double getTimeSpentInQueue() {
        return timeSpentInQueue;
    }

    public void setTimeSpentInQueue(double timeSpentInQueue) {
        this.timeSpentInQueue = timeSpentInQueue;
    }

    public int getInspectorId() {
        return inspectorId;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getRequiredForP1() {
        return requiredForP1;
    }

    public double getRequiredForP2() {
        return requiredForP2;
    }

    public double getRequiredForP3() {
        return requiredForP3;
    }

}
