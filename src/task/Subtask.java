package task;

public class Subtask extends Task {
    private final Epic parent;

    public Subtask(String name, String description, Task.Status status, Epic parent) {
        super(name, description, status);
        this.parent = parent;
    }

    @Override
    public String toCsvString() {
        return String.join(",", Integer.toString(getId()), TaskType.SUBTASK.toString(), getName(),
                getStatus().toString(), getDescription(), Integer.toString(getParent().getId()));
    }

    @Override
    public String toString() {
        return "\nSubtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", parent=" + getParent().getName() +
                '}';
    }

    public Epic getParent() {
        return parent;
    }
}
