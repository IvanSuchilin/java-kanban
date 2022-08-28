package main.manager.exception;

public class ManagerLoadKVSException extends RuntimeException {

    private final String message;

    public ManagerLoadKVSException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
