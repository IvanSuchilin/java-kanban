final public class Managers {

    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
