package manager;


import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    private Epic epic;
    FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
    TaskManager tasksManager = Managers.getDefault();

    @BeforeEach
    public void create (){
         epic = new Epic("epicName1", "try for check1",
                Task.Status.DONE);

    }

    @Test
    public void shouldBeEpicStatusNewInEmpty (){
        assertEquals(Task.Status.NEW, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusNewWithSubtasksStatusNewFb (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.NEW, "17.02.2022 15:22" , 360, epic);
        fileBackedTasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1", Task.Status.NEW,"16.02.2022 15:22" , 360, epic);
        fileBackedTasksManager.addSubtask(subtask2);
        assertEquals(Task.Status.NEW, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusNewWithSubtasksStatusNewTm (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.NEW, "17.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1", Task.Status.NEW, "16.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask2);
        assertEquals(Task.Status.NEW, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusDoneWithSubtasksStatusDoneFb (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.DONE, "17.02.2022 15:22" , 360, epic);
        fileBackedTasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1", Task.Status.DONE, "16.02.2022 15:22" , 360, epic);
        fileBackedTasksManager.addSubtask(subtask2);
        assertEquals(Task.Status.DONE, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusDoneWithSubtasksStatusDoneTm (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.DONE, "17.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1", Task.Status.DONE, "16.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask2);
        assertEquals(Task.Status.DONE, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusDoneWithSubtasksStatusDoneNewFb (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.DONE, "17.02.2022 15:22" , 360, epic);
        fileBackedTasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1", Task.Status.NEW, "16.02.2022 15:22" , 360, epic);
        fileBackedTasksManager.addSubtask(subtask2);
        assertEquals(Task.Status.IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusDoneWithSubtasksStatusDoneNewTm (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.NEW,"17.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1", Task.Status.DONE, "16.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask2);
        assertEquals(Task.Status.IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusDoneWithSubtasksStatusInProgressFb (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.IN_PROGRESS, "17.02.2022 15:22" , 360, epic);
        fileBackedTasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1", Task.Status.IN_PROGRESS, "16.02.2022 15:22" , 360, epic);
        fileBackedTasksManager.addSubtask(subtask2);
        assertEquals(Task.Status.IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusDoneWithSubtasksStatusInProgressTm (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.IN_PROGRESS, "17.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1", Task.Status.IN_PROGRESS, "16.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask2);
        assertEquals(Task.Status.IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusDoneWith1SubtaskStatusNewTm (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.NEW, "17.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask1);
        assertEquals(Task.Status.NEW, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusDoneWith1SubtaskStatusDoneTm (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.DONE, "17.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask1);
        assertEquals(Task.Status.DONE, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusDoneWith1SubtaskStatusInProgressTm (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.IN_PROGRESS, "17.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask1);
        assertEquals(Task.Status.IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusDoneWithSubtasksStatusInProgressDoneFb (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.IN_PROGRESS, "17.02.2022 15:22" , 360, epic);
        fileBackedTasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1", Task.Status.DONE, "16.02.2022 15:22" , 360, epic);
        fileBackedTasksManager.addSubtask(subtask2);
        assertEquals(Task.Status.IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusDoneWithSubtasksStatusInProgressDoneTm (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.IN_PROGRESS, "17.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1", Task.Status.DONE, "16.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask2);
        assertEquals(Task.Status.IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusDoneWithSubtasksStatusInProgressNewFb (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.IN_PROGRESS, "17.02.2022 15:22" , 360, epic);
        fileBackedTasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1", Task.Status.NEW, "16.02.2022 15:22" , 360, epic);
        fileBackedTasksManager.addSubtask(subtask2);
        assertEquals(Task.Status.IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }

    @Test
    public void shouldBeEpicStatusDoneWithSubtasksStatusInProgressNewTm (){
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1", Task.Status.IN_PROGRESS, "17.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1", Task.Status.NEW, "16.02.2022 15:22" , 360, epic);
        tasksManager.addSubtask(subtask2);
        assertEquals(Task.Status.IN_PROGRESS, epic.getStatus(), "Wrong Status");
    }
}