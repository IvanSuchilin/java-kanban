package manager;

import task.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    List<Task> getHistory();

    Map<Integer, Task> getTasks();

    Map<Integer, Epic> getEpics();

    Map<Integer, Subtask> getSubtasks();

    Subtask addSubtask(Subtask subtask);

    Task addTask(Task task);

    Epic addEpic(Epic epic);

    void deleteTaskById(int id);

    ArrayList<String> getAllTypeTasksList(TaskType nameTaskTypeSet);

    void deleteAllTasksFromSet(TaskType nameTasksSet);

    Task getTaskById(int id) throws CloneNotSupportedException;

    void updateEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    ArrayList<String> getTasksFromEpicTask(Epic epic);
}