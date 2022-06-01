public class Task {

    protected String name;
    protected String description;
    protected int id;
    protected static String[] allStatus = {"NEW","IN_PROGRESS","DONE"};

    protected String status;
    protected static int count = 0;


    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
        count++;
        this.id = count;
    }

    public Task() {
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*public String getStatus() {
        return status;
    }*/
}
