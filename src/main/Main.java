package main;

import main.Server.HttpTaskServer;
import main.Server.KVServer;
import main.Server.KVTaskClient;
import main.manager.FileBackedTasksManager;
import main.manager.Managers;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.io.IOException;

import static main.task.Task.Status.*;
import static main.task.Task.Status.NEW;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {


            new KVServer().start();
        KVTaskClient klient = new KVTaskClient("http://localhost:8078/register");
        System.out.println(klient.getApiToken());


        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Epic epic2Fb = new Epic("epic#222", "epicForCheck", NEW);
        HttpTaskServer.fBManager.addEpic(epic2Fb);
        Subtask subtask1Fb = new Subtask("subtask#11", "subtaskForCheck",
                DONE, "12.02.2022 22:30", 60, epic2Fb.getId());
        HttpTaskServer.fBManager.addSubtask(subtask1Fb);
        Task task1Fb = new Task("task#11", "taskForCheck", Task.Status.NEW,
                "11.02.2022 05:00", 60);
        HttpTaskServer.fBManager.addTask(task1Fb);
        Task task2FB = new Task("task#22", "taskForCheck", Task.Status.DONE,
                "15.02.2022 07:00", 60);
        HttpTaskServer.fBManager.addTask(task2FB);
    }
}


