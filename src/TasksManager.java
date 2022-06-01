import java.util.HashMap;

public class TasksManager {

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Task> epicTasks = new HashMap<>();
    HashMap<Integer, Task> subTasks = new HashMap<>();
    //String[] allStatus = {"NEW","IN_PROGRESS","DONE"};
    private int counter;




    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }


    public void createChildSubtask (String name, String description, String status,int epicId) {
        Subtask subtask = new Subtask(name, description, status);
        epicTasks.get(epicId).childSubtasks.add(subtask);
    }
}
