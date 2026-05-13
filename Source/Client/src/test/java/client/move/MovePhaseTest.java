package client.move;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MovePhaseTest {

    @Test
    void values_shouldReturnAllPhases() {
        MovePhase[] phases = MovePhase.values();
        
        assertEquals(2, phases.length);
        assertEquals(MovePhase.SEARCHING, phases[0]);
        assertEquals(MovePhase.WITH_TREASURE, phases[1]);
    }
    
    @Test
    void valueOf_withValidName_shouldReturnCorrectPhase() {
        assertEquals(MovePhase.SEARCHING, MovePhase.valueOf("SEARCHING"));
        assertEquals(MovePhase.WITH_TREASURE, MovePhase.valueOf("WITH_TREASURE"));
    }
    
    @Test
    void valueOf_withInvalidName_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> MovePhase.valueOf("INVALID"));
    }
}