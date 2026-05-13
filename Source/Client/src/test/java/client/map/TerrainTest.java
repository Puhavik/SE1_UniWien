package client.map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TerrainTest {

    @Test
    void values_shouldReturnAllTerrains() {
        Terrain[] terrains = Terrain.values();
        
        assertEquals(3, terrains.length);
        assertEquals(Terrain.GRASS, terrains[0]);
        assertEquals(Terrain.MOUNTAIN, terrains[1]);
        assertEquals(Terrain.WATER, terrains[2]);
    }
    
    @Test
    void valueOf_withValidName_shouldReturnCorrectTerrain() {
        assertEquals(Terrain.GRASS, Terrain.valueOf("GRASS"));
        assertEquals(Terrain.MOUNTAIN, Terrain.valueOf("MOUNTAIN"));
        assertEquals(Terrain.WATER, Terrain.valueOf("WATER"));
    }
    
    @Test
    void valueOf_withInvalidName_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Terrain.valueOf("INVALID"));
    }
}