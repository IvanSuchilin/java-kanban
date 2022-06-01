public class Subtask extends Task {
    protected int parentsId;

    public Subtask(String name, String description, String status) {
        super(name, description, status);
        count++;
        this.id = count;
    }
}
