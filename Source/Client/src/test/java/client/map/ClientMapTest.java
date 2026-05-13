package client.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapTest {

    private List<Field> fields;
    private ClientMap clientMap;

    @BeforeEach
    void setUp() {
        fields = new ArrayList<>();

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                fields.add(new Field(new Position(x, y), Terrain.GRASS));
            }
        }

        Field playerField = fields.get(0); // Position(0, 0)
        playerField.setPlayerState(PlayerState.PLAYER);

        clientMap = new ClientMap(fields);
    }

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        assertEquals(fields, clientMap.getFields());
        assertEquals(2, clientMap.getMaxX());
        assertEquals(2, clientMap.getMaxY());
        assertEquals(new Position(0, 0), clientMap.getPlayerPosition());
    }

    @Test
    void findField_withExistingPosition_shouldReturnField() {
        Optional<Field> result = clientMap.findField(new Position(1, 1));

        assertTrue(result.isPresent());
        assertEquals(new Position(1, 1), result.get().getPosition());
    }

    @Test
    void findField_withNonExistingPosition_shouldReturnEmpty() {
        Optional<Field> result = clientMap.findField(new Position(5, 5));

        assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0, LEFT",
        "9, 0, LEFT",
        "10, 0, RIGHT",
        "20, 0, RIGHT"
    })
    void getMapHalf_withHorizontalMap_shouldReturnCorrectHalf(int x, int y, MapHalf expected) {
        List<Field> horizontalFields = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j <= 4; j++) {
                horizontalFields.add(new Field(new Position(i, j), Terrain.GRASS));
            }
        }
        horizontalFields.get(0).setPlayerState(PlayerState.PLAYER);
        ClientMap horizontalMap = new ClientMap(horizontalFields);

        MapHalf result = horizontalMap.getMapHalf(new Position(x, y));

        assertEquals(expected, result);
    }

    @Test
    void getMapHalf_withUnsupportedConfiguration_shouldThrowException() {
        List<Field> invalidFields = new ArrayList<>();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 6; y++) {
                invalidFields.add(new Field(new Position(x, y), Terrain.GRASS));
            }
        }
        invalidFields.get(0).setPlayerState(PlayerState.PLAYER);
        ClientMap invalidMap = new ClientMap(invalidFields);

        assertThrows(IllegalStateException.class, () -> invalidMap.getMapHalf(new Position(0, 0)));
    }

    @Test
    void calculatePlayerPosition_withNoPlayerPosition_shouldThrowException() {
        List<Field> noPlayerFields = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            noPlayerFields.add(new Field(new Position(i, 0), Terrain.GRASS));
        }

        assertThrows(IllegalStateException.class, () -> new ClientMap(noPlayerFields));
    }
}
