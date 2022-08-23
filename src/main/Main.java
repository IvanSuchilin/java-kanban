package main;

import main.Server.HttpTaskServer;
import main.Server.KVServer;
import main.Server.KVTaskClient;
import main.manager.HTTPTaskManager;
import main.manager.Managers;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.io.IOException;

import static main.task.Task.Status.*;
import static main.task.Task.Status.NEW;

public class Main {
    public static void main(String[] args) throws Exception {
        new KVServer().start();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
      /*  HTTPTaskManager tasksManager = (HTTPTaskManager) Managers.getDefault();
        new KVServer().start();*/
        //KVTaskClient client = new KVTaskClient("http://localhost:8078");
        //System.out.println("Токен при регистрации: " + tasksManager.getClient().getApiToken());

       Epic epic2Fb = new Epic("epic#222", "epicForCheck", NEW);
        HttpTaskServer.taskManager.addEpic(epic2Fb);
        Subtask subtask1Fb = new Subtask("subtask#11", "subtaskForCheck",
                DONE, "12.02.2022 22:30", 60, epic2Fb.getId());
        HttpTaskServer.taskManager.addSubtask(subtask1Fb);
        Task task1Fb = new Task("task#11", "taskForCheck", Task.Status.NEW,
                "11.02.2022 05:00", 60);
        HttpTaskServer.taskManager.addTask(task1Fb);
        Task task2FB = new Task("task#22", "taskForCheck", Task.Status.DONE,
                "15.02.2022 07:00", 60);
        HttpTaskServer.taskManager.addTask(task2FB);
        HttpTaskServer.taskManager.getTaskById(1);
        HTTPTaskManager newHttpManager = HTTPTaskManager.loadFromKVS();
        System.out.println(newHttpManager.getTasks());
    }
}


