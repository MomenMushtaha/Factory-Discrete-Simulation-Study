// Represents a component with its type, inspector ID, arrival time, and required quantities for products P1, P2, and P3
public class Component {
    private String type;
    private int inspectorId;
    private double arrivalTime;
    private int requiredForP1, requiredForP2, requiredForP3;

    public Component(String type, int inspectorId, double arrivalTime, int requiredForP1, int requiredForP2, int requiredForP3) {
        this.type = type;
        this.inspectorId = inspectorId;
        this.arrivalTime = arrivalTime;
        this.requiredForP1 = requiredForP1;
        this.requiredForP2 = requiredForP2;
        this.requiredForP3 = requiredForP3;
    }

    public String getType() {
        return type;
    }

    public int getInspectorId() {
        return inspectorId;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public int getRequiredForP1() {
        return requiredForP1;
    }

    public int getRequiredForP2() {
        return requiredForP2;
    }

    public int getRequiredForP3() {
        return requiredForP3;
    }

}
