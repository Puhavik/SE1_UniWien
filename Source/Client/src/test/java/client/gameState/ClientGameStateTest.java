package client.gameState;

import client.map.ClientMap;
import client.network.ActionState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClientGameStateTest {

    @Test
    void constructor_withoutClientMap_shouldSetFieldsCorrectly() {
        ClientGameState gameState = new ClientGameState(ActionState.ACT, true, false);
        assertEquals(ActionState.ACT, gameState.getActionState());
        assertTrue(gameState.getTreasureStatus());
        assertFalse(gameState.isFullMapPresented());
        assertEquals(Optional.empty(), gameState.getClientMap());
    }
    
    @Test
    void constructor_withClientMap_shouldSetFieldsCorrectly() {
        ClientMap mockMap = Mockito.mock(ClientMap.class);
        ClientGameState gameState = new ClientGameState(
            ActionState.WAIT, false, true, Optional.of(mockMap));
        
        assertEquals(ActionState.WAIT, gameState.getActionState());
        assertFalse(gameState.getTreasureStatus());
        assertTrue(gameState.isFullMapPresented());
        assertTrue(gameState.getClientMap().isPresent());
        assertSame(mockMap, gameState.getClientMap().get());
    }
    
    @Test
    void setTreasureCollected_shouldUpdateTreasureStatus() {
        ClientGameState gameState = new ClientGameState(ActionState.ACT, false, false);
        assertFalse(gameState.getTreasureStatus());
        gameState.setTreasureCollected(true);
        assertTrue(gameState.getTreasureStatus());
    }
    
    @ParameterizedTest
    @CsvSource({
        "WON, true, true",
        "LOST, false, false",
        "ACT, true, false",
        "WAIT, false, true"
    })
    void constructor_withDifferentParameters_shouldSetFieldsCorrectly(
            ActionState actionState, boolean hasCollectedTreasure, boolean fullMapPresented) {
        ClientGameState gameState = new ClientGameState(actionState, hasCollectedTreasure, fullMapPresented);
        
        assertEquals(actionState, gameState.getActionState());
        assertEquals(hasCollectedTreasure, gameState.getTreasureStatus());
        assertEquals(fullMapPresented, gameState.isFullMapPresented());
    }
}