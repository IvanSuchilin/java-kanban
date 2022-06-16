public class Task {

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
    public String toString() {
        return "\nTask{" +
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

    enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }

}
