public class Workstation {
    private int id;
    private int buffer;
    private Component component;
    private double finishTime;

    public Workstation(int id, int buffer) {
        this.id = id;
        this.buffer = buffer;
        this.component = null;
        this.finishTime = 0;
    }

    public int getId() {
        return id;
    }

    public int getBuffer() {
        return buffer;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

}
