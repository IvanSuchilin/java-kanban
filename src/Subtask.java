public class Subtask extends Task {
    private final EpicTask parent;

    public Subtask(String name, String description, String status, EpicTask parent) {
        super(name, description, status);
        this.parent = parent;
    }

    public EpicTask getParent() {
        return parent;
    }
}
