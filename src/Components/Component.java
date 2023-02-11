package Components;

public class Component {

    private final CTypes[] componentType;

    public Component(CTypes[] componentType) {
        this.componentType = componentType;
    }

    public CTypes[] getType() {
        return this.componentType;
    }

}