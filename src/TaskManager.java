import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, Subtask> getSubtasks();

    Subtask addSubtask(Subtask subtask);

    Task addTask(Task task);

    Epic addEpic(Epic epic);

    void deleteTaskById(int id);

    ArrayList<String> getAllTypeTasksList(TaskType nameTaskTypeSet);

    void deleteAllTasksFromSet(TaskType nameTasksSet);

    Task getTaskById(int id);

    void updateEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    ArrayList<String> getTasksFromEpicTask(Epic epic);
}
