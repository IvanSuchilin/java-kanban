package main.manager.exception;

public class TaskValidationException extends RuntimeException {

    private final String message;

    public TaskValidationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
