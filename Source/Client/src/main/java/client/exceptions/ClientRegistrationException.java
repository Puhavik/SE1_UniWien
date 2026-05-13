package client.exceptions;

public class ClientRegistrationException extends Exception {

    public ClientRegistrationException(String message) {
        super(message);
    }

    public ClientRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}