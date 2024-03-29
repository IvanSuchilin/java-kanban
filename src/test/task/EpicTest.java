package test.task;

import main.manager.FileBackedTasksManager;
import main.manager.InMemoryTaskManager;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static main.task.Task.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    private Epic epic;
    private FileBackedTasksManager fileBackedTasksManager;
    private TaskManager tasksManager;

    @BeforeEach
    public void create() {
        fileBackedTasksManager = new FileBackedTasksManager();
        tasksManager = new InMemoryTaskManager();
        epic = new Epic("epicName1", "try for check1", DONE);
        fileBackedTasksManager.addEpic(epic);
        tasksManager.addEpic(epic);
    }

    @Test
    public void getStatusWithEmptySubtasks() {
        assertEquals(NEW, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void getStatusWithNewSubtasksFb() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                NEW, "17.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                NEW, "16.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask2);
        assertEquals(NEW, epic.getStatus(), "Wrong Status");
        assertEquals(LocalDateTime.of(2022, 02, 16, 15, 22), epic.getStartTime());
        assertEquals(Duration.ofMinutes(720), epic.getDuration());
    }

    @Test
    public void getStatusWithNewSubtasksTm() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                NEW, "17.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                NEW, "16.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask2);
        assertEquals(NEW, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void getStatusWithDoneSubtasksFb() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                DONE, "17.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                DONE, "16.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask2);
        assertEquals(DONE, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void getStatusWithDoneSubtasksTm() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                DONE, "17.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                DONE, "16.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask2);
        assertEquals(DONE, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void getStatusWithDoneNewSubtasksFb() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                DONE, "17.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                NEW, "16.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask2);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void getStatusWithDoneNewSubtasks() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                NEW, "17.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                DONE, "16.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask2);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void getStatusWithInProgressSubtasksFb() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                IN_PROGRESS, "17.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                IN_PROGRESS, "16.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask2);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void getStatusWithInProgressSubtasksTm() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                IN_PROGRESS, "17.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                IN_PROGRESS, "16.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask2);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void getStatusWithOneNewSubtasksTm() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                NEW, "17.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask1);
        assertEquals(NEW, epic.getStatus(), "Wrong Status");
        assertEquals(LocalDateTime.of(2022, 02, 17, 15, 22), epic.getStartTime());
        assertEquals(Duration.ofMinutes(360), epic.getDuration());
    }

    @Test
    public void getStatusWithOneDoneSubtasksTm() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                DONE, "17.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask1);
        assertEquals(DONE, epic.getStatus(), "Wrong Status");
        assertEquals(LocalDateTime.of(2022, 02, 17, 15, 22), epic.getStartTime());
        assertEquals(Duration.ofMinutes(360), epic.getDuration());
    }

    @Test
    public void getStatusWithOneInProgressSubtasksTm() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                IN_PROGRESS, "17.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask1);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Wrong Status");
        assertEquals(LocalDateTime.of(2022, 02, 17, 15, 22), epic.getStartTime());
        assertEquals(Duration.ofMinutes(360), epic.getDuration());
    }

    @Test
    public void getStatusWithOneNewSubtasksFb() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                NEW, "17.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask1);
        assertEquals(NEW, epic.getStatus(), "Wrong Status");
        assertEquals(LocalDateTime.of(2022, 02, 17, 15, 22), epic.getStartTime());
        assertEquals(Duration.ofMinutes(360), epic.getDuration());
    }

    @Test
    public void getStatusWithOneDoneSubtasksFb() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                DONE, "17.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask1);
        assertEquals(DONE, epic.getStatus(), "Wrong Status");
        assertEquals(LocalDateTime.of(2022, 02, 17, 15, 22), epic.getStartTime());
        assertEquals(Duration.ofMinutes(360), epic.getDuration());
    }

    @Test
    public void getStatusWithOneInProgressSubtasksFb() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                IN_PROGRESS, "17.02.2022 15:22", 860, epic.getId());
        fileBackedTasksManager.addSubtask(subtask1);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Wrong Status");
        assertEquals(LocalDateTime.of(2022, 02, 17, 15, 22), epic.getStartTime());
        assertEquals(Duration.ofMinutes(860), epic.getDuration());
    }

    @Test
    public void getStatusWithThreeSubsFb() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                IN_PROGRESS, "17.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                DONE, "16.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("thirdSubtask", "third sub in epicTask1",
                NEW, "15.02.2022 13:25", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask3);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Wrong Status");
        assertEquals(LocalDateTime.of(2022, 02, 15, 13, 25), epic.getStartTime());
        assertEquals(Duration.ofMinutes(1080), epic.getDuration());
    }

    @Test
    public void getStatusWithThreeSubsTm() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                IN_PROGRESS, "04.01.2020 13:25", 360, epic.getId());
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                DONE, "16.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("thirdSubtask", "third sub in epicTask1",
                NEW, "15.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask3);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Wrong Status");
        assertEquals(IN_PROGRESS, epic.getStatus(), "Wrong Status");
        assertEquals(LocalDateTime.of(2020, 01, 04, 13, 25), epic.getStartTime());
        assertEquals(Duration.ofMinutes(1080), epic.getDuration());
    }

    @Test
    public void shouldBeEpicStatusDoneWithSubtasksStatusInProgressNewFb() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                IN_PROGRESS, "17.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                NEW, "16.02.2022 15:22", 360, epic.getId());
        fileBackedTasksManager.addSubtask(subtask2);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Wrong Status");
        assertEquals(LocalDateTime.of(2022, 02, 16, 15, 22), epic.getStartTime());
        assertEquals(Duration.ofMinutes(720), epic.getDuration());
    }

    @Test
    public void shouldBeEpicStatusDoneWithSubtasksStatusInProgressNewTm() {
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                IN_PROGRESS, "17.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                NEW, "16.02.2022 15:22", 360, epic.getId());
        tasksManager.addSubtask(subtask2);
        assertEquals(IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }
}