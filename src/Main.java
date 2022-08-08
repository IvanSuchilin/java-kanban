import manager.*;

import task.*;



public class Main {



    public static void main(String[] args) throws CloneNotSupportedException {

        TaskManager tasksManager = Managers.getDefault();

        Epic epic2 = new Epic("epicName1", "try for check1",       //эпик + 2 подзадачи

                Task.Status.DONE);

        tasksManager.addEpic(epic2);

        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                Task.Status.DONE, "16.02.2022 22:30", 60, epic2);
        tasksManager.addSubtask(subtask1);

        Task newTask5 = new Task("newTask5", "check", Task.Status.NEW, "18.02.2022 18:22", 60);   //создать-добавить задачу

        tasksManager.addTask(newTask5);

        Task newTask6 = new Task("newTask6", "check", Task.Status.NEW, "17.02.2022 15:22", 360);   //создать-добавить задачу

        tasksManager.addTask(newTask6);

        System.out.println(tasksManager.getTaskSet());


      //  boolean whatIs = tasksManager.getTasks().get(2).equals(tasksManager.getTaskSet().last());

        tasksManager.deleteTaskById(2);
        Task newTask7 = new Task("newTask7", "check", Task.Status.NEW, "22.02.2022 22:30", 60);   //создать-добавить задачу

        tasksManager.addTask(newTask7);


        Task newTask1 = new Task("newTask1", "check", Task.Status.NEW, "22.02.2022 22:22", 5);   //создать-добавить задачу

        tasksManager.addTask(newTask1);

        System.out.println(tasksManager.getTaskSet());
        Task newTask2 = new Task("newTask2", "check1", Task.Status.IN_PROGRESS, "22.02.2022 22:30", 60);

        tasksManager.addTask(newTask2);

        Task newTask3 = new Task("newTask3", "check", Task.Status.NEW, "22.02.2022 22:00", 21);   //создать-добавить задачу

        tasksManager.addTask(newTask3);

        System.out.println(tasksManager.getTaskSet());



        System.out.println("новый метод");
        System.out.println(tasksManager.getPrioritizedTasks());

        tasksManager.getTaskById(1);

        tasksManager.getTaskById(2);

        tasksManager.getTaskById(3);

        tasksManager.getTaskById(4);

        tasksManager.getTaskById(5);

        tasksManager.getTaskById(6);

        tasksManager.getTaskById(7);

        printInfo();

        tasksManager.getTaskById(4);

        tasksManager.getTaskById(5);

        tasksManager.getTaskById(3);

        tasksManager.getTaskById(6);

        tasksManager.getTaskById(5);

        tasksManager.getTaskById(7);

        tasksManager.getTaskById(1);

        tasksManager.getTaskById(2);

        printInfo();

        //проверка удаления

        //tasksManager.deleteTaskById(2);

        printInfo();

        tasksManager.deleteTaskById(3);

        printInfo();

        //дополнительная проверка на удаления сета

        tasksManager.deleteAllTasksFromSet(TaskType.TASK);

        System.out.println(tasksManager.getHistory());

        System.out.println(tasksManager.getHistory().size());

    }



    private static void printInfo() {

        TaskManager tasksManager = Managers.getDefault();

        System.out.println(tasksManager.getHistory());

        System.out.println("Размер истории: " + tasksManager.getHistory().size());

    }

}