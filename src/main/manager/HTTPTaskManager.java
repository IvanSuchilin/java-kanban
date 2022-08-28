package main.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import main.manager.exception.ManagerLoadKVSException;
import main.server.HttpTaskServer;
import main.server.KVTaskClient;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import java.lang.reflect.Type;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {

    private KVTaskClient client;
    private static URI url;

    public HTTPTaskManager(String url) throws Exception {
        this.url = URI.create(url);
        client = new KVTaskClient(url);
    }

    @Override
    protected void save() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateAdapterTime().nullSafe())
                .create();
        client.put("tasks", gson.toJson(new ArrayList<>(getTasks().values())), String.valueOf(url));
        client.put("subtasks", gson.toJson(new ArrayList<>(getSubtasks().values())), String.valueOf(url));
        client.put("epics", gson.toJson(new ArrayList<>(getEpics().values())), String.valueOf(url));
        List<Integer> history = getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        client.put("history", gson.toJson(history), String.valueOf(url));
    }

    public static HTTPTaskManager loadFromKVS() {

        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078");
        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        Type historyType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        try {
            HTTPTaskManager httpBacked = null;
            httpBacked = new HTTPTaskManager("http://localhost:8078");
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateAdapterTime().nullSafe())
                    .create();
            List<Task> tasksList = gson.fromJson(kvTaskClient.load("tasks", String.valueOf(url)), taskType);
            for (Task task : tasksList) {
                httpBacked.getTasks().put(task.getId(), task);
                httpBacked.getTaskSet().add(task);
            }
            System.out.println(kvTaskClient.load("subtasks", String.valueOf(url)));
            List<Subtask> subtasksList = gson.fromJson(kvTaskClient.load("subtasks", String.valueOf(url)), subtaskType);
            for (Subtask subtask : subtasksList) {
                httpBacked.getSubtasks().put(subtask.getId(), subtask);
                httpBacked.getTaskSet().add(subtask);
            }
            List<Epic> epicsList = gson.fromJson(kvTaskClient.load("epics", String.valueOf(url)), epicType);
            for (Epic epic : epicsList) {
                httpBacked.getEpics().put(epic.getId(), epic);
            }
            List<Integer> history = gson.fromJson(kvTaskClient.load("history", String.valueOf(url)), historyType);
            for (Integer id : history) {
                httpBacked.getTaskById(id);
            }
            return httpBacked;
        } catch (Exception e) {
            throw new ManagerLoadKVSException("Загрузка с сервера не удалась");
        }
    }
}

