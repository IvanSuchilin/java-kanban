package test.manager;

import main.server.KVServer;
import main.manager.HTTPTaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static main.task.Task.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {

    private KVServer kvServer;

    @BeforeEach
    void setUp() throws Exception {

        kvServer = new KVServer();
        kvServer.start();
        taskManager = new HTTPTaskManager("http://localhost:8078");

        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1fb = new Epic("epic#11", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1fb);
        Epic epic2Fb = new Epic("epic#222", "epicForCheck", NEW);
        taskManager.addEpic(epic2Fb);
        Subtask subtask1Fb = new Subtask("subtask#11", "subtaskForCheck",
                DONE, "12.02.2022 22:30", 60, epic2Fb.getId());
        taskManager.addSubtask(subtask1Fb);
        Subtask subtask3Fb = new Subtask("subtask#11", "subtaskForCheck",
                IN_PROGRESS, "27.02.2022 22:30", 60, epic1fb.getId());
        taskManager.addSubtask(subtask3Fb);
        Subtask subtask2Fb = new Subtask("subtask#22", "subtaskForCheck",
                NEW, "06.02.2022 15:22", 360, epic1fb.getId());
        taskManager.addSubtask(subtask2Fb);
        Task task1Fb = new Task("task#11", "taskForCheck", Task.Status.NEW,
                "11.02.2022 05:00", 60);
        taskManager.addTask(task1Fb);
        Task task2FB = new Task("task#22", "taskForCheck", Task.Status.DONE,
                "15.02.2022 07:00", 60);
        taskManager.addTask(task2FB);
        taskManager.getTaskById(task2FB.getId());
        taskManager.getTaskById(subtask1Fb.getId());
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
    }

    @Disabled
    @Test
    void save() {
    }

    @Test
    void loadFromKVS() throws Exception {
        Task task1Fbs = new Task("task#11", "taskForCheck", Task.Status.NEW,
                "03.02.2022 05:00", 60);
        taskManager.addTask(task1Fbs);
        Task task2Fbs = new Task("task#222", "taskForCheck", Task.Status.DONE,
                "01.02.2022 07:00", 60);
        taskManager.addTask(task2Fbs);
        taskManager.getTaskById(1);
        HTTPTaskManager httpTaskManager = HTTPTaskManager.loadFromKVS();
        assertEquals(4, httpTaskManager.getTasks().size());
        assertEquals(1, httpTaskManager.getHistory().get(2).getId());
    }

    @Test
    void loadFromKVSEmptyDataTest() throws Exception {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        HTTPTaskManager httpTaskManager = HTTPTaskManager.loadFromKVS();
        assertEquals(0, httpTaskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, httpTaskManager.getSubtasks().size(), "Неверное количество задач.");
        assertEquals(0, httpTaskManager.getEpics().size(), "Неверное количество задач.");
    }

    @Test
    void loadFromKVSEmptyHistoryTest() throws Exception {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Task task1 = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);
        taskManager.addTask(task1);
        Task task2 = new Task("task#2", "taskForCheck", Task.Status.DONE,
                "24.02.2022 07:00", 60);
        taskManager.addTask(task2);
        HTTPTaskManager httpTaskManager = HTTPTaskManager.loadFromKVS();
        assertEquals(0, httpTaskManager.getHistory().size(), "Неверное количество задач.");
    }

    @Test
    void loadFromKVSWithEmptyChildListTest() throws Exception {
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        HTTPTaskManager httpTaskManager = HTTPTaskManager.loadFromKVS();
        assertEquals(2,
                httpTaskManager.getPrioritizedTasks().size(), "Неверное количество задач");
    }
}
