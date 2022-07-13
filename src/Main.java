import manager.*;
import task.*;

public class Main {
    private static void printInfo() {
        TaskManager tasksManager = Managers.getDefault();
        System.out.println(tasksManager.getHistory());
        System.out.println("Размер истории: " + tasksManager.getHistory().size());
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        TaskManager tasksManager = Managers.getDefault();


        Epic epic1 = new Epic("epicName1", "try for check1",       //эпик + 2 подзадачи
                Task.Status.DONE);
        tasksManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                Task.Status.DONE, epic1);
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                Task.Status.NEW, epic1);
        tasksManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("thirdSubtask", "sub in epicTask1",
                Task.Status.IN_PROGRESS, epic1);
        tasksManager.addSubtask(subtask3);
        //эпик 2 пустой
        Epic epic2 = new Epic("epicName2", "try for check2", Task.Status.NEW);
        tasksManager.addEpic(epic2);
        // запрос задач
        tasksManager.getTaskById(1);
        tasksManager.getTaskById(2);
        tasksManager.getTaskById(3);
        tasksManager.getTaskById(4);
        tasksManager.getTaskById(5);
        printInfo();
        tasksManager.getTaskById(4);
        tasksManager.getTaskById(5);
        tasksManager.getTaskById(4);
        tasksManager.getTaskById(5);
        tasksManager.getTaskById(5);
        tasksManager.getTaskById(3);
        tasksManager.getTaskById(1);
        tasksManager.getTaskById(1);
        printInfo();
        tasksManager.getTaskById(2);
        tasksManager.getTaskById(2);
        tasksManager.getTaskById(3);
        tasksManager.getTaskById(4);
        tasksManager.getTaskById(5);
        tasksManager.getTaskById(4);
        tasksManager.getTaskById(5);
        tasksManager.getTaskById(4);
        printInfo();
        tasksManager.getTaskById(5);
        tasksManager.getTaskById(5);
        tasksManager.getTaskById(3);
        tasksManager.getTaskById(1);
        tasksManager.getTaskById(1);
        tasksManager.getTaskById(2);
        tasksManager.getTaskById(4);
        tasksManager.getTaskById(5);
        tasksManager.getTaskById(4);
        tasksManager.getTaskById(5);
        tasksManager.getTaskById(4);
        tasksManager.getTaskById(5);
        tasksManager.getTaskById(5);
        printInfo();
        //проверка удаления
        tasksManager.deleteTaskById(2);
        printInfo();
        tasksManager.deleteTaskById(1);
        printInfo();

        /*
        код ниже работает как проверка удаления вместо кода выше
         */

        /*tasksManager.deleteAllTasksFromSet(TaskType.SUBTASK);
        System.out.println(tasksManager.getHistory());
        System.out.println(tasksManager.getHistory().size());*/

    }
}
