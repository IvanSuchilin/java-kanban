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

        JsonArray jsonTasks = JsonParser.parseString(response.body()).getAsJsonArray();
        Type taskType = new TypeToken<Task>() {
        }.getType();
        Type subTaskType = new TypeToken<Subtask>() {
        }.getType();
        ArrayList<Task> tasks = new ArrayList<Task>();
        for (int i = 0; i < jsonTasks.size(); i++) {
            JsonObject taskJSON = jsonTasks.get(i).getAsJsonObject();
            if (taskJSON.has("parentId")) {
                tasks.add(gson.fromJson(taskJSON, subTaskType));
            } else {
                tasks.add(gson.fromJson(taskJSON, taskType));
            }
        }
        assertEquals(subtask, tasks.get(0), "Задачи не совпадают");
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

    @Test
    void getSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {}.getType();
        List<Subtask> subtasks = gson.fromJson(response.body(), subtaskType);
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают");
    }

    @Test
    void deleteSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(0, taskManager.getSubtasks().size(), "Размер не совпадает");
    }

    @Test
    void deleteAllSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(0, taskManager.getSubtasks().size(), "Размер не совпадает");
    }

    @Test
    void deleteTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(1, taskManager.getTasks().size(), "Размер не совпадает");
    }

    @Test
    void deleteAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(0, taskManager.getTasks().size(), "Размер не совпадает");
    }

    @Test
    void deleteEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(0, taskManager.getEpics().size(), "Размер не совпадает");
        assertEquals(0, taskManager.getSubtasks().size(), "Размер не совпадает");
    }

    @Test
    void deleteAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(0, taskManager.getEpics().size(), "Размер не совпадает");
        assertEquals(0, taskManager.getSubtasks().size(), "Размер не совпадает");
    }


    @Test
    void getHistory() throws IOException, InterruptedException, CloneNotSupportedException {
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(4);
        taskManager.getTaskById(3);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonObject = jsonElement.getAsJsonArray();

        JsonArray jsonTasks = JsonParser.parseString(response.body()).getAsJsonArray();
        Type epicTaskType = new TypeToken<Epic>() {
        }.getType();
        Type taskType = new TypeToken<Task>() {
        }.getType();
        Type subTaskType = new TypeToken<Subtask>() {
        }.getType();
        ArrayList<Task> tasks = new ArrayList<Task>();
        for (int i = 0; i < jsonTasks.size(); i++) {
            JsonObject taskJSON = jsonTasks.get(i).getAsJsonObject();
            if (taskJSON.has("parentId")) {
                tasks.add(gson.fromJson(taskJSON, subTaskType));
                if (taskJSON.has("childSubtasks")) {
                    tasks.add(gson.fromJson(taskJSON, epicTaskType));
                } else {
                    tasks.add(gson.fromJson(taskJSON, taskType));
                }
            }
            assertEquals(4, taskManager.getHistory().size(), "Размер истории не совпадает");
        }
    }
}
