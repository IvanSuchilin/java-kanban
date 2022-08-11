package main.manager;

final public class Managers {
    static main.manager.HistoryManager historyManager = new InMemoryHistoryManager();
    static TaskManager taskManager = new InMemoryTaskManager();

    public static TaskManager getDefault() {
        return taskManager;
    }
    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
