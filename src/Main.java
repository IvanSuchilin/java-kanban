import Manager.*;
import Task.*;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {
        TaskManager tasksManager = Managers.getDefault();

        Task newTask1 = new Task("first", "check", Task.Status.NEW);   //создать-добавить задачу
        tasksManager.addTask(newTask1);
        Task newTask2 = new Task("second", "check1", Task.Status.IN_PROGRESS);
        tasksManager.addTask(newTask2);
        Epic epic1 = new Epic("epicName1", "try for check1",       //эпик + 2 подзадачи
                Task.Status.DONE);
        tasksManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                Task.Status.DONE, epic1);
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                Task.Status.NEW, epic1);
        tasksManager.addSubtask(subtask2);
        tasksManager.getTaskById(3);
        //эпик + 1 подзадача
        Epic epic2 = new Epic("epicName2", "try for check2", Task.Status.NEW);
        tasksManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("thirdSubtask", "sub in epicTask2",
                Task.Status.NEW, epic2);
        tasksManager.addSubtask(subtask3);
        tasksManager.getTaskById(5);
        tasksManager.getTaskById(6);
        //печать списков задач по категории
        System.out.println("Вывод всех задач:");
        System.out.println(tasksManager.getAllTypeTasksList(TaskType.TASK));
        System.out.println();
        System.out.println("Вывод всех подзадач:");
        System.out.println(tasksManager.getAllTypeTasksList(TaskType.SUBTASK));
        System.out.println();
        System.out.println("Вывод всех эпиков:");
        System.out.println(tasksManager.getAllTypeTasksList(TaskType.EPIC));
        //сменить статус задачи-печать
        tasksManager.getTaskById(1);
        System.out.println();
        System.out.println("Смена статсуса задачи " + newTask1.getName() + " на DONE и запрос ее");
        newTask1.changeStatus(Task.Status.DONE);
        System.out.println(newTask1.getStatus());
        tasksManager.getTaskById(1);
        System.out.println();
        System.out.println("Вывод истории запросов задач:");
        System.out.println(tasksManager.getHistory());

        /*
        Суть данной проверки: обновление статуса подзадачи приводит
        к обновлению статуса эпика только после обновления самой
        подзадачи через метод мэнеджера
        */

        System.out.println();
        System.out.println("Смена статсуса подзадачи " + subtask2.getName() + " на DONE и вывод:");
        subtask2.changeStatus(Task.Status.DONE);
        System.out.println(subtask2.getStatus());
        System.out.println("Статус родителя без обновления подзадачи" + epic1.getName() + ":");
        System.out.println(epic1.getStatus());
        tasksManager.getTaskById(5);
        tasksManager.getTaskById(3);
        System.out.println();
        System.out.println("Обновление подзадачи " + subtask2.getName() + " и вывод статуса:");
        tasksManager.updateSubtask(subtask2);
        System.out.println(subtask2.getStatus());
        System.out.println("Статус родителя после обновления подзадачи" + epic1.getName() + ":");
        System.out.println(epic1.getStatus());
        tasksManager.getTaskById(3);

        /*
        Суть данной проверки: доказать зависимость статуса эпика
        от статусов своих подзадач
        */

        System.out.println();
        System.out.println("Смена статсуса эпика " + epic1.getName() + " на NEW и вывод:");
        epic1.changeStatus(Task.Status.NEW);
        System.out.println(epic1.getStatus());
        tasksManager.updateEpic(epic1);
        System.out.println("Обновление эпика " + epic1.getName() + " и вывод статуса:");
        System.out.println(epic1.getStatus());
        System.out.println();
        System.out.println("Вывод истории запросов задач:");
        System.out.println(tasksManager.getHistory());
    }
}
