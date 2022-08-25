package main.manager;

final public class Managers {
    static HistoryManager historyManager = new InMemoryHistoryManager();
    static TaskManager taskManager = new InMemoryTaskManager();

    static {
        try {
            taskManager = new HTTPTaskManager("http://localhost:8078/register");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TaskManager getDefault() {
        return taskManager;
    }

    public HistoryManager getDefaultHistory() {
        return historyManager;
    }

    public static FileBackedTasksManager getDefaultFileBackedManager() {
        return new FileBackedTasksManager();
    }

    public static FileBackedTasksManager getDefaultFileBackedManager(String path) {
        return new FileBackedTasksManager(path);
    }
}
