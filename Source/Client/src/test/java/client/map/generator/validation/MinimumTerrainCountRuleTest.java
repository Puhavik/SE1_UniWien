package client.map.generator.validation;

import client.map.Field;
import client.map.Position;
import client.map.Terrain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MinimumTerrainCountRuleTest {

    private MinimumTerrainCountRule validationRule;
    private List<Field> fields;

    @BeforeEach
    void setUp() {
        validationRule = new MinimumTerrainCountRule();
        fields = new ArrayList<>();
    }

    @Test
    void validate_withEnoughOfAllTerrains_shouldReturnTrue() {
        for (int i = 0; i < MinimumTerrainCountRule.MIN_WATER_FIELDS; i++) {
            fields.add(new Field(new Position(i, 0), Terrain.WATER));
        }
        for (int i = 0; i < MinimumTerrainCountRule.MIN_MOUNTAIN_FIELDS; i++) {
            fields.add(new Field(new Position(i, 1), Terrain.MOUNTAIN));
        }
        for (int i = 0; i < MinimumTerrainCountRule.MIN_GRASS_FIELDS; i++) {
            fields.add(new Field(new Position(i, 2), Terrain.GRASS));
        }

        boolean result = validationRule.validate(fields);

        assertTrue(result);
        assertNull(validationRule.getErrorMessage());
    }

    @Test
    void validate_withNotEnoughWaterFields_shouldReturnFalse() {
        for (int i = 0; i < MinimumTerrainCountRule.MIN_WATER_FIELDS - 1; i++) {
            fields.add(new Field(new Position(i, 0), Terrain.WATER));
        }
        for (int i = 0; i < MinimumTerrainCountRule.MIN_MOUNTAIN_FIELDS; i++) {
            fields.add(new Field(new Position(i, 1), Terrain.MOUNTAIN));
        }
        for (int i = 0; i < MinimumTerrainCountRule.MIN_GRASS_FIELDS; i++) {
            fields.add(new Field(new Position(i, 2), Terrain.GRASS));
        }

        boolean result = validationRule.validate(fields);

        assertFalse(result);
        assertNotNull(validationRule.getErrorMessage());
        assertTrue(validationRule.getErrorMessage().contains("Not enough water fields"));
    }

    @Test
    void validate_withNotEnoughMountainFields_shouldReturnFalse() {
        for (int i = 0; i < MinimumTerrainCountRule.MIN_WATER_FIELDS; i++) {
            fields.add(new Field(new Position(i, 0), Terrain.WATER));
        }
        for (int i = 0; i < MinimumTerrainCountRule.MIN_MOUNTAIN_FIELDS - 1; i++) {
            fields.add(new Field(new Position(i, 1), Terrain.MOUNTAIN));
        }
        for (int i = 0; i < MinimumTerrainCountRule.MIN_GRASS_FIELDS; i++) {
            fields.add(new Field(new Position(i, 2), Terrain.GRASS));
        }

        boolean result = validationRule.validate(fields);

        assertFalse(result);
        assertNotNull(validationRule.getErrorMessage());
        assertTrue(validationRule.getErrorMessage().contains("Not enough mountain fields"));
    }

    @Test
    void validate_withNotEnoughGrassFields_shouldReturnFalse() {
        for (int i = 0; i < MinimumTerrainCountRule.MIN_WATER_FIELDS; i++) {
            fields.add(new Field(new Position(i, 0), Terrain.WATER));
        }
        for (int i = 0; i < MinimumTerrainCountRule.MIN_MOUNTAIN_FIELDS; i++) {
            fields.add(new Field(new Position(i, 1), Terrain.MOUNTAIN));
        }
        for (int i = 0; i < MinimumTerrainCountRule.MIN_GRASS_FIELDS - 1; i++) {
            fields.add(new Field(new Position(i, 2), Terrain.GRASS));
        }

        boolean result = validationRule.validate(fields);

        assertFalse(result);
        assertNotNull(validationRule.getErrorMessage());
        assertTrue(validationRule.getErrorMessage().contains("Not enough grass fields"));
    }

    @Test
    void validate_withNullFields_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> validationRule.validate(null));
    }

    static Stream<Object[]> terrainCountsProvider() {
        return Stream.of(
            new Object[] {6, 5, 24, false},  // Not enough water
            new Object[] {7, 4, 24, false},  // Not enough mountain
            new Object[] {7, 5, 23, false},  // Not enough grass
            new Object[] {7, 5, 24, true},   // Minimum required
            new Object[] {8, 6, 25, true}    // More than minimum
        );
    }

    @ParameterizedTest
    @MethodSource("terrainCountsProvider")
    void validate_withDifferentTerrainCounts_shouldReturnExpectedResult(
            int waterCount, int mountainCount, int grassCount, boolean expected) {
        for (int i = 0; i < waterCount; i++) {
            fields.add(new Field(new Position(i, 0), Terrain.WATER));
        }
        for (int i = 0; i < mountainCount; i++) {
            fields.add(new Field(new Position(i, 1), Terrain.MOUNTAIN));
        }
        for (int i = 0; i < grassCount; i++) {
            fields.add(new Field(new Position(i, 2), Terrain.GRASS));
        }

        boolean result = validationRule.validate(fields);

        assertEquals(expected, result);
    }
}