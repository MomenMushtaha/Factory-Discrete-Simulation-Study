package Components;

public class Component {

    private final ComponentType type;

    public Component(ComponentType type) {
        this.type = type;
    }

    public ComponentType getType() {
        return this.type;
    }

}