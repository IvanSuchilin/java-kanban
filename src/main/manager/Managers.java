package main.manager;

final public class Managers {
    static HistoryManager historyManager = new InMemoryHistoryManager();
    static TaskManager taskManager = new InMemoryTaskManager();

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }

    public static FileBackedTasksManager getDefaultFileBackedManager() {return  new FileBackedTasksManager();}
    public static FileBackedTasksManager getDefaultFileBackedManager(String path) {return  new FileBackedTasksManager(path);}
}
