import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends Task {

    private ArrayList<Subtask> childSubtasks;

    public Epic(String name, String description, Task.Status status) {
        super(name, description, status);
        childSubtasks = new ArrayList<>();
        setStatus(Status.NEW);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() + '\'' +
                ", status=" + getStatus() + '\'' +
                Arrays.toString(new ArrayList[]{childSubtasks}) +
                '}';
    }

    public ArrayList<Subtask> getChildSubtasks() {
        return childSubtasks;
    }

    public void checkStatus() {
        int doneCounter = 0;
        if (childSubtasks.size() == 0) {
            setStatus(Status.NEW);
            return;
        }
        for (Subtask task : childSubtasks) {
            if (task.getStatus().equals(Status.DONE)) {
                doneCounter++;
            } else if (task.getStatus().equals(Status.IN_PROGRESS)) {
                setStatus(Status.IN_PROGRESS);
                break;
            } else {
                if (doneCounter == 0) {
                    setStatus(Status.NEW);
                } else {
                    setStatus(Status.IN_PROGRESS);
                }
            }
        }
        if (doneCounter == this.childSubtasks.size() && doneCounter != 0) {
            setStatus(Status.DONE);
        }
    }

    @Override
    public void changeStatus(Status newStatus) {
        setStatus(getStatus());
    }
}
