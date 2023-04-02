import java.util.HashMap;
import java.util.Map;

public class Workstation {
    private int id;
    private Map<String, Integer> buffer; // Modified buffer to store component type and count
    private double finishTime;

    public Workstation(int id) {
        this.id = id;
        this.buffer = new HashMap<>(); // Initialize buffer as an empty HashMap
        this.finishTime = 0;
    }

    public int getId() {
        return id;
    }

    public int getBuffer() {
        return buffer.size();
    }

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

    public void addComponentToBuffer(Component component) {
        String componentType = component.getType();
        int currentBufferCount = buffer.getOrDefault(componentType, 0);
        buffer.put(componentType, currentBufferCount + 1);
    }

    public void removeComponentFromBuffer(Component component) {
        String componentType = component.getType();
        int currentBufferCount = buffer.getOrDefault(componentType, 0);
        if (currentBufferCount > 0) {
            buffer.put(componentType, currentBufferCount - 1);
        }
    }

    public int getBufferCountForComponentType(String componentType) {
        return buffer.getOrDefault(componentType, 0);
    }

    public boolean isInspectorBlocked() {
        int totalCount = 0;
        for (int count : buffer.values()) {
            totalCount += count;
        }
        return totalCount >= 2;
    }
}
