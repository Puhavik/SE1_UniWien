package client.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidMoveStrategyExceptionTest {

    @Test
    void constructor_withMessage_shouldSetMessage() {
        String errorMessage = "Invalid move strategy";
        InvalidMoveStrategyException exception = new InvalidMoveStrategyException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
    
    @Test
    void constructor_withMessageAndCause_shouldSetMessageAndCause() {
        String errorMessage = "Invalid move strategy";
        Throwable cause = new RuntimeException("Original cause");
        InvalidMoveStrategyException exception = new InvalidMoveStrategyException(errorMessage, cause);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    void exceptionIsRuntimeException() {
        InvalidMoveStrategyException exception = new InvalidMoveStrategyException("Test");
        assertTrue(exception instanceof RuntimeException);
    }
}