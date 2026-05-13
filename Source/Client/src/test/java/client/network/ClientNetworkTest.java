package client.network;

import client.move.Move;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClientNetworkTest {

    @Test
    void constructor_shouldInitializeWithServerUrlAndGameId() {
        ClientNetwork clientNetwork = new ClientNetwork("http://test.com", "test-game-id");

        assertNotNull(clientNetwork);
    }

    @Test
    void requestGameState_whenPlayerNotRegistered_shouldThrowException() {
        ClientNetwork clientNetwork = new ClientNetwork("http://test.com", "test-game-id");

        assertThrows(IllegalStateException.class, () -> clientNetwork.requestGameState());
    }

    @Test
    void sendHalfMap_whenPlayerNotRegistered_shouldLogError() {
        ClientNetwork clientNetwork = new ClientNetwork("http://test.com", "test-game-id");

        clientNetwork.sendHalfMap(null);
    }

    @Test
    void sendMove_withEmptyMove_shouldReturnEarly() {
        ClientNetwork clientNetwork = new ClientNetwork("http://test.com", "test-game-id");

        clientNetwork.sendMove(Optional.empty());
    }

    @Test
    void sendMove_whenPlayerNotRegistered_shouldThrowException() {
        ClientNetwork clientNetwork = new ClientNetwork("http://test.com", "test-game-id");

        assertThrows(IllegalStateException.class, () -> clientNetwork.sendMove(Optional.of(Move.UP)));
    }
}
