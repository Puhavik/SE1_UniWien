package client.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientControllerTest {

    @Test
    void constructor_withNullServerBaseUrl_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ClientController(null, "test-game-id"));
    }

    @Test
    void constructor_withEmptyServerBaseUrl_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ClientController("", "test-game-id"));
    }

    @Test
    void constructor_withNullGameId_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ClientController("http://test.com", null));
    }

    @Test
    void constructor_withEmptyGameId_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ClientController("http://test.com", ""));
    }
}
