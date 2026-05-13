package client.network;

import client.map.Terrain;
import messagesbase.messagesfromclient.ETerrain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class TerrainConverterTest {

    @Test
    void convertFromClient_withNullTerrain_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> TerrainConverter.convertFromClient(null));
    }

    @ParameterizedTest
    @CsvSource({
        "WATER, Water",
        "GRASS, Grass",
        "MOUNTAIN, Mountain"
    })
    void convertFromClient_withValidTerrain_shouldReturnCorrectETerrain(String terrainStr, String expectedETerrainStr) {
        Terrain terrain = Terrain.valueOf(terrainStr);
        ETerrain expectedETerrain = ETerrain.valueOf(expectedETerrainStr);
        
        ETerrain result = TerrainConverter.convertFromClient(terrain);
        
        assertEquals(expectedETerrain, result);
    }

    @Test
    void convertFromServer_withNullETerrain_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> TerrainConverter.convertFromServer(null));
    }

    @ParameterizedTest
    @CsvSource({
        "Water, WATER",
        "Grass, GRASS",
        "Mountain, MOUNTAIN"
    })
    void convertFromServer_withValidETerrain_shouldReturnCorrectTerrain(String eTerrainStr, String expectedTerrainStr) {
        ETerrain eTerrain = ETerrain.valueOf(eTerrainStr);
        Terrain expectedTerrain = Terrain.valueOf(expectedTerrainStr);
        
        Terrain result = TerrainConverter.convertFromServer(eTerrain);
        
        assertEquals(expectedTerrain, result);
    }
}