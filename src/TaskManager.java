import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Subtask addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        subtask.getParent().childSubtasks.add(subtask);
        subtask.getParent().checkStatus();
        return subtask;
    }

    public Task addTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public EpicTask addEpicTask(EpicTask epicTask) {
        epicTasks.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (subtasks.containsKey(id)) {
            EpicTask epic = subtasks.get(id).getParent();
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
            EpicTask epic = epicTasks.get(id);
            for (Subtask subtask : epic.childSubtasks) {
                subtasks.remove(subtask.getId());
            }
            epic.childSubtasks.clear();
            epicTasks.remove(id);
        }
    }

    public ArrayList<String> getTasksList() {
        ArrayList<String> tasksName = new ArrayList<>();
        for (int key : tasks.keySet()) {
            tasksName.add(tasks.get(key).name);
        }
        return tasksName;
    }

    public ArrayList<String> getSubtasksList() {
        ArrayList<String> tasksName = new ArrayList<>();
        for (int key : subtasks.keySet()) {
            tasksName.add(subtasks.get(key).name);
        }
        return tasksName;
    }

    public ArrayList<String> getEpicTasksList() {
        ArrayList<String> tasksName = new ArrayList<>();
        for (int key : epicTasks.keySet()) {
            tasksName.add(epicTasks.get(key).name);
        }
        return tasksName;
    }

    public void deleteAllTasksFromSet(String nameTasksSet) {
        if (nameTasksSet.equals("tasks")) {
            tasks.clear();
        } else if (nameTasksSet.equals("subtasks")) {
            subtasks.clear();
            for (int key : epicTasks.keySet()) {
                epicTasks.get(key);
            }
        } else {
            epicTasks.clear();
            subtasks.clear();
        }
    }

    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else {
            return epicTasks.get(id);
        }
    }

    public void updateEpicTask(EpicTask epicTask) {
        epicTasks.put(epicTask.getId(), epicTask);
        epicTask.checkStatus();
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        subtask.getParent().checkStatus();
    }

    public ArrayList<String> getTasksFromEpicTask(EpicTask epicTask) {
        ArrayList<String> subtasksName = new ArrayList<>();
        for (int i = 0; i < epicTask.childSubtasks.size(); i++) {
            subtasksName.add(epicTask.childSubtasks.get(i).name);
        }
        return subtasksName;
    }
}
