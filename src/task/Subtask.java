package task;

public class Subtask extends Task {
    private final Epic parent;

    public Subtask(String name, String description, Task.Status status, Epic parent) {
        super(name, description, status);
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "\nTask.Subtask{" +
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
