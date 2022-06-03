public class Task {

    protected String name;
    protected String description;
    private final int id;
    protected static String[] allStatus = {"NEW", "IN_PROGRESS", "DONE"};
    private String status;
    private static int count = 0;

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
        count++;
        this.id = count;
    }

    public int getId() {
        return id;
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
