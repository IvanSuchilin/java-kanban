package Task;

public class Task implements Cloneable {

    private String name;
    private String description;
    private int id;
    private Status status;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    @Override
    public Task clone() throws CloneNotSupportedException {
        super.clone();
        Task addTask = new Task(this.name, this.description, this.status);
        addTask.setId(this.id);
        return addTask;
    }

    @Override
    public String toString() {
        return "\nTask.Task{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() + '\'' +
                ", status=" + getStatus() +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void changeStatus(Status newStatus) {
        setStatus(newStatus);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }
}
