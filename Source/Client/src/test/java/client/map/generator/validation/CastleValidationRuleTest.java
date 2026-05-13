package client.map.generator.validation;

import client.map.Field;
import client.map.Position;
import client.map.Terrain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CastleValidationRuleTest {

    private CastleValidationRule validationRule;
    private List<Field> fields;

    @BeforeEach
    void setUp() {
        validationRule = new CastleValidationRule();
        fields = new ArrayList<>();
    }

    @Test
    void validate_withEnoughGrassFields_shouldReturnTrue() {
        for (int i = 0; i < CastleValidationRule.REQUIRED_CASTLE_COUNT; i++) {
            fields.add(new Field(new Position(i, 0), Terrain.GRASS));
        }

        boolean result = validationRule.validate(fields);

        assertTrue(result);
        assertNull(validationRule.getErrorMessage());
    }

    @Test
    void validate_withNotEnoughGrassFields_shouldReturnFalse() {
        for (int i = 0; i < CastleValidationRule.REQUIRED_CASTLE_COUNT - 1; i++) {
            fields.add(new Field(new Position(i, 0), Terrain.GRASS));
        }
        fields.add(new Field(new Position(CastleValidationRule.REQUIRED_CASTLE_COUNT - 1, 0), Terrain.WATER));

        boolean result = validationRule.validate(fields);

        assertFalse(result);
        assertNotNull(validationRule.getErrorMessage());
        assertTrue(validationRule.getErrorMessage().contains("Not enough grass fields"));
    }

    @ParameterizedTest
    @CsvSource({
        "0, false",
        "1, false",
        "5, false",
        "6, true",
        "7, true",
        "10, true"
    })
    void validate_withDifferentGrassFieldCounts_shouldReturnExpectedResult(int grassCount, boolean expected) {
        for (int i = 0; i < grassCount; i++) {
            fields.add(new Field(new Position(i, 0), Terrain.GRASS));
        }
        for (int i = 0; i < 5; i++) {
            fields.add(new Field(new Position(i, 1), Terrain.WATER));
            fields.add(new Field(new Position(i, 2), Terrain.MOUNTAIN));
        }

        boolean result = validationRule.validate(fields);

        assertEquals(expected, result);
        if (expected) {
            assertNull(validationRule.getErrorMessage());
        } else {
            assertNotNull(validationRule.getErrorMessage());
            assertTrue(validationRule.getErrorMessage().contains("Not enough grass fields"));
        }
    }
}