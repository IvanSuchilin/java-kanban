package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileBackedTasksManager extends InMemoryTaskManager {
    String path;

    public FileBackedTasksManager(String path) {
        this.path = path;
    }

    public static void main(String[] args) {

        FileBackedTasksManager fileBacked = new FileBackedTasksManager("backUp.csv");
        Task newTask1 = new Task("first", "check", Task.Status.NEW);   //создать-добавить задачу
        fileBacked.addTask(newTask1);
        Task newTask2 = new Task("second", "check1", Task.Status.IN_PROGRESS);
        fileBacked.addTask(newTask2);
        Epic epic1 = new Epic("epicName1", "try for check1",       //эпик + 2 подзадачи
                Task.Status.DONE);
        fileBacked.addEpic(epic1);
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                Task.Status.DONE, epic1);
        fileBacked.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                Task.Status.NEW, epic1);
        fileBacked.addSubtask(subtask2);
        fileBacked.getTaskById(1);
        fileBacked.getTaskById(2);
        fileBacked.getTaskById(1);
        fileBacked.getTaskById(4);
        //проверка
        System.out.println(fileBacked.getTasks());
        System.out.println(fileBacked.getSubtasks());
        System.out.println(fileBacked.getEpics());
        System.out.println(fileBacked.getHistory());
        File doc = new File("backUp.csv");
        FileBackedTasksManager fileBacked2 = loadFromFile(doc);
        System.out.println(fileBacked2.getTasks());
        System.out.println(fileBacked2.getSubtasks());
        System.out.println(fileBacked2.getEpics());
        System.out.println(fileBacked2.getHistory());
    }

    public void save() {
        try {
            File backUp = new File(path);
            PrintWriter pw = new PrintWriter(backUp);
            pw.println("id,type,name,status,description,epic");
            for (Task task : getTasks().values()) {
                pw.println(taskToString(task));
            }
            for (Epic epic : getEpics().values()) {
                pw.println(taskToString(epic));
            }
            for (Subtask subtask : getSubtasks().values()) {
                pw.println(taskToString(subtask));
            }
            pw.println();
            pw.println(historyToString(getHistoryManager()));
            pw.close();
        } catch (IOException exception) {
            throw new ManagerSaveException("Что-то пошло не так с сохранением в backUp - файл");
        }
    }

    public Task addBackedTask(String[] split) {
        Task newTask = new Task(split[2], split[4], Task.Status.valueOf(split[3]));
        getTasks().put(Integer.parseInt(split[0]), newTask);
        newTask.setId(Integer.parseInt(split[0]));
        return newTask;
    }

    public Task addBackedEpic(String[] split) {
        Epic newEpic = new Epic(split[2], split[4], Task.Status.valueOf(split[3]));
        getEpics().put(Integer.parseInt(split[0]), newEpic);
        newEpic.setId(Integer.parseInt(split[0]));
        return newEpic;
    }

    public Task addBackedSubtask(String[] split) {
        int parentsId = Integer.parseInt(split[5]);
        Epic parent = getEpics().get(parentsId);
        Subtask newSubTask = new Subtask(split[2], split[4], Task.Status.valueOf(split[3]), parent);
        getSubtasks().put(Integer.parseInt(split[0]), newSubTask);
        newSubTask.setId(Integer.parseInt(split[0]));
        parent.getChildSubtasks().add(newSubTask);
        parent.checkStatus();
        return newSubTask;
    }

    public String taskToString(Task task) {
        return String.join(",", Integer.toString(task.getId()), TaskType.TASK.toString(), task.getName(),
                task.getStatus().toString(), task.getDescription());
    }

    public String taskToString(Subtask subtask) {
        return String.join(",", Integer.toString(subtask.getId()), TaskType.SUBTASK.toString(), subtask.getName(),
                subtask.getStatus().toString(), subtask.getDescription(), Integer.toString(subtask.getParent().getId()));
    }

    public String taskToString(Epic task) {
        return String.join(",", Integer.toString(task.getId()), TaskType.EPIC.toString(), task.getName(),
                task.getStatus().toString(), task.getDescription());
    }

    public Task taskFromString(String value) {
        String[] split = value.split(",");
        if (split[1].equals(TaskType.TASK.toString())) {
            return addBackedTask(split);
        } else if (split[1].equals(TaskType.EPIC.toString())) {
            return addBackedEpic(split);
        } else {
            return addBackedSubtask(split);
        }
    }

    public static String historyToString(HistoryManager manager) {
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

    public static List<Integer> historyFromString(String value) {
        List<Integer> splitList = new ArrayList<>();
        String[] split = value.split(",");
        for (String splitItem : split) {
            splitList.add((Integer.parseInt(splitItem)));
        }
        return splitList;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(file.getPath());
        List<Integer> splitList;
        List<String> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                records.add(scanner.nextLine());
            }
            for (int i = 1; i < records.size(); i++) {
                if (!records.get(i).isEmpty()) {
                    fileBacked.taskFromString(records.get(i));
                } else {
                    splitList = historyFromString(records.get(i + 1));
                    for (Integer id : splitList)
                        fileBacked.getTaskById(id);
                    break;
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
        save();
        super.deleteTaskById(id);
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
}
