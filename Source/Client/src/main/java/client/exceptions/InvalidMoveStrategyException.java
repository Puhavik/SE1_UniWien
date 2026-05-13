package client.exceptions;

public class InvalidMoveStrategyException extends RuntimeException {

    public InvalidMoveStrategyException(String message) {
        super(message);
    }

    public InvalidMoveStrategyException(String message, Throwable cause) {
        super(message, cause);
    }
}