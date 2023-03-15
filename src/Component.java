public class Component {
    private String type;
    private int inspectorId;
    private double arrivalTime;

    public Component(String type, int inspectorId, double arrivalTime) {
        this.type = type;
        this.inspectorId = inspectorId;
        this.arrivalTime = arrivalTime;
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

}
