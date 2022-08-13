package main.manager;

import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.util.*;

public interface TaskManager {

    List<Task> getHistory();

    Set<Task> getTaskSet();

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

    List<String> getTasksFromEpicTask(Epic epic);
}
