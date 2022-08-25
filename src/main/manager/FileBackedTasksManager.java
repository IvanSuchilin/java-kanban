package main.manager;

import main.manager.exception.ManagerLoadException;
import main.manager.exception.ManagerSaveException;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String path;
    static final String FILE_PATH = "backUp.csv";

    public FileBackedTasksManager() {
        path = FILE_PATH;
    }

    public FileBackedTasksManager(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public Task taskFromString(String value) {
        String[] split = value.split(",");
        return addBackedTask(split);
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(file.getPath());
        List<Integer> splitList;
        List<String> records = new ArrayList<>(1);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                records.add(scanner.nextLine());
            }
            if (records.get(0).length() == 0) {
                throw new ManagerLoadException("Файл некорректно был сохранен, восстановление невозможно");
            }
            if (records.get(1).length() == 0) {
                return fileBacked;
            }
            for (int i = 1; i < records.size(); i++) {
                if (!records.get(i).isEmpty()) {
                    fileBacked.taskFromString(records.get(i));
                } else {
                    if (records.get(i + 1).length() != 0) {
                        splitList = historyFromString(records.get(i + 1));
                        for (Integer id : splitList)
                            fileBacked.getTaskById(id);
                        break;
                    } else {
                        splitList = new ArrayList<>();
                        return fileBacked;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileBacked;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        Subtask addSubtask = super.addSubtask(subtask);
        save();
        return addSubtask;
    }

    @Override
    public Task addTask(Task task) {
        Task addTask = super.addTask(task);
        save();
        return addTask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic addEpic = super.addEpic(epic);
        save();
        return addEpic;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteAllTasksFromSet(TaskType nameTasksSet) {
        super.deleteAllTasksFromSet(nameTasksSet);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        super.getTaskById(id);
        save();
        return super.getTaskById(id);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    protected void save() {
        try {
            File backUp = new File(path);
            PrintWriter pw = new PrintWriter(backUp);
            pw.println("id,type,name,status,description,startTime,duration,epic");
            List<Task> allTaskSetValues = new ArrayList<>();
            allTaskSetValues.addAll(new ArrayList<>(getTasks().values()));
            allTaskSetValues.addAll(new ArrayList<>(getEpics().values()));
            allTaskSetValues.addAll(new ArrayList<>(getSubtasks().values()));
            for (Task task : allTaskSetValues) {
                pw.println(task.toCsvString());
            }
            pw.println();
            pw.println(historyToString(getHistoryManager()));
            pw.close();
        } catch (IOException exception) {
            throw new ManagerSaveException("Что-то пошло не так с сохранением в backUp - файл");
        }
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder historyString = new StringBuilder();
        List<Task> historyToString = manager.getHistory();
        if (!historyToString.isEmpty()) {
            for (Task taskFromHistory : historyToString) {
                historyString.append(taskFromHistory.getId()).append(",");
            }
            return historyString.substring(0, historyString.length() - 1);
        } else {
            return "";
        }
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> splitList = new ArrayList<>();
        String[] split = value.split(",");
        for (String splitItem : split) {
            splitList.add((Integer.parseInt(splitItem)));
        }
        return splitList;
    }

    private Task addBackedTask(String[] split) {
        switch (split[1]) {
            case "TASK":
                Task newTask = new Task(split[2], split[4], Task.Status.valueOf(split[3]), split[5], Long.parseLong(split[6]));
                getTasks().put(Integer.parseInt(split[0]), newTask);
                newTask.setId(Integer.parseInt(split[0]));
                return newTask;
            case "SUBTASK":
                int parentsId = Integer.parseInt(split[7]);
                Epic parent = getEpics().get(parentsId);
                Subtask newSubTask = new Subtask(split[2], split[4], Task.Status.valueOf(split[3]), split[5], Long.parseLong(split[6]), parentsId);
                getSubtasks().put(Integer.parseInt(split[0]), newSubTask);
                newSubTask.setId(Integer.parseInt(split[0]));
                parent.getChildSubtasks().add(newSubTask);
                parent.checkStatus();
                parent.setEpicDuration();
                return newSubTask;
            default:
                Epic newEpic = new Epic(split[2], split[4], Task.Status.valueOf(split[3]));
                getEpics().put(Integer.parseInt(split[0]), newEpic);
                newEpic.setId(Integer.parseInt(split[0]));
                return newEpic;
        }
    }
}