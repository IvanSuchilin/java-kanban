import java.util.ArrayList;

public class EpicTask extends Task {
   static ArrayList<Subtask> childSubtasks;

    public EpicTask(String name, String description, Subtask childSubtask) {
        int doneCounter =0;
        this.name = name;
        this.description = description;
        count++;
        this.id = count;
        childSubtasks = new ArrayList<>();
        childSubtasks.add(childSubtask);
        for (Subtask task: childSubtasks){
            if (task.status.equals(allStatus[2])){
                doneCounter++;
            } else if (task.status.equals(allStatus[1])){
                this.status = allStatus[1];
            } else {
                this.status = allStatus[0];
            }
        }
        if (doneCounter == this.childSubtasks.size()){
        this.status = allStatus[2];
        }

    }

    public EpicTask(String name, String description) {
        this.name = name;
        this.description = description;
        count++;
        this.id = count;
        this.childSubtasks = new ArrayList<>();
        this.status = allStatus[0];
    }

}
