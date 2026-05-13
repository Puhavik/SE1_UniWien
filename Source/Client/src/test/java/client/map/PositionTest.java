package client.map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void constructor_shouldSetCoordinates() {
        Position position = new Position(3, 5);

        assertEquals(3, position.x());
        assertEquals(5, position.y());
    }

    @Test
    void equals_withSameCoordinates_shouldReturnTrue() {
        Position position1 = new Position(3, 5);
        Position position2 = new Position(3, 5);

        assertEquals(position1, position2);
        assertEquals(position1.hashCode(), position2.hashCode());
    }

    @Test
    void equals_withDifferentCoordinates_shouldReturnFalse() {
        Position position1 = new Position(3, 5);
        Position position2 = new Position(5, 3);

        assertNotEquals(position1, position2);
    }

    @Test
    void toString_shouldIncludeCoordinates() {
        Position position = new Position(3, 5);

        String result = position.toString();

        assertTrue(result.contains("3"));
        assertTrue(result.contains("5"));
    }
}