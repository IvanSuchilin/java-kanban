public class Task {

    protected String name;
    protected String description;
    private int id;
    protected static String[] allStatus = {"NEW", "IN_PROGRESS", "DONE"};
    private String status;

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void changeStatus(String newStatus) {
        setStatus(newStatus);
    }
}
