import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    static int count = 0;

    private int increaseCount(){
        count++;
        return count;
    }

    public Subtask addSubtask(Subtask subtask) {
        subtask.setId(increaseCount());
        subtasks.put(subtask.getId(), subtask);
        subtask.getParent().childSubtasks.add(subtask);
        subtask.getParent().checkStatus();
        return subtask;
    }

    public Task addTask(Task task) {
        task.setId(increaseCount());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic addEpicTask(Epic epic) {
        epic.setId(increaseCount());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getParent();
            for (int i = 0; i < epic.childSubtasks.size(); i++) {
                if (epic.childSubtasks.get(i).getId() == id) {
                    epic.childSubtasks.remove(i);
                    epic.checkStatus();
                    break;
                }
            }
            subtasks.remove(id);
            epic.checkStatus();
        } else {
            Epic epic = epics.get(id);
            for (Subtask subtask : epic.childSubtasks) {
                subtasks.remove(subtask.getId());
            }
            epic.childSubtasks.clear();
            epics.remove(id);
        }
    }

    public ArrayList<String> getAllTypeTasksList(Tasks nameTasksSet) {                    //списки задач каждого вида
        ArrayList<String> tasksName = new ArrayList<>();
        if (nameTasksSet.equals(Tasks.TASK)) {
            for (int key : tasks.keySet()) {
                tasksName.add(tasks.get(key).toString());
            }
            return tasksName;
        } else if (nameTasksSet.equals(Tasks.SUBTASK)) {
            for (int key : subtasks.keySet()) {
                tasksName.add(subtasks.get(key).toString());
            }
            return tasksName;
        } else {
            for (int key : epics.keySet()) {
                tasksName.add(epics.get(key).toString());
            }
            return tasksName;
        }
    }

    public void deleteAllTasksFromSet(String nameTasksSet) {
        if (nameTasksSet.equals("tasks")) {
            tasks.clear();
        } else if (nameTasksSet.equals("subtasks")) {
            subtasks.clear();
            for (int key : epics.keySet()) {
                epics.get(key);
            }
        } else {
            epics.clear();
            subtasks.clear();
        }
    }

    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else {
            return epics.get(id);
        }
    }

    public void updateEpicTask(Epic epic) {
        epics.put(epic.getId(), epic);
        epic.checkStatus();
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        subtask.getParent().checkStatus();
    }

    public ArrayList<String> getTasksFromEpicTask(Epic epic) {
        ArrayList<String> subtasksName = new ArrayList<>();
        for (int i = 0; i < epic.childSubtasks.size(); i++) {
            subtasksName.add(epic.childSubtasks.get(i).getName());
        }
        return subtasksName;
    }
}
enum Tasks{
    TASK,
    SUBTASK,
    EPIC
}
