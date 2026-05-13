package client.map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTest {

    @Test
    void values_shouldReturnAllPlayerStates() {
        PlayerState[] states = PlayerState.values();
        
        assertEquals(4, states.length);
        assertEquals(PlayerState.PLAYER, states[0]);
        assertEquals(PlayerState.ENEMY, states[1]);
        assertEquals(PlayerState.BOTH, states[2]);
        assertEquals(PlayerState.NONE, states[3]);
    }
    
    @Test
    void valueOf_withValidName_shouldReturnCorrectPlayerState() {
        assertEquals(PlayerState.PLAYER, PlayerState.valueOf("PLAYER"));
        assertEquals(PlayerState.ENEMY, PlayerState.valueOf("ENEMY"));
        assertEquals(PlayerState.BOTH, PlayerState.valueOf("BOTH"));
        assertEquals(PlayerState.NONE, PlayerState.valueOf("NONE"));
    }
    
    @Test
    void valueOf_withInvalidName_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> PlayerState.valueOf("INVALID"));
    }
}