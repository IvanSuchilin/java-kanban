package Manager;

import Task.Task;

import java.util.List;

public interface HistoryManager {

    int LIMIT = 10;

    void add(Task task) throws CloneNotSupportedException;

    List<Task> getHistory();
}
