package test.manager;

import main.manager.InMemoryTaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static main.task.Task.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void getPrioritizedTasks() {
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("epic#2", "epicForCheck", NEW);
        taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic2);
        taskManager.addSubtask(subtask1);
        Subtask subtask3 = new Subtask("subtask#1", "subtaskForCheck",
                IN_PROGRESS, "17.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask3);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                NEW, "16.02.2022 15:22", 360, epic1);
        taskManager.addSubtask(subtask2);
        Task task1 = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);
        taskManager.addTask(task1);
        Task task2 = new Task("task#2", "taskForCheck", Task.Status.DONE,
                "24.02.2022 07:00", 60);
        taskManager.addTask(task2);
        ArrayList<Task> priorizitedTask = taskManager.getPrioritizedTasks();
        assertNotNull(priorizitedTask, "Задачи не возвращаются.");
        assertEquals(5, priorizitedTask.size(), "Неверное количество задач.");
        assertEquals(subtask2, priorizitedTask.get(0), "Задачи не совпадают.");
        assertEquals(task2, priorizitedTask.get(4), "Задачи не совпадают.");
        taskManager.deleteTaskById(5);
        ArrayList<Task> priorizitedTaskSec = taskManager.getPrioritizedTasks();
        assertEquals(subtask3, priorizitedTaskSec.get(0), "Задачи не совпадают.");
        taskManager.deleteTaskById(1);
        taskManager.deleteTaskById(2);
        assertEquals(2, taskManager.getPrioritizedTasks().size());
        assertEquals(task1, taskManager.getPrioritizedTasks().get(0), "Задачи не совпадают.");
    }
}