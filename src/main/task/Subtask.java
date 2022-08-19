package main.task;

import java.util.Objects;

public class Subtask extends Task {
    private final int parentId;

    public Subtask(String name, String description, Task.Status status, String startTime, long duration, int parentId) {
        super(name, description, status, startTime, duration);
        this.parentId = parentId;
    }

    public Subtask(String name, String description, Status status, int parentId) {
        super(name, description, status);
        this.parentId = parentId;
    }

    @Override
    public String toCsvString() {
        return String.join(",", Integer.toString(getId()), TaskType.SUBTASK.toString(), getName(),
                getStatus().toString(), getDescription(),getStartTime().format(getFormatter()), Long.toString(getDuration().toMinutes()),Integer.toString(getParentId()));
    }

    @Override
    public String toString() {
        return "\nSubtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", parent=" + getParentId() + '\'' +
                ", startTime=" + getStartTime() +
                '}';
    }


    public int getParentId() {
        return parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return parentId == subtask.parentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parentId);
    }
}
