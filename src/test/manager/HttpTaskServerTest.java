package test.manager;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import main.Server.HttpTaskServer;
import main.manager.TaskManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static main.task.Task.Status.*;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private HttpTaskServer server;
    private TaskManager taskManager;
    private Task task;
    private Task task2;
    private Epic epic;
    private Subtask subtask;


 private Gson gson = new GsonBuilder()
         .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateAdapterTime().nullSafe())
         .create();

    @BeforeEach
    void setUp() throws Exception {
        server = new HttpTaskServer();
        taskManager = server.getTaskManager();

        epic = new Epic("epic#222", "epicForCheck", NEW);
        taskManager.addEpic(epic);
        subtask = new Subtask("subtask#11", "subtaskForCheck",
                IN_PROGRESS, "12.02.2022 22:30", 60,  epic.getId());
        taskManager.addSubtask(subtask);

        task = new Task("task#22", "taskForCheck", Task.Status.DONE,
                "22.08.2022 07:00", 60);
        task2 = new Task("task#222", "taskForCheck", Task.Status.NEW,
                "18.08.2022 07:00", 60);
        taskManager.addTask(task);
        taskManager.addTask(task2);

        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> tasks = gson.fromJson(response.body(), taskType);
        assertEquals(task, tasks.get(0), "Задачи не совпадают");
    }

    @Test
    void getPriorityTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonObject = jsonElement.getAsJsonArray();

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> tasks = gson.fromJson(jsonObject, taskType);
        assertEquals(2, tasks.get(0).getId(), "Задачи не совпадают");
    }

    @Test
    void getTaskByIdForFromEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<Epic>() {}.getType();

        Epic received = gson.fromJson(response.body(), epicType)    ;
        assertEquals(epic,received, "Задачи не сходятся");
    }

    @Test
    void getTaskByIdForFromTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>() {}.getType();

        Task received = gson.fromJson(response.body(), taskType)    ;
        assertEquals(task,received, "Задачи не сходятся");
    }

    @Test
    void getTaskByIdForFromSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<Subtask>() {}.getType();

        Subtask received = gson.fromJson(response.body(), subtaskType)    ;
        assertEquals(subtask,received, "Задачи не сходятся");
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<ArrayList<Epic>>() {}.getType();
        List<Epic> epics = gson.fromJson(response.body(), epicType);
        assertEquals(epic, epics.get(0), "Задачи не совпадают");
    }
}