package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface TaskManager {

    List<Task> getHistory();

    TreeSet<Task> getTaskSet();

    Map<Integer, Task> getTasks();

    Map<Integer, Epic> getEpics();

    Map<Integer, Subtask> getSubtasks();

    Subtask addSubtask(Subtask subtask);

    Task addTask(Task task);

    Epic addEpic(Epic epic);

    void deleteTaskById(int id);

    List<Task> getAllTypeTasksList(TaskType nameTaskTypeSet);

    void deleteAllTasksFromSet(TaskType nameTasksSet);

    Task getTaskById(int id) throws CloneNotSupportedException;

    void updateEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    ArrayList<String> getTasksFromEpicTask(Epic epic);
}
