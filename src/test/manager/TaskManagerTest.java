package test.manager;

import main.manager.TaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static main.task.Task.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest <T extends TaskManager> {

    protected  T taskManager;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getHistory() {
    }

    @Test
    void getTaskSet() {
    }

    @Test
    void getTasks() {
    }

    @Test
    void getEpics() {
    }

    @Test
    void getSubtasks() {
    }

    @Test
    void addSubtask() {
        Epic epic1 = new Epic("epic#1", "epicForCheck", Task.Status.IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                Task.Status.DONE, "22.02.2022 22:30", 60, epic1);
        Task taskWithId = taskManager.addSubtask(subtask1);
        final int subtaskId = taskWithId.getId();
        final Task savedTask = taskManager.getSubtasks().get(subtaskId);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(subtask1, savedTask, "Задачи не совпадают");

        final List<Task> tasks = taskManager.getAllTypeTasksList(TaskType.SUBTASK);

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addTask() {
        Task task = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);;
        Task taskWithId = taskManager.addTask(task);
        final int taskId = taskWithId.getId();
        final Task savedTask = taskManager.getTasks().get(taskId);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");

        final List<Task> tasks = taskManager.getAllTypeTasksList(TaskType.TASK);

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }


    @Test
    void addEpic() {
        Epic epic1 = new Epic("epic#1", "epicForCheck", Task.Status.IN_PROGRESS);
        Task taskWithId = taskManager.addEpic(epic1);
        final int taskId = taskWithId.getId();
        final Task savedTask = taskManager.getEpics().get(taskId);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(epic1, savedTask, "Задачи не совпадают");

        final List<Task> tasks = taskManager.getAllTypeTasksList(TaskType.EPIC);

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epic1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void deleteTaskByIdEpic() {
        Epic epic1 = new Epic("epic#1", "epicForCheck", Task.Status.IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                Task.Status.DONE, "22.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask1);
        taskManager.deleteTaskById(1);

        assertEquals(0,taskManager.getEpics().size(), "Эпик не удален");
        assertEquals(0,taskManager.getSubtasks().size(), "Эпик с подзадачей не удален");
    }

    @Test
    void deleteTaskByIdTask() {
        Task task = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);;
        taskManager.addTask(task);
        assertEquals(1,taskManager.getTasks().size(), "Задача не добавлена");
        taskManager.deleteTaskById(1);

        assertEquals(0,taskManager.getTasks().size(), "Задача не удалена");
    }

    @Test
    void deleteTaskByIdSubtask() {
        Epic epic1 = new Epic("epic#1", "epicForCheck", Task.Status.IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                Task.Status.DONE, "22.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask1);
        taskManager.deleteTaskById(2);
        int sizeChild = taskManager.getEpics().get(1).getChildSubtasks().size();

        assertEquals(0,sizeChild, "Список подзадач не пуст");
        assertEquals(0,taskManager.getSubtasks().size(), "Подзадача не удалена");
    }

    @Test
    void deleteTaskByWrongId() {
        try {
            Epic epic1 = new Epic("epic#1", "epicForCheck", Task.Status.IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                Task.Status.DONE, "22.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask1);
        taskManager.deleteTaskById(3);
    } catch (IllegalArgumentException e){
            assertNotEquals("", e.getMessage());
        }
    }

    @Test
    void getAllTypeTasksList() {
    }

    @Test
    void deleteAllTasksFromSet() {
    }

    @Test
    void getTaskById() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateSubtask() {
    }

    @Test
    void getTasksFromEpicTask() {
    }

    @Test
    void getPrioritizedTasks() {
    }
}