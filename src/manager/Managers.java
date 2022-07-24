package manager;

final public class Managers {
  //  static String path = "backUp.csv";
    static HistoryManager historyManager = new InMemoryHistoryManager();
    static TaskManager taskManager = new InMemoryTaskManager();
   // static FileBackedTasksManager backedManager = new FileBackedTasksManager(path);

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
