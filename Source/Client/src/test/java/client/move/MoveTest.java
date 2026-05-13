package client.move;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    @Test
    void values_shouldReturnAllMoves() {
        Move[] moves = Move.values();
        
        assertEquals(4, moves.length);
        assertEquals(Move.UP, moves[0]);
        assertEquals(Move.DOWN, moves[1]);
        assertEquals(Move.LEFT, moves[2]);
        assertEquals(Move.RIGHT, moves[3]);
    }
    
    @Test
    void valueOf_withValidName_shouldReturnCorrectMove() {
        assertEquals(Move.UP, Move.valueOf("UP"));
        assertEquals(Move.DOWN, Move.valueOf("DOWN"));
        assertEquals(Move.LEFT, Move.valueOf("LEFT"));
        assertEquals(Move.RIGHT, Move.valueOf("RIGHT"));
    }
    
    @Test
    void valueOf_withInvalidName_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Move.valueOf("INVALID"));
    }
}