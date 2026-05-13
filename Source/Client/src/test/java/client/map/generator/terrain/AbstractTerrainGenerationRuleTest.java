package client.map.generator.terrain;

import client.map.Terrain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbstractTerrainGenerationRuleTest {

    private static class TestTerrainRule extends AbstractTerrainGenerationRule {
        public TestTerrainRule(Random rand) {
            super(rand);
        }

        @Override
        public Optional<Terrain> apply(int x, int y, int sideWater, int waterCount, int mountainCount, int grassCount, int minWaterCount, int minMountainCount, int minGrassCount) {
            return Optional.of(Terrain.GRASS);
        }

        public Optional<Terrain> testSelectRandom(Terrain... options) {
            return selectRandom(options);
        }
    }

    private TestTerrainRule terrainRule;
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        mockRandom = Mockito.mock(Random.class);
        terrainRule = new TestTerrainRule(mockRandom);
    }

    @Test
    void constructor_shouldInitializeWithRandomInstance() {
        assertNotNull(terrainRule);
    }

    @Test
    void selectRandom_withOneOption_shouldReturnThatOption() {
        when(mockRandom.nextInt(1)).thenReturn(0);
        Optional<Terrain> result = terrainRule.testSelectRandom(Terrain.GRASS);
        assertTrue(result.isPresent());
        assertEquals(Terrain.GRASS, result.get());
    }

    @Test
    void selectRandom_withMultipleOptions_shouldReturnOneOfThem() {
        when(mockRandom.nextInt(3)).thenReturn(1);

        Optional<Terrain> result = terrainRule.testSelectRandom(Terrain.GRASS, Terrain.WATER, Terrain.MOUNTAIN);

        assertTrue(result.isPresent());
        assertEquals(Terrain.WATER, result.get());
    }

    @Test
    void selectRandom_shouldUseRandomToSelectOption() {
        when(mockRandom.nextInt(3)).thenReturn(2);
        terrainRule.testSelectRandom(Terrain.GRASS, Terrain.WATER, Terrain.MOUNTAIN);
        verify(mockRandom).nextInt(3);
    }
}