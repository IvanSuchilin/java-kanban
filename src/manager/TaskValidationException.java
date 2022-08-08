package manager;

public class TaskValidationException extends RuntimeException {

    private String message;

    public TaskValidationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
