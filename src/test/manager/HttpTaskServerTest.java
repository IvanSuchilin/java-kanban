package test.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.Server.HttpTaskServer;
import main.Server.KVServer;
import main.manager.HTTPTaskManager;
import main.manager.InMemoryTaskManager;
import main.manager.TaskManager;
import main.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private HttpTaskServer server;
    private TaskManager taskManager;
    private Task task;


 private Gson gson = new Gson();

    @BeforeEach
    void setUp() throws Exception {
        server = new HttpTaskServer();
        taskManager = new InMemoryTaskManager();

        task = new Task("task#22", "taskForCheck", Task.Status.DONE,
                "22.08.2022 07:00", 60);
        taskManager.addTask(task);
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

       /* String body = response.body();
        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> tasks = gson.fromJson(response.body(), taskType);
        assertEquals(task, tasks.get(0), "Задачи не совпадают");*/
    }

}