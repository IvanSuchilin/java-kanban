import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        TasksManager tasksManager = new TasksManager();
        Task newTask = new Task("first", "check", Task.allStatus[0]);
        tasksManager.tasks.put(newTask.getId(), newTask);

        Task newTask2 = new Task("second", "check1", Task.allStatus[0]);
        tasksManager.tasks.put(newTask2.getId(), newTask2);

        Task newTask3 = new Task("third", "check2", Task.allStatus[0]);
        tasksManager.tasks.put(newTask3.getId(), newTask3);



        Subtask subtask = new Subtask("Sub1", "subtcheck", Task.allStatus[2]);



        EpicTask newEpic = new EpicTask("firstEpic", "daffa",subtask);
        tasksManager.epicTasks.put(newEpic.getId(), newEpic);
        tasksManager.subTasks.put(Task.count, subtask);

        EpicTask newEpic2 = new EpicTask("seconEpic", "ARSDFFD");
        tasksManager.epicTasks.put(newEpic2.getId(), newEpic2);


    }
}
