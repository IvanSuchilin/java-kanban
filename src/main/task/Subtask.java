package main.task;

import java.util.Objects;

public class Subtask extends Task {
    private final Epic parent;

    public Subtask(String name, String description, Task.Status status, String startTime, long duration, Epic parent) {
        super(name, description, status, startTime, duration);
        this.parent = parent;
    }

    public Subtask(String name, String description, Status status, Epic parent) {
        super(name, description, status);
        this.parent = parent;
    }

    @Override
    public String toCsvString() {
        return String.join(",", Integer.toString(getId()), TaskType.SUBTASK.toString(), getName(),
                getStatus().toString(), getDescription(),getStartTime().format(getFormatter()), Long.toString(getDuration().toMinutes()),Integer.toString(getParent().getId()));
    }

    @Override
    public String toString() {
        return "\nSubtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", parent=" + getParent().getName() + '\'' +
                ", startTime=" + getStartTime() +
                '}';
    }

    public Epic getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return parent.equals(subtask.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parent);
    }
}
