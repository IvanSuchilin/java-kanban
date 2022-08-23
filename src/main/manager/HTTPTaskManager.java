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
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {

    private KVTaskClient client;
    public URI url;

    public HTTPTaskManager(String url) throws Exception {
        this.url = URI.create(url);
        client = new KVTaskClient(url );
    }

    private URI getUrl() {
        return url;
    }

    public KVTaskClient getClient() {
        return client;
    }

    @Override
    protected void save() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateAdapterTime().nullSafe())
                .create();
        client.put("tasks", gson.toJson(new ArrayList<>(getTasks().values())));
        client.put("subtasks", gson.toJson(new ArrayList<>(getSubtasks().values())));
        client.put("epics", gson.toJson(new ArrayList<>(getEpics().values())));
        List<Integer> history = getHistory().stream()
                        .map(Task::getId)
                .collect(Collectors.toList());
        client.put("history", gson.toJson(history));
    }

    public static HTTPTaskManager loadFromKVS() throws Exception {
        KVTaskClient kvTaskClient =new KVTaskClient("http://localhost:8078");
        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {}.getType();
        Type epicType = new TypeToken<ArrayList<Epic>>() {}.getType();
        Type historyType = new TypeToken<ArrayList<Integer>>() {}.getType();
        HTTPTaskManager httpBacked = new HTTPTaskManager("http://localhost:8078");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateAdapterTime().nullSafe())
                .create();
       // System.out.println(kvTaskClient.load("tasks"));
        List<Task> tasksList = gson.fromJson( kvTaskClient.load("tasks"), taskType);
        for (Task task:tasksList){
            httpBacked.getTasks().put(task.getId(), task);
            httpBacked.getPrioritizedTasks().add(task);
        }
        System.out.println(kvTaskClient.load("subtasks"));
        List<Subtask> subtasksList = gson.fromJson( kvTaskClient.load("subtasks"), subtaskType);
        for (Subtask subtask:subtasksList){
            httpBacked.getSubtasks().put(subtask.getId(), subtask);
            httpBacked.getPrioritizedTasks().add(subtask);
        }
        List<Epic> epicsList = gson.fromJson(kvTaskClient.load("epics"), epicType);
        for (Epic epic:epicsList){
            httpBacked.getEpics().put(epic.getId(), epic);
        }
        List <Integer> history = gson.fromJson(kvTaskClient.load("history"), historyType);
        for (Integer id:history){
            httpBacked.getTaskById(id);
        }


        return httpBacked;
    }
}

