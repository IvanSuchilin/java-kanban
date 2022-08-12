package test.manager;

import main.manager.FileBackedTasksManager;
import main.manager.InMemoryTaskManager;
import main.manager.exception.ManagerLoadException;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static main.task.Task.Status.*;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest <FileBackedTasksManager> {

    static final String FILE_PATH = "backUp.csv";

    @BeforeEach
    void setUp() {

        File doc = new File(FILE_PATH);
        taskManager = new FileBackedTasksManager(FILE_PATH);
        Epic epic1fb = new Epic("epic#11", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1fb);
        Epic epic2Fb = new Epic("epic#222", "epicForCheck", NEW);
        taskManager.addEpic(epic2Fb);
        Subtask subtask1Fb = new Subtask("subtask#11", "subtaskForCheck",
                DONE, "12.02.2022 22:30", 60, epic2Fb);
        taskManager.addSubtask(subtask1Fb);
        Subtask subtask3Fb = new Subtask("subtask#11", "subtaskForCheck",
                IN_PROGRESS, "27.02.2022 22:30", 60, epic1fb);
        taskManager.addSubtask(subtask3Fb);
        Subtask subtask2Fb = new Subtask("subtask#22", "subtaskForCheck",
                NEW, "06.02.2022 15:22", 360, epic1fb);
        taskManager.addSubtask(subtask2Fb);
        Task task1Fb = new Task("task#11", "taskForCheck", Task.Status.NEW,
                "11.02.2022 05:00", 60);
        taskManager.addTask(task1Fb);
        Task task2FB = new Task("task#22", "taskForCheck", Task.Status.DONE,
                "15.02.2022 07:00", 60);
        taskManager.addTask(task2FB);
        taskManager.getTaskById(7);
        taskManager.getTaskById(3);
        taskManager.getTaskById(2);
        taskManager.getTaskById(5);
        taskManager.getTaskById(1);
        taskManager.getTaskById(6);
        taskManager.getTaskById(1);
        taskManager.getTaskById(4);
    }

    @Test
    void getPath() {
        String path = taskManager.getPath();
        assertEquals("backUp.csv", path, "Неверное расположение файла.");
    }

    @Test
    void addBackedTask() {
    }

    @Test
    void taskFromString() {
    }

    @Test
    void loadFromFile() {
        File doc = new File(FILE_PATH);
        FileBackedTasksManager newFileManager = FileBackedTasksManager.loadFromFile(doc);
        assertEquals(newFileManager.getTasks().size(), taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(newFileManager.getSubtasks().size(), taskManager.getSubtasks().size(), "Неверное количество задач.");
        assertEquals(newFileManager.getEpics().size(), taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(newFileManager.getHistory().size(), taskManager.getHistory().size(), "Неверное количество задач.");
    }

    @Test
    void loadFromFileEmptyData() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        assertEquals(0, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getSubtasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getHistory().size(), "Неверное количество задач.");
        File doc = new File(FILE_PATH);
        FileBackedTasksManager newFileManager = FileBackedTasksManager.loadFromFile(doc);
        assertEquals(newFileManager.getTasks().size(), taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(newFileManager.getSubtasks().size(), taskManager.getSubtasks().size(), "Неверное количество задач.");
        assertEquals(newFileManager.getEpics().size(), taskManager.getEpics().size(), "Неверное количество задач.");
    }

    @Test
    void loadFromFileEmptyAllStrings() {
       try {
           taskManager.deleteAllTasksFromSet(TaskType.TASK);
           taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
           taskManager.deleteAllTasksFromSet(TaskType.EPIC);
           assertEquals(0, taskManager.getTasks().size(), "Неверное количество задач.");
           assertEquals(0, taskManager.getSubtasks().size(), "Неверное количество задач.");
           assertEquals(0, taskManager.getEpics().size(), "Неверное количество задач.");
           assertEquals(0, taskManager.getHistory().size(), "Неверное количество задач.");
           File doc = new File("empty.csv");
           FileBackedTasksManager newFileManager = FileBackedTasksManager.loadFromFile(doc);
       }catch (ManagerLoadException e) {
           assertEquals("Файл некорректно был сохранен, восстановление невозможно", e.getMessage());
       }
    }

    @Test
    void loadFromFileEmptyHistory() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Task task1 = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);
        taskManager.addTask(task1);
        Task task2 = new Task("task#2", "taskForCheck", Task.Status.DONE,
                "24.02.2022 07:00", 60);
        taskManager.addTask(task2);
        File doc = new File(FILE_PATH);
        FileBackedTasksManager newFileManager = FileBackedTasksManager.loadFromFile(doc);
        assertEquals(newFileManager.getTasks().size(), taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(newFileManager.getSubtasks().size(), taskManager.getSubtasks().size(), "Неверное количество задач.");
        assertEquals(newFileManager.getEpics().size(), taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(0, newFileManager.getHistory().size(), "Неверное количество задач.");
    }
}