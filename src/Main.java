public class Main {

    public static void main(String[] args) {
        TaskManager tasksManager = new TaskManager();

        Task newTask1 = new Task("first", "check", Task.allStatus[0]);   //создать-добавить задачу
        tasksManager.addTask(newTask1);
        Task newTask2 = new Task("second", "check1", Task.allStatus[1]);
        tasksManager.addTask(newTask2);

        EpicTask epicTask1 = new EpicTask("epicName1", "try for check1",       //эпик + 2 подзадачи
                Task.allStatus[2]);
        tasksManager.addEpicTask(epicTask1);
        Subtask subtask1 = new Subtask("firstSubtask", "first sub in epicTask1",
                Task.allStatus[2], epicTask1);
        tasksManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("secondSubtask", "second sub in epicTask1",
                Task.allStatus[1], epicTask1);
        tasksManager.addSubtask(subtask2);
        //эпик + 1 подзадача
        EpicTask epicTask2 = new EpicTask("epicName2", "try for check2", Task.allStatus[0]);
        tasksManager.addEpicTask(epicTask2);
        Subtask subtask3 = new Subtask("thirdSubtask", "sub in epicTask2",
                Task.allStatus[1], epicTask2);
        tasksManager.addSubtask(subtask3);
        //печать списков задач по категории
        System.out.println(tasksManager.getTasksList());
        System.out.println(tasksManager.getSubtasksList());
        System.out.println(tasksManager.getEpicTasksList());
        //сменить статус задачи-печать
        newTask1.changeStatus(Task.allStatus[2]);
        System.out.println(newTask1.getStatus());

        subtask2.changeStatus(Task.allStatus[2]);                           //сменить статус подзадачи-печать-обновить
        System.out.println(epicTask1.getStatus());
        tasksManager.updateSubtask(subtask2);
        System.out.println(subtask2.getStatus());
        epicTask1.changeStatus(Task.allStatus[0]);
        System.out.println(epicTask1.getStatus());
        tasksManager.updateEpicTask(epicTask1);


        tasksManager.deleteTaskById(1);                                                 //удалить задачу
        System.out.println(tasksManager.getTasksList());

        System.out.println(epicTask2.getStatus());
        tasksManager.deleteTaskById(7);                                        //удалить подзадачу из эпик2 - чек статус
        System.out.println(epicTask2.getStatus());
        System.out.println(tasksManager.getTasksFromEpicTask(epicTask2));

        System.out.println(tasksManager.getSubtasksList());

        tasksManager.deleteTaskById(3);                                    //удалить эпик 1 - чек cписки подзадач и эпик
        System.out.println(tasksManager.getSubtasksList());
        System.out.println(tasksManager.getEpicTasksList());
    }
}
