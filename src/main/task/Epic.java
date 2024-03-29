package main.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<Subtask> childSubtasks;

    public Epic(String name, String description, Task.Status status) {
        super(name, description, status);
        setDuration(null);
        setStartTime(null);
        childSubtasks = new ArrayList<>();
        setStatus(Status.NEW);
    }

    @Override
    public String toString() {
        return "\nEpic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() + '\'' +
                ", status=" + getStatus() + '\'' +
                Arrays.toString(new ArrayList[]{childSubtasks}) + '\'' +
                ", startTime=" + getStartTime() +
                '}';
    }

    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime endTimeFromSubtasks = null;
        for (Subtask task : childSubtasks) {
            if (endTimeFromSubtasks == null) {
                endTimeFromSubtasks = task.getEndTime();
            } else {
                if (task.getEndTime().isAfter(endTimeFromSubtasks)) {
                    endTimeFromSubtasks = task.getEndTime();
                }
            }
        }
        return endTimeFromSubtasks;
    }

    @Override
    public LocalDateTime getStartTime() {
        LocalDateTime startTime = null;
        for (Subtask task : childSubtasks) {
            task.getEndTime();
            if (startTime == null) {
                startTime = task.getStartTime();
            } else {
                if (task.getStartTime().isBefore(startTime)) {
                    startTime = task.getStartTime();
                }
            }
            setStartTime(startTime);
        }
        return startTime;
    }

    @Override
    public void changeStatus(Status newStatus) {
        setStatus(getStatus());
    }

    @Override
    public String toCsvString() {
        if (getStartTime() != null) {
            return String.join(",", Integer.toString(getId()), TaskType.EPIC.toString(), getName(),
                    getStatus().toString(), getDescription(), getStartTime().format(getFormatter()), Long.toString(getDuration().toMinutes()));
        } else {
            return String.join(",", Integer.toString(getId()), TaskType.EPIC.toString(), getName(),
                    getStatus().toString(), getDescription(), "null", "null");
        }
    }

    public ArrayList<Subtask> getChildSubtasks() {
        return childSubtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(childSubtasks, epic.childSubtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), childSubtasks);
    }

    public void setEpicDuration() {
        long epicDuration = 0;
        if (getChildSubtasks().size() == 0) {
            setStartTime(null);
            setDuration(null);
        } else {
            for (Task subtask : getChildSubtasks()) {
                epicDuration += subtask.getDuration().toMinutes();
            }
            setDuration(Duration.ofMinutes(epicDuration));
        }
    }

    public void checkStatus() {
        int doneCounter = 0;
        if (childSubtasks.size() == 0) {
            setStatus(Status.NEW);
            return;
        }
        for (Subtask task : childSubtasks) {
            if (task.getStatus().equals(Status.DONE)) {
                doneCounter++;
                setStatus(Status.IN_PROGRESS);
            } else if (task.getStatus().equals(Status.IN_PROGRESS)) {
                setStatus(Status.IN_PROGRESS);
                break;
            } else {
                if (doneCounter == 0) {
                    setStatus(Status.NEW);
                } else {
                    setStatus(Status.IN_PROGRESS);
                }
            }
        }
        if (doneCounter == this.childSubtasks.size() && doneCounter != 0) {
            setStatus(Status.DONE);
        }
    }
}
