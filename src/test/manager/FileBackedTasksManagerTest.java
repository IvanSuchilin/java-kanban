package test.manager;

import main.manager.FileBackedTasksManager;
import main.manager.exception.ManagerLoadException;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;

import static main.task.Task.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    static final String FILE_PATH = "backUp.csv";

    @BeforeEach
    void setUp() {
        File doc = new File(FILE_PATH);
        taskManager = new FileBackedTasksManager(FILE_PATH);
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
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
        taskManager.getTaskById(task2FB.getId());
        taskManager.getTaskById(subtask1Fb.getId());
        taskManager.getTaskById(epic2Fb.getId());
        taskManager.getTaskById(subtask2Fb.getId());
        taskManager.getTaskById(epic1fb.getId());
        taskManager.getTaskById(task1Fb.getId());
        taskManager.getTaskById(epic1fb.getId());
        taskManager.getTaskById(subtask3Fb.getId());
    }

    @AfterEach
    void setUpTmt() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
    }

    @Test
    void getPath() {
        String path = taskManager.getPath();
        assertEquals("backUp.csv", path, "Неверное расположение файла.");
    }

    @Test
    void taskFromStringDurationTest() {
        String taskString = "666,TASK,task#111,NEW,taskForCheck,01.02.2022 05:00,60";
        Task parseTask = taskManager.taskFromString(taskString);
        assertEquals(Duration.ofMinutes(60), parseTask.getDuration(), "Длительность не соответствует");
    }

    @Test
    void taskFromStringIdTest() {
        String taskString = "666,TASK,task#111,NEW,taskForCheck,01.02.2022 05:00,60";
        Task parseTask = taskManager.taskFromString(taskString);
        assertEquals(666, parseTask.getId(), "Id не соответствует");
    }

    @Test
    void taskFromStringStatusTest() {
        String taskString = "666,TASK,task#111,NEW,taskForCheck,01.02.2022 05:00,60";
        Task parseTask = taskManager.taskFromString(taskString);
        assertEquals(NEW, parseTask.getStatus(), "Статус не соответствует");
    }

    @Test
    void loadFromFileTasksTest() {
        File doc = new File(FILE_PATH);
        FileBackedTasksManager newFileManager = FileBackedTasksManager.loadFromFile(doc);
        assertEquals(newFileManager.getTasks().size(), taskManager.getTasks().size(), "Неверное количество задач.");
    }

    @Test
    void loadFromFileSubtasksTest() {
        File doc = new File(FILE_PATH);
        FileBackedTasksManager newFileManager = FileBackedTasksManager.loadFromFile(doc);
        assertEquals(newFileManager.getSubtasks().size(), taskManager.getSubtasks().size(), "Неверное количество задач.");
    }

    @Test
    void loadFromFileEpicsTest() {
        File doc = new File(FILE_PATH);
        FileBackedTasksManager newFileManager = FileBackedTasksManager.loadFromFile(doc);
        assertEquals(newFileManager.getEpics().size(), taskManager.getEpics().size(), "Неверное количество задач.");
    }

    @Test
    void loadFromFileHistoryTest() {
        File doc = new File(FILE_PATH);
        FileBackedTasksManager newFileManager = FileBackedTasksManager.loadFromFile(doc);
        assertEquals(newFileManager.getHistory().size(), taskManager.getHistory().size(), "Неверное количество задач.");
    }

    @Test
    void loadFromFileEmptyDataTest() {
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
    void loadFromFileEmptyAllStringsTest() {
        try {
            taskManager.deleteAllTasksFromSet(TaskType.TASK);
            taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
            taskManager.deleteAllTasksFromSet(TaskType.EPIC);
            File doc = new File("empty.csv");
            FileBackedTasksManager newFileManager = FileBackedTasksManager.loadFromFile(doc);
        } catch (ManagerLoadException e) {
            assertEquals("Файл некорректно был сохранен, восстановление невозможно", e.getMessage());
        }
    }

    @Test
    void loadFromFileEmptyHistoryTest() {
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
        assertEquals(0, newFileManager.getHistory().size(), "Неверное количество задач.");
    }

    @Test
    void loadFromFileWithEmptyChildListTest() {
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        File doc = new File(FILE_PATH);
        FileBackedTasksManager newFileManager = FileBackedTasksManager.loadFromFile(doc);
        int deltaSize = taskManager.getPrioritizedTasks().size() - taskManager.getSubtasks().size();
        assertEquals(taskManager.getPrioritizedTasks().size() - deltaSize,
                newFileManager.getPrioritizedTasks().size(), "Неверное время эпика после удаления подзадач");
    }
}