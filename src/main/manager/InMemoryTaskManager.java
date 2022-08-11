package main.manager;

import main.manager.exception.TaskValidationException;
import main.resources.TimeValidator;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {


    private final TreeSet<Task> taskSet = new TreeSet<>();

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private int count = 0;

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public TreeSet<Task> getTaskSet() {
        return taskSet;
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        try {
            if (!TimeValidator.validateTime(subtask, getPrioritizedTasks())) {
                throw new TaskValidationException("Заданное время задачи пересекается по времени с другой задачей");
            } else {
                subtask.setId(increaseCount());
                subtasks.put(subtask.getId(), subtask);
                taskSet.add(subtask);
                Epic epic = subtask.getParent();
                epic.getChildSubtasks().add(subtask);
                epic.checkStatus();
                epic.setEpicDuration();
                return subtask;
            }
        } catch (TaskValidationException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Task addTask(Task task) {
        try {
            if (!TimeValidator.validateTime(task, getPrioritizedTasks())) {
                throw new TaskValidationException("Заданное время задачи пересекается по времени с другой задачей");
            } else {
                if (!task.getClass().equals(Task.class)) {
                    throw new IllegalArgumentException("В метод передан неверный тип объекта");
                } else {
                    task.setId(increaseCount());
                    tasks.put(task.getId(), task);
                    taskSet.add(task);
                    return task;
                }
            }
        } catch (TaskValidationException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(increaseCount());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            taskSet.remove(tasks.get(id));
            tasks.remove(id);
        } else if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getParent();
            for (int i = 0; i < epic.getChildSubtasks().size(); i++) {
                if (epic.getChildSubtasks().get(i).getId() == id) {
                    epic.getChildSubtasks().remove(i);
                    epic.checkStatus();
                    epic.setEpicDuration();
                    break;
                }
            }
            historyManager.remove(id);
            taskSet.remove(subtasks.get(id));
            subtasks.remove(id);
            epic.checkStatus();
            epic.setEpicDuration();
        } else if (epics.containsKey(id)){
            Epic epic = epics.get(id);
            for (Subtask subtask : epic.getChildSubtasks()) {
                historyManager.remove(subtask.getId());
                taskSet.remove(subtask);
                subtasks.remove(subtask.getId());
            }
            epic.getChildSubtasks().clear();
            historyManager.remove(id);
            epics.remove(id);
        } else {
            throw new IllegalArgumentException("Нет такого id");
        }
    }

    @Override
    public List<Task> getAllTypeTasksList(TaskType nameTaskTypeSet) {
        List<Task> tasksName = new ArrayList<>();
        if (nameTaskTypeSet.equals(TaskType.TASK)) {
            for (int key : tasks.keySet()) {
                tasksName.add(tasks.get(key));
            }
            return tasksName;
        } else if (nameTaskTypeSet.equals(TaskType.SUBTASK)) {
            for (int key : subtasks.keySet()) {
                tasksName.add(subtasks.get(key));
            }
            return tasksName;
        } else {
            for (int key : epics.keySet()) {
                tasksName.add(epics.get(key));
            }
            return tasksName;
        }
    }

    @Override
    public void deleteAllTasksFromSet(TaskType nameTasksSet) {
        if (nameTasksSet.equals(TaskType.TASK)) {
            for (Task task : tasks.values()) {
                historyManager.remove(task.getId());
                taskSet.remove(task);
            }
            tasks.clear();
        } else if (nameTasksSet.equals(TaskType.SUBTASK)) {
            for (Task subtask : subtasks.values()) {
                historyManager.remove(subtask.getId());
                taskSet.remove(subtask);
            }
            subtasks.clear();
            for (int key : epics.keySet()) {
                epics.get(key);
            }
        } else {
            for (Task epic : epics.values()) {
                historyManager.remove(epic.getId());
            }
            for (Task subtask : subtasks.values()) {
                historyManager.remove(subtask.getId());
                taskSet.remove(subtask);
            }
            epics.clear();
            subtasks.clear();
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            historyManager.add(epics.get(id));
            return epics.get(id);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        epic.checkStatus();
        epic.setEpicDuration();
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        subtask.getParent().checkStatus();
        subtask.getParent().setEpicDuration();
    }

    @Override
    public ArrayList<String> getTasksFromEpicTask(Epic epic) {
        ArrayList<String> subtasksName = new ArrayList<>();
        for (int i = 0; i < epic.getChildSubtasks().size(); i++) {
            subtasksName.add(epic.getChildSubtasks().get(i).getName());
        }
        return subtasksName;
    }

    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(taskSet);
    }

    private int increaseCount() {
        return ++count;
    }
}
