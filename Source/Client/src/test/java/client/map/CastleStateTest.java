package client.map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CastleStateTest {

    @Test
    void values_shouldReturnAllCastleStates() {
        CastleState[] states = CastleState.values();
        
        assertEquals(3, states.length);
        assertEquals(CastleState.PLAYER_CASTLE, states[0]);
        assertEquals(CastleState.ENEMY_CASTLE, states[1]);
        assertEquals(CastleState.NO_CASTLE, states[2]);
    }
    
    @Test
    void valueOf_withValidName_shouldReturnCorrectCastleState() {
        assertEquals(CastleState.PLAYER_CASTLE, CastleState.valueOf("PLAYER_CASTLE"));
        assertEquals(CastleState.ENEMY_CASTLE, CastleState.valueOf("ENEMY_CASTLE"));
        assertEquals(CastleState.NO_CASTLE, CastleState.valueOf("NO_CASTLE"));
    }
    
    @Test
    void valueOf_withInvalidName_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> CastleState.valueOf("INVALID"));
    }
}