package client.map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MapHalfTest {

    @Test
    void values_shouldReturnAllMapHalves() {
        MapHalf[] halves = MapHalf.values();
        
        assertEquals(4, halves.length);
        assertEquals(MapHalf.LEFT, halves[0]);
        assertEquals(MapHalf.RIGHT, halves[1]);
        assertEquals(MapHalf.TOP, halves[2]);
        assertEquals(MapHalf.BOTTOM, halves[3]);
    }
    
    @Test
    void valueOf_withValidName_shouldReturnCorrectMapHalf() {
        assertEquals(MapHalf.LEFT, MapHalf.valueOf("LEFT"));
        assertEquals(MapHalf.RIGHT, MapHalf.valueOf("RIGHT"));
        assertEquals(MapHalf.TOP, MapHalf.valueOf("TOP"));
        assertEquals(MapHalf.BOTTOM, MapHalf.valueOf("BOTTOM"));
    }
    
    @Test
    void valueOf_withInvalidName_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> MapHalf.valueOf("INVALID"));
    }
}