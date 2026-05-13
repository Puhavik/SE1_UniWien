package client.map.generator.terrain;

import client.map.Terrain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EdgeWaterTerrainRuleTest {

    private EdgeWaterTerrainRule terrainRule;
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        mockRandom = Mockito.mock(Random.class);
        terrainRule = new EdgeWaterTerrainRule(mockRandom);
    }

    @Test
    void constructor_shouldInitializeWithRandomInstance() {
        assertNotNull(terrainRule);
    }

    @Test
    void apply_whenOnEdgeWithLowSideWater_shouldSelectRandomFromGrassAndMountain() {
        when(mockRandom.nextInt(2)).thenReturn(0);

        Optional<Terrain> result = terrainRule.apply(
                0, 3,
                1,
                5, 4, 20,
                7, 5, 24
        );

        assertTrue(result.isPresent());
        assertEquals(Terrain.GRASS, result.get());
        verify(mockRandom).nextInt(2);
    }

    @Test
    void apply_whenOnEdgeWithHighSideWater_shouldReturnEmpty() {
        Optional<Terrain> result = terrainRule.apply(
                0, 3,
                2,
                5, 4, 20,
                7, 5, 24
        );

        assertFalse(result.isPresent());
    }

    @Test
    void apply_whenNotOnEdge_shouldReturnEmpty() {
        Optional<Terrain> result = terrainRule.apply(
                3, 3, // Not on edge
                1, // Low side water
                5, 4, 20,
                7, 5, 24
        );
        assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 3, 1, true", // Left edge, low side water
            "9, 3, 1, true", // Right edge, low side water
            "5, 0, 1, true", // Top edge, low side water
            "5, 4, 1, true", // Bottom edge, low side water
            "0, 3, 2, false", // Left edge, high side water
            "5, 2, 1, false" // Not on edge, low side water
    })
    void apply_withDifferentPositionsAndSideWater_shouldReturnExpectedResult(
            int x, int y, int sideWater, boolean shouldReturnTerrain) {
        when(mockRandom.nextInt(2)).thenReturn(0);


        Optional<Terrain> result = terrainRule.apply(
                x, y,
                sideWater,
                5, 4, 20,
                7, 5, 24
        );

        assertEquals(shouldReturnTerrain, result.isPresent());
        if (shouldReturnTerrain) {
            assertEquals(Terrain.GRASS, result.get());
        }
    }
}