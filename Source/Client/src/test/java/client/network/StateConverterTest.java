package client.network;

import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.GameState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class StateConverterTest {

    @Test
    void convertFromServer_withNullGameState_shouldThrowNullPointerException() {
        UniquePlayerIdentifier playerId = Mockito.mock(UniquePlayerIdentifier.class);

        assertThrows(NullPointerException.class, () -> StateConverter.convertFromServer(null, playerId));
    }

    @Test
    void convertFromServer_withNullPlayerId_shouldThrowNullPointerException() {
        GameState gameState = Mockito.mock(GameState.class);

        assertThrows(NullPointerException.class, () -> StateConverter.convertFromServer(gameState, null));
    }
}
