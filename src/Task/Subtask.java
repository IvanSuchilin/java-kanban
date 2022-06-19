package Task;

public class Subtask extends Task {
    private final Epic parent;

    public Subtask(String name, String description, Task.Status status, Epic parent) {
        super(name, description, status);
        this.parent = parent;
    }

    @Override
    public Subtask clone() throws CloneNotSupportedException {
        super.clone();
        Subtask addTask = new Subtask(this.getName(), this.getDescription(), this.getStatus(), this.getParent());
        addTask.setId(this.getId());
        return addTask;
    }

    @Override
    public String toString() {
        return "Task.Subtask{" +
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
