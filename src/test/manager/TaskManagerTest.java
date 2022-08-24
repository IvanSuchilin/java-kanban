package test.manager;

import main.manager.TaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static main.task.Task.Status.*;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    static final Epic EPIC_TMT = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
    static final Subtask SUBTASK_FIRST = new Subtask("subtask#1", "subtaskForCheck",
            DONE, "22.02.2022 22:30", 60, EPIC_TMT.getId());
    static final Subtask SUBTASK_SECOND = new Subtask("subtask#2", "subtaskForCheck",
            IN_PROGRESS, "17.02.2022 22:30", 60, EPIC_TMT.getId());
    static final Subtask SUBTASK_THIRD = new Subtask("subtask#3", "third sub in epicTask1",
            NEW, "16.02.2022 15:22", 360, EPIC_TMT.getId());

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void getHistorySizeTest() throws CloneNotSupportedException {
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.addEpic(EPIC_TMT);
        Subtask firstFOrCheck = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "12.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask secondFOrCheck = new Subtask("subtask#2", "subtaskForCheck",
                IN_PROGRESS, "11.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask thirdFOrCheck = new Subtask("subtask#3", "third sub in epicTask1",
                NEW, "09.02.2022 15:22", 360, EPIC_TMT.getId());
        taskManager.addSubtask(firstFOrCheck);
        taskManager.addSubtask(secondFOrCheck);
        taskManager.addSubtask(thirdFOrCheck);
        taskManager.getTaskById(EPIC_TMT.getId());
        taskManager.getTaskById(firstFOrCheck.getId());
        taskManager.getTaskById(secondFOrCheck.getId());
        taskManager.getTaskById(thirdFOrCheck.getId());
        List<Task> history = taskManager.getHistory();
        assertNotNull(history, "Задачи не возвращаются.");
        assertEquals(4, history.size(), "Неверное количество задач.");
    }

    @Test
    void getTaskSetTest() {
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.addEpic(EPIC_TMT);
        Subtask firstFOrCheck = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "12.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask secondFOrCheck = new Subtask("subtask#2", "subtaskForCheck",
                IN_PROGRESS, "11.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask thirdFOrCheck = new Subtask("subtask#3", "third sub in epicTask1",
                NEW, "09.02.2022 15:22", 360, EPIC_TMT.getId());
        taskManager.addSubtask(firstFOrCheck);
        taskManager.addSubtask(secondFOrCheck);
        taskManager.addSubtask(thirdFOrCheck);
        TreeSet<Task> tasksForValidation = (TreeSet<Task>) taskManager.getTaskSet();
        assertEquals(3, tasksForValidation.size(), "Неверное количество задач.");
        assertEquals(thirdFOrCheck, tasksForValidation.first(), "Неверная задача.");
    }

    @Test
    void getTaskSetAndDeleteSubtaskTest() {
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.addEpic(EPIC_TMT);
        Subtask firstFOrCheck = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "12.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask secondFOrCheck = new Subtask("subtask#2", "subtaskForCheck",
                IN_PROGRESS, "11.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask thirdFOrCheck = new Subtask("subtask#3", "third sub in epicTask1",
                NEW, "09.02.2022 15:22", 360, EPIC_TMT.getId());
        taskManager.addSubtask(firstFOrCheck);
        taskManager.addSubtask(secondFOrCheck);
        taskManager.addSubtask(thirdFOrCheck);
        TreeSet<Task> tasksForValidation = (TreeSet<Task>) taskManager.getTaskSet();
        taskManager.deleteTaskById(thirdFOrCheck.getId());
        assertEquals(secondFOrCheck, tasksForValidation.first(), "Неверная задача.");
    }

    @Test
    void getTasksTest() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        Task task1 = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);
        taskManager.addTask(task1);
        Task task2 = new Task("task#2", "taskForCheck", Task.Status.DONE,
                "24.02.2022 07:00", 60);
        taskManager.addTask(task2);
        Map<Integer, Task> tasks = taskManager.getTasks();
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(task1.getId()), "Задачи не совпадают.");
    }

    @Test
    void getEpicsTest() {
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epic3 = new Epic("epic#3", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic3);
        Epic epic2 = new Epic("epic#2", "epicForCheck", NEW);
        taskManager.addEpic(epic2);
        Map<Integer, Epic> epics = taskManager.getEpics();
        assertEquals(2, epics.size(), "Неверное количество задач.");
        assertEquals(epic2, epics.get(epic2.getId()), "Задачи не совпадают.");
    }

    @Test
    void getSubtasksTest() {
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.addEpic(EPIC_TMT);
        Subtask firstFOrCheck = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "12.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask secondFOrCheck = new Subtask("subtask#2", "subtaskForCheck",
                IN_PROGRESS, "11.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask thirdFOrCheck = new Subtask("subtask#3", "third sub in epicTask1",
                NEW, "09.02.2022 15:22", 360, EPIC_TMT.getId());
        taskManager.addSubtask(firstFOrCheck);
        taskManager.addSubtask(secondFOrCheck);
        taskManager.addSubtask(thirdFOrCheck);
        assertEquals(3, taskManager.getSubtasks().size(), "Неверное количество задач.");
        assertEquals(thirdFOrCheck, taskManager.getSubtasks().get(thirdFOrCheck.getId()), "Задачи не совпадают.");
    }

    @Test
    void addSubtaskTestCheckParentsStatus() {
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        Epic checkEpic = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(checkEpic);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, checkEpic.getId());
        Task taskWithId = taskManager.addSubtask(subtask1);
        final int subtaskId = taskWithId.getId();
        final Task savedTask = taskManager.getSubtasks().get(subtaskId);
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(subtask1, savedTask, "Задачи не совпадают");
        final List<Task> tasks = taskManager.getAllTypeTasksList(TaskType.SUBTASK);
        assertEquals(subtask1, tasks.get(0), "Задачи не совпадают.");
        assertEquals(DONE, checkEpic.getStatus(), "Статус эпика при добавлении подзадачи неверный");
    }

    @Test
    void addSubtaskTestCheckParents() {
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        Epic checkEpic = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(checkEpic);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, checkEpic.getId());
        taskManager.addSubtask(subtask1);
        assertEquals(checkEpic.getId(), subtask1.getParentId(), "Родитель подзадачи не совпадает");
    }

    @Test
    void addTaskTest() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        Task task = new Task("task#1", "taskForCheck", NEW,
                "24.02.2022 05:00", 60);
        Task taskWithId = taskManager.addTask(task);
        final int taskId = taskWithId.getId();
        final Task savedTask = taskManager.getTasks().get(taskId);
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");
        final List<Task> tasks = taskManager.getAllTypeTasksList(TaskType.TASK);
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addEpicTest() {
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Task taskWithId = taskManager.addEpic(EPIC_TMT);
        final int taskId = taskWithId.getId();
        final Task savedTask = taskManager.getEpics().get(taskId);
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(EPIC_TMT, savedTask, "Задачи не совпадают");
        final List<Task> tasks = taskManager.getAllTypeTasksList(TaskType.EPIC);
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(EPIC_TMT, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void deleteTaskByIdEpicTest() {
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        taskManager.addEpic(EPIC_TMT);
        Subtask firstFOrCheck = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "12.02.2022 22:30", 60, EPIC_TMT.getId());
        taskManager.addSubtask(firstFOrCheck);
        taskManager.deleteTaskById(EPIC_TMT.getId());
        assertEquals(0, taskManager.getEpics().size(), "Эпик не удален");
        assertEquals(0, taskManager.getSubtasks().size(), "Эпик с подзадачей не удален");
    }

    @Test
    void deleteTaskByIdTaskTest() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        Task task = new Task("task#1", "taskForCheck", NEW,
                "24.02.2022 05:00", 60);
        taskManager.addTask(task);
        assertEquals(1, taskManager.getTasks().size(), "Задача не добавлена");
    }

    @Test
    void deleteTaskByIdSubtaskStatusTest() {
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        Epic epicCheck = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epicCheck);
        Subtask firstFOrCheck = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "12.02.2022 22:30", 60, EPIC_TMT.getId());
        taskManager.addSubtask(firstFOrCheck);
        taskManager.deleteTaskById(firstFOrCheck.getId());
        assertEquals(NEW, epicCheck.getStatus(), "Статус эпика при удалении подзадачи неверный");
    }

    @Test
    void deleteTaskByWrongIdTest() {
        try {
            taskManager.addEpic(EPIC_TMT);
            Subtask firstFOrCheck = new Subtask("subtask#1", "subtaskForCheck",
                    DONE, "12.02.2022 22:30", 60, EPIC_TMT.getId());
            taskManager.deleteTaskById(3);
        } catch (IllegalArgumentException e) {
            assertNotEquals("", e.getMessage());
        }
    }

    @Test
    void getAllTypeTasksListSubTest() {
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        taskManager.addEpic(EPIC_TMT);
        Subtask firstFOrCheck = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "12.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask secondFOrCheck = new Subtask("subtask#2", "subtaskForCheck",
                IN_PROGRESS, "11.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask thirdFOrCheck = new Subtask("subtask#3", "third sub in epicTask1",
                NEW, "09.02.2022 15:22", 360, EPIC_TMT.getId());
        taskManager.addSubtask(firstFOrCheck);
        taskManager.addSubtask(secondFOrCheck);
        taskManager.addSubtask(thirdFOrCheck);
        List<Task> subtasksList = taskManager.getAllTypeTasksList(TaskType.SUBTASK);
        assertEquals(3, subtasksList.size(), "Неверное количество задач.");
        assertEquals(firstFOrCheck, subtasksList.get(0), "Задачи не совпадают.");
    }

    @Test
    void getAllTypeTasksListEpicTest() {
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        taskManager.addEpic(EPIC_TMT);
        Epic epic2 = new Epic("epic#2", "epicForCheck", NEW);
        taskManager.addEpic(epic2);
        Subtask firstFOrCheck = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "12.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask secondFOrCheck = new Subtask("subtask#2", "subtaskForCheck",
                IN_PROGRESS, "11.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask thirdFOrCheck = new Subtask("subtask#3", "third sub in epicTask1",
                NEW, "09.02.2022 15:22", 360, EPIC_TMT.getId());
        taskManager.addSubtask(firstFOrCheck);
        taskManager.addSubtask(secondFOrCheck);
        taskManager.addSubtask(thirdFOrCheck);
        List<Task> epicsList = taskManager.getAllTypeTasksList(TaskType.EPIC);
        assertEquals(2, epicsList.size(), "Неверное количество задач.");
        assertEquals(EPIC_TMT, epicsList.get(0), "Задачи не совпадают.");
        assertEquals(epic2, epicsList.get(1), "Задачи не совпадают.");
    }

    @Test
    void getAllTypeTasksListTaskTest() {
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        Task task1 = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);
        taskManager.addTask(task1);
        Task task2 = new Task("task#2", "taskForCheck", Task.Status.DONE,
                "24.02.2022 07:00", 60);
        taskManager.addTask(task2);
        List<Task> tasksList = taskManager.getAllTypeTasksList(TaskType.TASK);
        assertEquals(2, tasksList.size(), "Неверное количество задач.");
        assertEquals(task1, tasksList.get(0), "Задачи не совпадают.");
    }

    @Test
    void deleteAllTasksFromTaskSetTest() {
        taskManager.addEpic(EPIC_TMT);
        Task task1 = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);
        taskManager.addTask(task1);
        Task task2 = new Task("task#2", "taskForCheck", Task.Status.DONE,
                "24.02.2022 07:00", 60);
        taskManager.addTask(task2);
        taskManager.deleteAllTasksFromSet(TaskType.TASK);
        assertEquals(0, taskManager.getTasks().size(), "Неверное количество задач.");
    }

    @Test
    void deleteAllTasksFromEpicSetTest() {
        taskManager.addEpic(EPIC_TMT);
        Epic epic2 = new Epic("epic#2", "epicForCheck", NEW);
        taskManager.addEpic(epic2);
        Subtask firstFOrCheck = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "12.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask secondFOrCheck = new Subtask("subtask#2", "subtaskForCheck",
                IN_PROGRESS, "11.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask thirdFOrCheck = new Subtask("subtask#3", "third sub in epicTask1",
                NEW, "09.02.2022 15:22", 360, EPIC_TMT.getId());
        taskManager.addSubtask(firstFOrCheck);
        taskManager.addSubtask(secondFOrCheck);
        taskManager.addSubtask(thirdFOrCheck);
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        assertEquals(0, taskManager.getSubtasks().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getEpics().size(), "Неверное количество задач.");
    }

    @Test
    void deleteAllTasksFromSubtaskSetTest() {
        taskManager.addEpic(EPIC_TMT);
        Epic epic2 = new Epic("epic#2", "epicForCheck", NEW);
        taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic2.getId());
        taskManager.addSubtask(subtask1);
        Subtask secondFOrCheck = new Subtask("subtask#2", "subtaskForCheck",
                IN_PROGRESS, "11.02.2022 22:30", 60, EPIC_TMT.getId());
        Subtask thirdFOrCheck = new Subtask("subtask#3", "third sub in epicTask1",
                NEW, "09.02.2022 15:22", 360, EPIC_TMT.getId());
        taskManager.addSubtask(secondFOrCheck);
        taskManager.addSubtask(thirdFOrCheck);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        assertEquals(0, taskManager.getSubtasks().size(), "Неверное количество задач.");
    }

    @Test
    void getTaskByWrongIdTest() throws CloneNotSupportedException {
        try {
            taskManager.addEpic(EPIC_TMT);
            Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                    DONE, "22.02.2022 22:30", 60, EPIC_TMT.getId());
            taskManager.addSubtask(subtask1);
            taskManager.getTaskById(3);
        } catch (IllegalArgumentException e) {
            assertEquals("Нет такого id", e.getMessage());
        }
    }

    @Test
    void getTaskByIdTaskTest() throws CloneNotSupportedException {
        Task task1 = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);
        taskManager.addTask(task1);
        Task task = taskManager.getTaskById(task1.getId());
        assertEquals(task, task1, "Задачи не равны");
    }

    @Test
    void getTaskByIdSubtaskTest() throws CloneNotSupportedException {
        taskManager.addEpic(EPIC_TMT);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, EPIC_TMT.getId());
        taskManager.addSubtask(subtask1);
        Task subtask = taskManager.getTaskById(subtask1.getId());
        assertEquals(subtask, subtask1, "Задачи не равны");
    }

    @Test
    void getTaskByIdEpicTest() throws CloneNotSupportedException {
        taskManager.addEpic(EPIC_TMT);
        Task epic = taskManager.getTaskById(EPIC_TMT.getId());
        assertEquals(epic, EPIC_TMT, "Задачи не равны");
    }

    @Test
    void withOutUpdateEpicTest() {
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        String nameFirst = epic1.getDescription();
        EPIC_TMT.setDescription("newDescription");
        assertEquals("epicForCheck", nameFirst, "Описание не совпадает");
    }

    @Test
    void updateEpicTest() {
        taskManager.addEpic(EPIC_TMT);
        EPIC_TMT.setDescription("newDescription");
        taskManager.updateEpic(EPIC_TMT);
        assertEquals("newDescription", EPIC_TMT.getDescription(), "Описание не совпадает");
    }

    @Test
    void updateTaskTest() {
        Task taskFOrUpdate = new Task("taskFOrUpdate", "taskFOrUpdate", Task.Status.NEW,
                "11.08.2022 05:00", 60);
        taskManager.addTask(taskFOrUpdate);
        taskFOrUpdate.setDescription("newDescription");
        taskManager.updateTask(taskFOrUpdate);
        assertEquals("newDescription", taskFOrUpdate.getDescription(), "Описание не совпадает");
    }

    @Test
    void withOutUpdateSubtaskTest() {
        Epic epic3 = new Epic("epic#3", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic3);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic3.getId());
        taskManager.addSubtask(subtask1);
        assertEquals(DONE, epic3.getStatus(), "Статус эпика неверный");
    }

    @Test
    void updateSubtaskTest() {
        Epic epic3 = new Epic("epic#3", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic3);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic3.getId());
        taskManager.addSubtask(subtask1);
        subtask1.changeStatus(IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        assertEquals(IN_PROGRESS, epic3.getStatus(), "Статус эпика неверный");
    }

    @Test
    void getTasksFromEpicTest() {
        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        Epic epic1 = new Epic("epic#1", "epicForCheck", IN_PROGRESS);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                DONE, "22.02.2022 22:30", 60, epic1.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask3 = new Subtask("subtask#1", "subtaskForCheck",
                IN_PROGRESS, "17.02.2022 22:30", 60, epic1.getId());
        taskManager.addSubtask(subtask3);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                NEW, "16.02.2022 15:22", 360, epic1.getId());
        taskManager.addSubtask(subtask2);
        List<String> subtasksName = taskManager.getTasksFromEpicTask(epic1);
        assertNotNull(subtasksName, "Задачи не возвращаются.");
        assertEquals(3, subtasksName.size(), "Неверное количество задач.");
        assertEquals("subtask#1", subtasksName.get(0), "Задачи не совпадают.");
    }
}