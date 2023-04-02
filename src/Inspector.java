// Represents an inspector with an ID, queue, and finish time
public class Inspector {
    private int id;
    private int queue;
    private double finishTime;

    public Inspector(int id, int queue) {
        this.id = id;
        this.queue = queue;
        this.finishTime = 0;
    }

    public int getId() {
        return id;
    }

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

}
