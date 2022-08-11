package main.manager.exception;

public class ManagerLoadException extends RuntimeException {
    private final String message;

    public ManagerLoadException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
