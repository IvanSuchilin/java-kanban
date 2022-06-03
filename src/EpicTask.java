import java.util.ArrayList;

public class EpicTask extends Task {
    ArrayList<Subtask> childSubtasks;

    public EpicTask(String name, String description, String status) {
        super(name, description, status);
        childSubtasks = new ArrayList<>();
        setStatus(allStatus[0]);
    }

    public void checkStatus() {
        int doneCounter = 0;
        if (childSubtasks.size() == 0) {
            setStatus(allStatus[0]);
            return;
        }
        for (Subtask task : childSubtasks) {
            if (task.getStatus().equals(allStatus[2])) {
                doneCounter++;
            } else if (task.getStatus().equals(allStatus[1])) {
                setStatus(allStatus[1]);
                break;
            } else {
                if (doneCounter == 0) {
                    setStatus(allStatus[0]);
                } else {
                    setStatus(allStatus[1]);
                }
            }
        }
        if (doneCounter == this.childSubtasks.size() && doneCounter != 0) {
            setStatus(allStatus[2]);
        }
    }

    @Override
    public void changeStatus(String newStatus) {
        setStatus(getStatus());
    }
}
