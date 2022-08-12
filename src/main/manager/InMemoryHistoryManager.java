package main.manager;

import main.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node head;
    private Node tail;

    public void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        int id = task.getId();
        if (nodeMap.containsKey(id)) {
            remove(id);
        }
        linkLast(task);
        nodeMap.put(id, tail);
    }

    @Override
    public void remove(int id) {
          Node nodeForRemove = nodeMap.get(id);
          removeNode(nodeForRemove);
          nodeMap.remove(id);
    }

    private void removeNode(Node nodeForRemove) {
        if (nodeForRemove == null) {
            return;
        }
        if (nodeForRemove.next != null && nodeForRemove.prev != null) {
            nodeForRemove.prev.next = nodeForRemove.next;
            nodeForRemove.next.prev = nodeForRemove.prev;
        }
        if (nodeForRemove.next == null && nodeForRemove.prev != null) {
            tail = nodeForRemove.prev;
            tail.next = null;
        }
        if (nodeForRemove.next != null && nodeForRemove.prev == null) {
            head = nodeForRemove.next;
            head.prev = null;
        }
        if (nodeForRemove.next == null && nodeForRemove.prev == null) {
            head = null;
            tail = null;
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public List<Task> getTasks() {
        List<Task> tasksList = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasksList.add(node.getTask());
            node = node.next;
        }
        return tasksList;
    }

    public static class Node {
        private final Task task;
        private Node next;
        private Node prev;

        public Node(Node prev, Task task, Node next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }

        public Task getTask() {
            return task;
        }
    }
}
