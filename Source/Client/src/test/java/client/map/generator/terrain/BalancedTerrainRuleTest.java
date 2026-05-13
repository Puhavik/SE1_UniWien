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

class BalancedTerrainRuleTest {

    private BalancedTerrainRule terrainRule;
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        mockRandom = Mockito.mock(Random.class);
        terrainRule = new BalancedTerrainRule(mockRandom);
    }

    @Test
    void constructor_shouldInitializeWithRandomInstance() {
        assertNotNull(terrainRule);
    }

    @Test
    void apply_whenAllTerrainsBelowMinimum_shouldSelectRandomFromAll() {
        when(mockRandom.nextInt(3)).thenReturn(1);

        Optional<Terrain> result = terrainRule.apply(
            0, 0, 0,
            5, 4, 20,
            7, 5, 24
        );

        assertTrue(result.isPresent());
        assertEquals(Terrain.WATER, result.get());
        verify(mockRandom).nextInt(3);
    }

    @Test
    void apply_whenAllTerrainsAboveMinimum_shouldSelectRandomFromGrassAndMountain() {
        when(mockRandom.nextInt(2)).thenReturn(1);

        Optional<Terrain> result = terrainRule.apply(
            0, 0, 0,
            10, 8, 30,
            7, 5, 24
        );

        assertTrue(result.isPresent());
        assertEquals(Terrain.MOUNTAIN, result.get());
        verify(mockRandom).nextInt(2);
    }

    @Test
    void apply_whenWaterAndMountainAboveMinimum_shouldReturnGrass() {
        Optional<Terrain> result = terrainRule.apply(
            0, 0, 0,
            10, 8, 20,
            7, 5, 24
        );
        assertTrue(result.isPresent());
        assertEquals(Terrain.GRASS, result.get());
    }

    @Test
    void apply_whenGrassAboveMinimum_shouldSelectRandomFromWaterAndMountain() {
        when(mockRandom.nextInt(2)).thenReturn(0); // Select WATER

        Optional<Terrain> result = terrainRule.apply(
            0, 0, 0,
            5, 4, 30,
            7, 5, 24
        );

        // Assert
        assertTrue(result.isPresent());
        assertEquals(Terrain.WATER, result.get());
        verify(mockRandom).nextInt(2);
    }

    @Test
    void apply_whenWaterAboveMinimum_shouldSelectRandomFromGrassAndMountain() {
        when(mockRandom.nextInt(2)).thenReturn(0); // Select GRASS
        Optional<Terrain> result = terrainRule.apply(
            0, 0, 0,
            10, 4, 20,
            7, 5, 24
        );

        assertTrue(result.isPresent());
        assertEquals(Terrain.GRASS, result.get());
        verify(mockRandom).nextInt(2);
    }

    @Test
    void apply_whenAllBelowMinimum_shouldSelectRandomFromGrassAndWater() {
        BalancedTerrainRule spyRule = Mockito.spy(new BalancedTerrainRule(mockRandom));

        spyRule.apply(
            0, 0, 0,
            5, 4, 20, // All below minimum
            7, 5, 24
        );
        verify(spyRule).apply(
            eq(0), eq(0), eq(0),
            eq(5), eq(4), eq(20),
            eq(7), eq(5), eq(24)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "5, 4, 20, 7, 5, 24, 3", // All below minimum -> select from all 3
        "10, 8, 30, 7, 5, 24, 2", // All above minimum -> select from GRASS and MOUNTAIN
        "10, 8, 20, 7, 5, 24, 0", // WATER and MOUNTAIN above minimum -> return GRASS
        "5, 4, 30, 7, 5, 24, 2", // GRASS above minimum -> select from WATER and MOUNTAIN
        "10, 4, 20, 7, 5, 24, 2", // WATER above minimum -> select from GRASS and MOUNTAIN
        "5, 4, 20, 7, 5, 24, 2"  // No terrain above minimum -> select from GRASS and WATER
    })
    void apply_withDifferentTerrainCounts_shouldReturnExpectedTerrain(
            int waterCount, int mountainCount, int grassCount,
            int minWaterCount, int minMountainCount, int minGrassCount,
            int expectedOptionsCount) {
        when(mockRandom.nextInt(anyInt())).thenReturn(0);

        Optional<Terrain> result = terrainRule.apply(
            0, 0, 0,
            waterCount, mountainCount, grassCount,
            minWaterCount, minMountainCount, minGrassCount
        );

        assertTrue(result.isPresent());
        if (expectedOptionsCount == 0) {
            assertEquals(Terrain.GRASS, result.get());
        } else {
            assertNotNull(result.get());
        }
    }
}
