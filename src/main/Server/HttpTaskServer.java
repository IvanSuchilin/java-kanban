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
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;
import main.task.TaskType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final HttpServer server;
    private FileBackedTasksManager taskManager;
    private Gson gson;

    public HttpTaskServer() throws Exception {
        taskManager = new FileBackedTasksManager();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapterTime().nullSafe())
                .create();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new GetPrioritizedTaskHandler());
        server.createContext("/tasks/task", new TaskHandler());
        server.createContext("/tasks/history", new GetHistoryHandler());
        server.createContext("/tasks/subtask", new SubtaskHandler());
        server.createContext("/tasks/epic", new EpicHandler());

    }

    public FileBackedTasksManager getTaskManager() {
        return taskManager;
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        server.start();
    }

    public void stop() {
        System.out.println("Останавливаем сервер на порту " + PORT);
        server.stop(0);
    }

    public static class LocalDateAdapterTime extends TypeAdapter<LocalDateTime> {
        DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDatetime) throws IOException {
            if (localDatetime != null) {
                jsonWriter.value(localDatetime.format(formatterWriter));
            } else {
            }
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
        }
    }

    class GetHistoryHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/history запроса от клиента.");
            String response;
            httpExchange.sendResponseHeaders(200, 0);
            response = gson.toJson(taskManager.getHistory());
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    class GetPrioritizedTaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks запроса от клиента.");
            String response;
            String path = httpExchange.getRequestURI().getPath();
            String[] splitStrings = path.split("/");
            if (splitStrings.length == 2) {
                httpExchange.sendResponseHeaders(200, 0);
                response = gson.toJson(taskManager.getPrioritizedTasks());
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                httpExchange.sendResponseHeaders(400, 0);
                response = "Неверный запрос";
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }

    class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/tasks запроса от клиента.");
            String response;
            ArrayList<Task> arrayResponse;
            String path = httpExchange.getRequestURI().getPath();
            String[] splitStrings = path.split("/");
            if (splitStrings.length == 3 && httpExchange.getRequestURI().getQuery() == null) {
                switch (method) {
                    case "GET":
                        httpExchange.sendResponseHeaders(200, 0);
                        arrayResponse = new ArrayList<>(taskManager.getTasks().values());
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
                            taskManager.updateTask(task);
                            response = "Задача обновлена";
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } else {
                            Task addTask = taskManager.addTask(task);
                            if (addTask != null) {
                                response = gson.toJson(addTask);
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            } else {
                                httpExchange.sendResponseHeaders(424, 0);
                                response = "Задача не добавлена";
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            }
                        }
                        break;
                    case "DELETE":
                        httpExchange.sendResponseHeaders(200, 0);
                        taskManager.deleteAllTasksFromSet(TaskType.TASK);
                        response = "Удаление всех Task";
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(405, 0);
                        response = "Метод не поддерживается";
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                }
            } else if (splitStrings.length == 3 && httpExchange.getRequestURI().getQuery() != null) {
                switch (method) {
                    case "DELETE":
                        httpExchange.sendResponseHeaders(200, 0);
                        int idForDelete = Integer.parseInt(httpExchange.getRequestURI().getQuery().substring(3));
                        taskManager.deleteTaskById(idForDelete);
                        response = "Удаление Task по id=" + idForDelete;
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    case "GET":
                        httpExchange.sendResponseHeaders(200, 0);
                        int idGet = Integer.parseInt(httpExchange.getRequestURI().getQuery().substring(3));
                        taskManager.getTaskById(idGet);
                        System.out.println("Получение задачи по id=" + idGet);
                        response = gson.toJson(taskManager.getTaskById(idGet));
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(405, 0);
                        response = "Метод не поддерживается";
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                }
            }
        }
    }

    class SubtaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/subtask запроса от клиента.");
            String response;
            ArrayList<Task> arrayResponse;
            String path = httpExchange.getRequestURI().getPath();
            String[] splitStrings = path.split("/");
            if (splitStrings.length == 3 && httpExchange.getRequestURI().getQuery() == null) {
                switch (method) {
                    case "GET":
                        httpExchange.sendResponseHeaders(200, 0);
                        arrayResponse = new ArrayList<>(taskManager.getSubtasks().values());
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
                            taskManager.updateSubtask(subtask);
                            response = "Задача обновлена";
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } else {
                            Subtask addTask = taskManager.addSubtask(subtask);
                            if (addTask != null) {
                                response = gson.toJson(addTask);
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            } else {
                                httpExchange.sendResponseHeaders(424, 0);
                                response = "Задача не добавлена";
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            }
                        }
                        break;
                    case "DELETE":
                        httpExchange.sendResponseHeaders(200, 0);
                        taskManager.deleteAllTasksFromSet(TaskType.SUBTASK);
                        response = "Удаление всех Subtask";
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(405, 0);
                        response = "Метод не поддерживается";
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                }
            } else if (splitStrings.length == 3 && httpExchange.getRequestURI().getQuery() != null) {
                switch (method) {
                    case "DELETE":
                        httpExchange.sendResponseHeaders(200, 0);
                        int idForDelete = Integer.parseInt(httpExchange.getRequestURI().getQuery().substring(3));
                        taskManager.deleteTaskById(idForDelete);
                        response = "Удаление Subtask по id=" + idForDelete;
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    case "GET":
                        httpExchange.sendResponseHeaders(200, 0);
                        int idGet = Integer.parseInt(httpExchange.getRequestURI().getQuery().substring(3));
                        taskManager.getTaskById(idGet);
                        System.out.println("Получение задачи по id=" + idGet);
                        response = gson.toJson(taskManager.getTaskById(idGet));
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(405, 0);
                        response = "Метод не поддерживается";
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                }
            } else if (splitStrings.length == 4 && httpExchange.getRequestURI().getQuery() != null) {
                if ("GET".equals(method)) {
                    httpExchange.sendResponseHeaders(200, 0);
                    int idGet = Integer.parseInt(httpExchange.getRequestURI().getQuery().substring(3));
                    if (taskManager.getEpics().containsKey(idGet)) {
                        Epic epic = taskManager.getEpics().get(idGet);
                        ArrayList<String> subtasks = taskManager.getTasksFromEpicTask(epic);
                        System.out.println("Получение подзадач по id epic =" + idGet);
                        response = gson.toJson(subtasks);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                } else {
                    httpExchange.sendResponseHeaders(405, 0);
                    response = "Метод не поддерживается";
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }

        }
    }


    class EpicHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " /tasks/epic запроса от клиента.");
            String response;
            ArrayList<Task> arrayResponse;
            String path = httpExchange.getRequestURI().getPath();
            String[] splitStrings = path.split("/");
            if (splitStrings.length == 3 && httpExchange.getRequestURI().getQuery() == null) {
                switch (method) {
                    case "GET":
                        httpExchange.sendResponseHeaders(200, 0);
                        arrayResponse = new ArrayList<>(taskManager.getEpics().values());
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
                        Epic epic = gson.fromJson(body, Epic.class);
                        if (epic.getId() != 0) {
                            taskManager.updateEpic(epic);
                            response = "Задача обновлена";
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } else {
                            Epic addTask = taskManager.addEpic(epic);
                            if (addTask != null) {
                                response = gson.toJson(addTask);
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            } else {
                                response = "Задача не добавлена";
                                httpExchange.sendResponseHeaders(424, 0);
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            }
                        }
                        break;
                    case "DELETE":
                        httpExchange.sendResponseHeaders(200, 0);
                        taskManager.deleteAllTasksFromSet(TaskType.EPIC);
                        response = "Удаление всех Subtask";
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(405, 0);
                        response = "Метод не поддерживается";
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                }
            } else if (splitStrings.length == 3 && httpExchange.getRequestURI().getQuery() != null) {
                switch (method) {
                    case "DELETE":
                        httpExchange.sendResponseHeaders(200, 0);
                        int idForDelete = Integer.parseInt(httpExchange.getRequestURI().getQuery().substring(3));
                        taskManager.deleteTaskById(idForDelete);
                        response = "Удаление Epic по id=" + idForDelete;
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    case "GET":
                        httpExchange.sendResponseHeaders(200, 0);
                        int idGet = Integer.parseInt(httpExchange.getRequestURI().getQuery().substring(3));
                        taskManager.getTaskById(idGet);
                        System.out.println("Получение задачи по id=" + idGet);
                        response = gson.toJson(taskManager.getTaskById(idGet));
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(405, 0);
                        response = "Метод не поддерживается";
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                }
            }
        }
    }
}





