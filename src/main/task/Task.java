package main.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class Task implements Comparable<Task> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private String name;
    private String description;
    private int id;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;


    public Task(String name, String description, Status status, String startTime, long duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    @Override
    public String toString() {
        return "\nTask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() + '\'' +
                ", status=" + getStatus() + '\'' +
                ", startTime=" + getStartTime() +
                '}';
    }

    @Override
    public int compareTo(Task o) {
        LocalDateTime startTime = getStartTime();
        if (startTime != null && o.getStartTime() != null) {
            if (startTime.isBefore(o.getStartTime())) {
                return -1;
            } else if (startTime.isAfter(o.getStartTime())) {
                return 1;
            } else return 0;
        } else if (startTime != null && o.getStartTime() == null) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) && description.equals(task.description)
                && status == task.status && Objects.equals(duration, task.duration)
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, duration, startTime);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void changeStatus(Status newStatus) {
        setStatus(newStatus);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String toCsvString() {
        return String.join(",", Integer.toString(getId()), TaskType.TASK.toString(), getName(),
                getStatus().toString(), getDescription(), getStartTime().format(formatter), Long.toString(duration.toMinutes()));
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(getDuration().toMinutes());
    }

    public enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }
}
