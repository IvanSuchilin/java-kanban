package main.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jdk.internal.org.jline.reader.ScriptEngine;
import main.Server.HttpTaskServer;
import main.Server.KVTaskClient;
import main.manager.exception.ManagerLoadException;
import main.manager.exception.ManagerSaveException;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HTTPTaskManager extends FileBackedTasksManager {
    KVTaskClient client;
    URI url;

    public HTTPTaskManager(String url) throws Exception {
        this.url = URI.create(url);
        client = new KVTaskClient(url + "/register");
    }


    @Override
    public Task taskFromString(String value) {
        return super.taskFromString(value);
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        return super.addSubtask(subtask);
    }

    @Override
    public Task addTask(Task task) {
        return super.addTask(task);
    }

    @Override
    public Epic addEpic(Epic epic) {
        return super.addEpic(epic);
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
    }

    @Override
    public void deleteAllTasksFromSet(TaskType nameTasksSet) {
        super.deleteAllTasksFromSet(nameTasksSet);
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    @Override
    protected void save() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateAdapterTime())
                .create();
        client.put("tasks", gson.toJson(new ArrayList<>(getTasks().values())));
        client.put("subtasks", gson.toJson(new ArrayList<>(getSubtasks().values())));
        client.put("epics", gson.toJson(new ArrayList<>(getEpics().values())));
        client.put("history", gson.toJson(new ArrayList<>(getHistory())));
    }

    public HTTPTaskManager loadFromKVS() throws Exception {
        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {}.getType();
        Type epicType = new TypeToken<ArrayList<Epic>>() {}.getType();
        HTTPTaskManager httpBacked = new HTTPTaskManager("http://localhost:8078");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateAdapterTime())
                .create();
        List<Task> tasksList = gson.fromJson( client.load("tasks"), taskType);
        for (Task task:tasksList){
            getTasks().put(task.getId(), task);
        }
        List<Subtask> subtasksList = gson.fromJson( client.load("subtasks"), taskType);
        for (Subtask subtask:subtasksList){
            getTasks().put(subtask.getId(), subtask);
        }
        List<Epic> epicsList = gson.fromJson( client.load("epics"), taskType);
        for (Epic epic:epicsList){
            getTasks().put(epic.getId(), epic);
        }
        ///не могу понять как написать
        client.load("history");

        return httpBacked;
    }
}

