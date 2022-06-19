package Manager;

import Task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> historyOfLastTenRequests = new ArrayList<>();

    @Override
    public void add(Task task) throws CloneNotSupportedException {
        Task addTask = task.clone();
        if (historyOfLastTenRequests.size() == HistoryManager.LIMIT) {
            historyOfLastTenRequests.remove(0);
        }
        historyOfLastTenRequests.add(addTask);
    }

    @Override
    public List<Task> getHistory() {
        return historyOfLastTenRequests;
    }
}
