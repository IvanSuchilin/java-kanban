package main.resources;

import main.task.Task;

import java.time.LocalDateTime;
import java.util.List;

public class TimeValidator {

    public static boolean validateTime (Task task, List<Task> prioritizedList) {
        List<Task> validatorPrioritizedList = prioritizedList;
        LocalDateTime taskStartTime = task.getStartTime();
        LocalDateTime taskEndTime = task.getEndTime();
        if (validatorPrioritizedList.size() == 0) {
            return true;
        }
        if (validatorPrioritizedList.size() != 0) {
            if (taskStartTime.isAfter(validatorPrioritizedList.get(validatorPrioritizedList.size() - 1).getEndTime())) {
                return true;
            }
            if (taskEndTime.isBefore(validatorPrioritizedList.get(0).getStartTime())) {
                return true;
            }
            for (int i = 1; i < validatorPrioritizedList.size(); i++) {
                LocalDateTime StartTimeCurrent = validatorPrioritizedList.get(i).getStartTime();
                LocalDateTime EndTimePre = validatorPrioritizedList.get(i - 1).getEndTime();
                if (taskStartTime.isAfter(EndTimePre) && taskEndTime.isBefore(StartTimeCurrent)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*public static boolean validateTime (Task main.task, List<Task> prioritizedList) {
        List<Task> validatorPrioritizedList = prioritizedList;
        LocalDateTime TargetStart = main.task.getStartTime();
        LocalDateTime TargetEnd = main.task.getEndTime();
        if (validatorPrioritizedList.size() == 0) {
            return true;
        }
        for (Task CurrentTask : validatorPrioritizedList) {
            LocalDateTime CurrentStart = CurrentTask.getStartTime();
            LocalDateTime CurrentEnd = CurrentTask.getEndTime();
            if (CurrentStart.isAfter(TargetStart) && CurrentStart.isBefore(TargetEnd)) return false;
            if (CurrentEnd.isAfter(TargetStart) && CurrentEnd.isBefore(TargetEnd)) return false;
        }
        return true;
    }*/

}
