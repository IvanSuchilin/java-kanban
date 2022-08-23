package test.manager;

import com.google.gson.Gson;
import main.Server.HttpTaskServer;
import main.Server.KVServer;
import main.manager.HTTPTaskManager;
import main.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private HttpTaskServer server;
    HTTPTaskManager taskManager;

    private Task task;
 private Gson gson = new Gson();

    @BeforeEach
    void setUp() throws Exception {
        new KVServer().start();
        server = new HttpTaskServer();
        server.start();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addTask() throws IOException, InterruptedException {
        Task newTask = new Task("task#22", "taskForCheck", Task.Status.DONE,
                "22.08.2022 07:00", 60);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}