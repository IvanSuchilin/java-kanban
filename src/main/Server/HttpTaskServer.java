package main.Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.manager.FileBackedTasksManager;
import main.manager.Managers;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final HttpServer server;
    public static final FileBackedTasksManager fBManager = Managers.getDefaultFileBackedManager();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    static Gson gson = new GsonBuilder()
            // .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapterTime())
            .create();

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new GetPrioritizedTaskHandler());
        server.createContext("/tasks/task", new TaskHandler());
        server.createContext("/tasks/history", new GetHistoryHandler());
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        server.start();
    }

    static class LocalDateAdapterTime extends TypeAdapter<LocalDateTime> {
        DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDatetime) throws IOException {
            jsonWriter.value(localDatetime.format(formatterWriter));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
        }
    }

    static class GetHistoryHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/history запроса от клиента.");
            String response;
            httpExchange.sendResponseHeaders(200, 0);
            response = gson.toJson(fBManager.getHistory());
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }


    static class GetPrioritizedTaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks запроса от клиента.");
            String response;
            ArrayList<Task> arrayResponse;

            String path = httpExchange.getRequestURI().getPath();
            String[] splitStrings = path.split("/");
            if (splitStrings.length == 2) {
                // response = "Работает";
                httpExchange.sendResponseHeaders(200, 0);
                response = gson.toJson(fBManager.getPrioritizedTasks());
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                httpExchange.sendResponseHeaders(200, 0);
                response = splitStrings[3];
                // response = gson.toJson(fBManager.getHistory());
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }

    static class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            //System.out.println( httpExchange.getRequestURI().getQuery());
            System.out.println("Началась обработка " + method + " /tasks/tasks запроса от клиента.");
            String response;
            ArrayList<Task> arrayResponse;
            String path = httpExchange.getRequestURI().getPath();
            String[] splitStrings = path.split("/");
            if (splitStrings.length == 3 && httpExchange.getRequestURI().getQuery() == null) {
                switch (method) {
                    case "GET":
                        httpExchange.sendResponseHeaders(200, 0);
                        arrayResponse = new ArrayList<>(fBManager.getTasks().values());
                        System.out.println("Получение списка всех Task");
                        response = gson.toJson(arrayResponse);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    case "POST":
                        httpExchange.sendResponseHeaders(200, 0);
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        Task task = gson.fromJson(body, Task.class);
                        if (task.getId() != 0) {
                            fBManager.updateTask(task);
                            response = "Задача обновлена";
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } else {
                            Task addTask = fBManager.addTask(task);
                            if (addTask != null) {
                                response = gson.toJson(addTask);
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            } else {
                                response = "Задача не добавлена";
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            }
                        }
                        break;
                    case "DELETE":
                        httpExchange.sendResponseHeaders(200, 0);
                        fBManager.deleteAllTasksFromSet(TaskType.TASK);
                        response = "Удаление всех Task";
                        //response = null;
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                }
            } else if (splitStrings.length == 3 && httpExchange.getRequestURI().getQuery() != null) {
                switch (method) {
                    case "DELETE":
                        httpExchange.sendResponseHeaders(200, 0);
                        int idForDelete = Integer.parseInt(httpExchange.getRequestURI().getQuery().substring(3));
                        fBManager.deleteTaskById(idForDelete);
                        response = "Удаление Task по id=" + idForDelete;
                        // response = null;
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    case "GET":
                        httpExchange.sendResponseHeaders(200, 0);
                        int idGet = Integer.parseInt(httpExchange.getRequestURI().getQuery().substring(3));
                        fBManager.getTaskById(idGet);
                        System.out.println("Получение задачи по id=" + idGet);
                        response = gson.toJson(fBManager.getTaskById(idGet));
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                }
            }
        }
    }

    static class SubtaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            //System.out.println( httpExchange.getRequestURI().getQuery());
            System.out.println("Началась обработка " + method + " /tasks/subtask запроса от клиента.");
            String response;
            ArrayList<Task> arrayResponse;
            String path = httpExchange.getRequestURI().getPath();
            String[] splitStrings = path.split("/");
            if (splitStrings.length == 3 && httpExchange.getRequestURI().getQuery() == null) {
                switch (method) {
                    case "GET":
                        httpExchange.sendResponseHeaders(200, 0);
                        arrayResponse = new ArrayList<>(fBManager.getSubtasks().values());
                        System.out.println("Получение списка всех Subtask");
                        response = gson.toJson(arrayResponse);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    case "POST":
                        httpExchange.sendResponseHeaders(200, 0);
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        if (subtask.getId() != 0) {
                            fBManager.updateSubtask(subtask);
                            response = "Задача обновлена";
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } else {
                            Subtask addTask = fBManager.addSubtask(subtask);
                            if (addTask != null) {
                                response = gson.toJson(addTask);
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            } else {
                                response = "Задача не добавлена";
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            }
                        }
                        break;
                    case "DELETE":
                        httpExchange.sendResponseHeaders(200, 0);
                        fBManager.deleteAllTasksFromSet(TaskType.SUBTASK);
                        response = "Удаление всех Task";
                        //response = null;
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                }
            } else if (splitStrings.length == 3 && httpExchange.getRequestURI().getQuery() != null) {
                switch (method) {
                    case "DELETE":
                        httpExchange.sendResponseHeaders(200, 0);
                        int idForDelete = Integer.parseInt(httpExchange.getRequestURI().getQuery().substring(3));
                        fBManager.deleteTaskById(idForDelete);
                        response = "Удаление Task по id=" + idForDelete;
                        // response = null;
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    case "GET":
                        httpExchange.sendResponseHeaders(200, 0);
                        int idGet = Integer.parseInt(httpExchange.getRequestURI().getQuery().substring(3));
                        fBManager.getTaskById(idGet);
                        System.out.println("Получение задачи по id=" + idGet);
                        response = gson.toJson(fBManager.getTaskById(idGet));
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                }
            }
        }
    }
}






