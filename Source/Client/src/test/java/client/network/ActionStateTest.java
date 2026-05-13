package client.network;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ActionStateTest {

    @Test
    void values_shouldReturnAllActionStates() {
        ActionState[] states = ActionState.values();
        
        assertEquals(4, states.length);
        assertEquals(ActionState.WON, states[0]);
        assertEquals(ActionState.LOST, states[1]);
        assertEquals(ActionState.ACT, states[2]);
        assertEquals(ActionState.WAIT, states[3]);
    }
    
    @Test
    void valueOf_withValidName_shouldReturnCorrectActionState() {
        assertEquals(ActionState.WON, ActionState.valueOf("WON"));
        assertEquals(ActionState.LOST, ActionState.valueOf("LOST"));
        assertEquals(ActionState.ACT, ActionState.valueOf("ACT"));
        assertEquals(ActionState.WAIT, ActionState.valueOf("WAIT"));
    }
    
    @Test
    void valueOf_withInvalidName_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> ActionState.valueOf("INVALID"));
    }
}