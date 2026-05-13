package client.network;

import client.move.Move;
import messagesbase.messagesfromclient.EMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MoveConverterTest {

    private MoveConverter moveConverter;

    @BeforeEach
    void setUp() {
        moveConverter = new MoveConverter();
    }

    @Test
    void convertFromClient_withNullMove_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> moveConverter.convertFromClient(null));
    }

    @ParameterizedTest
    @CsvSource({
        "UP, Up",
        "DOWN, Down",
        "LEFT, Left",
        "RIGHT, Right"
    })
    void convertFromClient_withValidMove_shouldReturnCorrectEMove(String moveStr, String expectedEMoveStr) {
        Move move = Move.valueOf(moveStr);
        EMove expectedEMove = EMove.valueOf(expectedEMoveStr);
        
        EMove result = moveConverter.convertFromClient(move);
        
        assertEquals(expectedEMove, result);
    }
}