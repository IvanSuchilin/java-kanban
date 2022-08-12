package test.manager;

import main.manager.HistoryManager;
import main.manager.InMemoryHistoryManager;
import main.task.Epic;
import main.task.Subtask;
import main.task.Task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class InMemoryHistoryManagerTest {

    HistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    @BeforeEach
    public void create() {
        Task task1 = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);
        task1.setId(1);
        inMemoryHistoryManager.add(task1);
        Task task2 = new Task("task#2", "taskForCheck", Task.Status.DONE,
                "24.02.2022 07:00", 60);
        task2.setId(2);
        inMemoryHistoryManager.add(task2);
        Epic epic1 = new Epic("epic#1", "epicForCheck", Task.Status.IN_PROGRESS);
        epic1.setId(3);
        inMemoryHistoryManager.add(epic1);
        Subtask subtask1 = new Subtask("subtask#1", "subtaskForCheck",
                Task.Status.DONE, "22.02.2022 22:30", 60, epic1);
        subtask1.setId(4);
        inMemoryHistoryManager.add(subtask1);
    }

    @Test
    void addNullInEmpty() {
        inMemoryHistoryManager.remove(1);
        inMemoryHistoryManager.remove(2);
        inMemoryHistoryManager.remove(3);
        inMemoryHistoryManager.remove(4);
        inMemoryHistoryManager.add(null);
        Assertions.assertTrue(inMemoryHistoryManager.getHistory().isEmpty());
    }

    @Test
    void addNull() {
        int sizeHistory1 = inMemoryHistoryManager.getHistory().size();
        inMemoryHistoryManager.add(null);
        int sizeHistory2 = inMemoryHistoryManager.getHistory().size();
        assertEquals(sizeHistory1, sizeHistory2);
    }

    @Test
    void addSameTask() {
        Task task1 = new Task("task#1", "taskForCheck", Task.Status.NEW,
                "24.02.2022 05:00", 60);
        task1.setId(1);
        inMemoryHistoryManager.add(task1);
        int sizeHistory1 = inMemoryHistoryManager.getHistory().size();
        inMemoryHistoryManager.add(task1);
        int sizeHistory2 = inMemoryHistoryManager.getHistory().size();
        assertEquals(sizeHistory1, sizeHistory2);
    }

    @Test
    void addTask() {
        Task task3 = new Task("task#3", "taskForCheck", Task.Status.NEW,
                "20.02.2022 05:00", 60);
        task3.setId(5);
        inMemoryHistoryManager.add(task3);
        int sizeHistory1 = inMemoryHistoryManager.getHistory().size();
        Task addedTask = inMemoryHistoryManager.getHistory().get(4);
        assertEquals(5, sizeHistory1);
        assertEquals(task3, addedTask);
    }

    @Test
    void removeFirstElement() {
        inMemoryHistoryManager.remove(1);
        int sizeHistory1 = inMemoryHistoryManager.getHistory().size();
        assertEquals(3, sizeHistory1);
    }

    @Test
    void removeLastElement() {
        inMemoryHistoryManager.remove(4);
        int sizeHistory1 = inMemoryHistoryManager.getHistory().size();
        assertEquals(3, sizeHistory1);
    }

    @Test
    void removeMiddleElement() {
        inMemoryHistoryManager.remove(2);
        int sizeHistory1 = inMemoryHistoryManager.getHistory().size();
        assertEquals(3, sizeHistory1);
    }

    @Test
    void removeAllFromList() {
        inMemoryHistoryManager.remove(1);
        inMemoryHistoryManager.remove(2);
        inMemoryHistoryManager.remove(3);
        inMemoryHistoryManager.remove(4);
        int size = inMemoryHistoryManager.getHistory().size();
        assertEquals(0, size);
    }

    @Test
    void removeHalfFromList() {
        inMemoryHistoryManager.remove(1);
        inMemoryHistoryManager.remove(2);
        int size = inMemoryHistoryManager.getHistory().size();
        assertEquals(2, size);
    }
}