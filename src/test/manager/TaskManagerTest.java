package test.manager;

import main.manager.TaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static main.task.Task.Status.*;
import static org.junit.jupiter.api.Assertions.*;


public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getHistory() throws CloneNotSupportedException {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask1);
        Subtask subtask3 = new Subtask("subtask#1", "subtaskForCheck",
                IN_PROGRESS, "17.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask3);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                NEW, "16.02.2022 15:22", 360, epic1);
        taskManager.addSubtask(subtask2);
        taskManager.getTaskById(epic1.getId());
        taskManager.getTaskById(subtask1.getId());
        taskManager.getTaskById(subtask3.getId());
        taskManager.getTaskById(subtask2.getId());
        List<Task> history = taskManager.getHistory();
        assertNotNull(history, "Задачи не возвращаются.");
        assertEquals(4, history.size(), "Неверное количество задач.");
        taskManager.deleteTaskById(epic1.getId());
        List<Task> historyAfterDelete = taskManager.getHistory();
        assertEquals(0, historyAfterDelete.size(), "Неверное количество задач.");
    }

    @Test
    void getTaskSet() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask1);
        Subtask subtask3 = new Subtask("subtask#1", "subtaskForCheck",
                IN_PROGRESS, "17.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask3);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                NEW, "16.02.2022 15:22", 360, epic1);
        taskManager.addSubtask(subtask2);
        TreeSet<Task> tasksForValidation = taskManager.getTaskSet();
        assertNotNull(tasksForValidation, "Задачи не возвращаются.");
        assertEquals(3, tasksForValidation.size(), "Неверное количество задач.");
        assertEquals(subtask2, tasksForValidation.first(), "Неверная задача.");
        taskManager.deleteTaskById(subtask2.getId());
        assertEquals(subtask3, tasksForValidation.first(), "Неверная задача.");
    }

    @Test
    void getTasks() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Task task1 = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);
        taskManager.addTask(task1);
        Task task2 = new Task("task#2", "taskForCheck", Task.Status.DONE,
                "24.02.2022 07:00", 60);
        taskManager.addTask(task2);
        Map<Integer, Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(task1.getId()), "Задачи не совпадают.");
        assertEquals(task2, tasks.get(task2.getId()), "Задачи не совпадают.");
    }

    @Test
    void getEpics() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("epic#2", "epicForCheck", NEW);
        taskManager.addEpic(epic2);
        Map<Integer, Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество задач.");
        assertEquals(epic1, epics.get(epic1.getId()), "Задачи не совпадают.");
        assertEquals(epic2, epics.get(epic2.getId()), "Задачи не совпадают.");
        assertEquals(NEW, epic1.getStatus(), "Статус эпика  неверный");
        assertEquals(NEW, epic2.getStatus(), "Статус эпика  неверный");
    }

    @Test
    void getSubtasks() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask1);
        Subtask subtask3 = new Subtask("subtask#1", "subtaskForCheck",
                IN_PROGRESS, "17.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask3);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                NEW, "16.02.2022 15:22", 360, epic1);
        taskManager.addSubtask(subtask2);
        Map<Integer, Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask3, subtasks.get(subtask3.getId()), "Задачи не совпадают.");
        assertEquals(subtask2, subtasks.get(subtask2.getId()), "Задачи не совпадают.");
    }

    @Test
    void addSubtask() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic1);
        Task taskWithId = taskManager.addSubtask(subtask1);
        final int subtaskId = taskWithId.getId();
        final Task savedTask = taskManager.getSubtasks().get(subtaskId);
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(subtask1, savedTask, "Задачи не совпадают");
        final List<Task> tasks = taskManager.getAllTypeTasksList(TaskType.SUBTASK);
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, tasks.get(0), "Задачи не совпадают.");
        assertEquals(DONE, epic1.getStatus(), "Статус эпика при добавлении подзадачи неверный");
        assertEquals(epic1, subtask1.getParent(), "Родитель подзадачи не совпадает");
    }

    @Test
    void addTask() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Task task = new Task("task#1", "taskForCheck", NEW,
                "24.02.2022 05:00", 60);
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
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
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
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask1);
        taskManager.deleteTaskById(epic1.getId());
        assertEquals(0, taskManager.getEpics().size(), "Эпик не удален");
        assertEquals(0, taskManager.getSubtasks().size(), "Эпик с подзадачей не удален");
    }

    @Test
    void deleteTaskByIdTask() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Task task = new Task("task#1", "taskForCheck", NEW,
                "24.02.2022 05:00", 60);
        taskManager.addTask(task);
        assertEquals(1, taskManager.getTasks().size(), "Задача не добавлена");
        taskManager.deleteTaskById(task.getId());
        assertEquals(0, taskManager.getTasks().size(), "Задача не удалена");
    }

    @Test
    void deleteTaskByIdSubtask() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                NEW, "22.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask1);
        taskManager.deleteTaskById(subtask1.getId());
        int sizeChild = taskManager.getEpics().get(epic1.getId()).getChildSubtasks().size();
        assertEquals(0, sizeChild, "Список подзадач не пуст");
        assertEquals(0, taskManager.getSubtasks().size(), "Подзадача не удалена");
        assertEquals(NEW, epic1.getStatus(), "Статус эпика при удалении подзадачи неверный");
    }

    @Test
    void deleteTaskByWrongId() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        try {
            Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
            taskManager.addEpic(epic1);
            Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                    DONE, "22.02.2022 22:30", 60, epic1);
            taskManager.addSubtask(subtask1);
            taskManager.deleteTaskById(3);
        } catch (IllegalArgumentException e) {
            assertNotEquals("", e.getMessage());
        }
    }

    @Test
    void getAllTypeTasksListSub() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask1);
        Subtask subtask3 = new Subtask("subtask#1", "subtaskForCheck",
                IN_PROGRESS, "17.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask3);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                NEW, "16.02.2022 15:22", 360, epic1);
        taskManager.addSubtask(subtask2);
        List<Task> subtasksList = taskManager.getAllTypeTasksList(TaskType.SUBTASK);
        assertNotNull(subtasksList, "Задачи не возвращаются.");
        assertEquals(3, subtasksList.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasksList.get(0), "Задачи не совпадают.");
        assertEquals(subtask3, subtasksList.get(1), "Задачи не совпадают.");
        assertEquals(IN_PROGRESS, epic1.getStatus(), "Статус эпика неверный");
    }

    @Test
    void getAllTypeTasksListEpic() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
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
        List<Task> epicsList = taskManager.getAllTypeTasksList(TaskType.EPIC);
        assertNotNull(epicsList, "Задачи не возвращаются.");
        assertEquals(2, epicsList.size(), "Неверное количество задач.");
        assertEquals(epic1, epicsList.get(0), "Задачи не совпадают.");
        assertEquals(epic2, epicsList.get(1), "Задачи не совпадают.");
    }

    @Test
    void getAllTypeTasksListTask() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Task task1 = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);
        taskManager.addTask(task1);
        Task task2 = new Task("task#2", "taskForCheck", Task.Status.DONE,
                "24.02.2022 07:00", 60);
        taskManager.addTask(task2);
        List<Task> tasksList = taskManager.getAllTypeTasksList(TaskType.TASK);
        assertNotNull(tasksList, "Задачи не возвращаются.");
        assertEquals(2, tasksList.size(), "Неверное количество задач.");
        assertEquals(task1, tasksList.get(0), "Задачи не совпадают.");
        assertEquals(task2, tasksList.get(1), "Задачи не совпадают.");
    }

    @Test
    void deleteAllTasksFromSet() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
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
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        assertEquals(0, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getSubtasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getEpics().size(), "Неверное количество задач.");
    }

    @Test
    void getTaskByWrongId() throws CloneNotSupportedException {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        try {
            Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
            taskManager.addEpic(epic1);
            Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                    DONE, "22.02.2022 22:30", 60, epic1);
            taskManager.addSubtask(subtask1);
            taskManager.getTaskById(3);
        } catch (IllegalArgumentException e) {
            assertEquals("Нет такого id", e.getMessage());
        }
    }

    @Test
    void getTaskById() throws CloneNotSupportedException {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask1);
        Task task1 = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);
        taskManager.addTask(task1);
        Task subtask = taskManager.getTaskById(subtask1.getId());
        Task task = taskManager.getTaskById(task1.getId());
        Task epic = taskManager.getTaskById(epic1.getId());
        assertEquals(subtask, subtask1, "Задачи не равны");
        assertEquals(task, task1, "Задачи не равны");
        assertEquals(epic, epic1, "Задачи не равны");
    }

    @Test
    void updateEpic() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        String nameFirst = epic1.getDescription();
        epic1.setDescription("newDescription");
        assertEquals("epicForCheck", nameFirst, "Описание не совпадает");
        taskManager.updateEpic(epic1);
        assertEquals("newDescription", epic1.getDescription(), "Описание не совпадает");
    }

    @Test
    void updateTask() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Task taskFOrUpdate = new Task("taskFOrUpdate", "taskFOrUpdate", Task.Status.NEW,
                "11.08.2022 05:00", 60);
        taskManager.addTask(taskFOrUpdate);
        taskFOrUpdate.setDescription("newDescription");
        taskManager.updateTask(taskFOrUpdate);
        assertEquals("newDescription", taskFOrUpdate.getDescription(), "Описание не совпадает");
    }

    @Test
    void updateSubtask() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask1);
        assertEquals(DONE, epic1.getStatus(), "Статус эпика неверный");
        subtask1.changeStatus(IN_PROGRESS);
        assertEquals(DONE, epic1.getStatus(), "Статус эпика неверный");
        taskManager.updateSubtask(subtask1);
        assertEquals(IN_PROGRESS, epic1.getStatus(), "Статус эпика неверный");
    }

    @Test
    void getTasksFromEpic() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask1);
        Subtask subtask3 = new Subtask("subtask#1", "subtaskForCheck",
                IN_PROGRESS, "17.02.2022 22:30", 60, epic1);
        taskManager.addSubtask(subtask3);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                NEW, "16.02.2022 15:22", 360, epic1);
        taskManager.addSubtask(subtask2);
        ArrayList<String> subtasksName = taskManager.getTasksFromEpicTask(epic1);
        assertNotNull(subtasksName, "Задачи не возвращаются.");
        assertEquals(3, subtasksName.size(), "Неверное количество задач.");
        assertEquals("subtask#1", subtasksName.get(0), "Задачи не совпадают.");
    }
}