package client.map.generator;

import client.map.CastleState;
import client.map.Field;
import client.map.Position;
import client.map.Terrain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CastlePositionerTest {

    private CastlePositioner castlePositioner;
    private Random mockRandom;
    private List<Field> fields;

    @BeforeEach
    void setUp() {
        mockRandom = Mockito.mock(Random.class);
        castlePositioner = new CastlePositioner(mockRandom);
        fields = new ArrayList<>();
    }

    @Test
    void constructor_shouldInitializeWithRandomInstance() {
        assertNotNull(castlePositioner);
    }

    @Test
    void placeCastlesAndPlayer_withEnoughGrassFields_shouldPlaceCastlesAndPlayer() {
        for (int i = 0; i < CastlePositioner.REQUIRED_CASTLE_COUNT; i++) {
            fields.add(new Field(new Position(i, 0), Terrain.GRASS));
        }
        fields.add(new Field(new Position(CastlePositioner.REQUIRED_CASTLE_COUNT, 0), Terrain.WATER));
        fields.add(new Field(new Position(CastlePositioner.REQUIRED_CASTLE_COUNT + 1, 0), Terrain.MOUNTAIN));

        when(mockRandom.nextInt(CastlePositioner.REQUIRED_CASTLE_COUNT)).thenReturn(0);

        castlePositioner.placeCastlesAndPlayer(fields);

        int castleCount = 0;
        boolean playerFound = false;

        for (Field field : fields) {
            if (field.getCastleState() == CastleState.PLAYER_CASTLE) {
                castleCount++;
                assertEquals(Terrain.GRASS, field.getTerrain(), "Castles should only be placed on grass fields");
            }
            if (field.isPlayerCastle() && field.getPlayerState().toString().contains("PLAYER")) {
                playerFound = true;
            }
        }

        assertEquals(CastlePositioner.REQUIRED_CASTLE_COUNT, castleCount, "Should place exactly " + CastlePositioner.REQUIRED_CASTLE_COUNT + " castles");
        assertTrue(playerFound, "Should place the player on one of the castle fields");
    }

    @Test
    void placeCastlesAndPlayer_withNotEnoughGrassFields_shouldThrowException() {
        for (int i = 0; i < CastlePositioner.REQUIRED_CASTLE_COUNT - 1; i++) {
            fields.add(new Field(new Position(i, 0), Terrain.GRASS));
        }
        fields.add(new Field(new Position(CastlePositioner.REQUIRED_CASTLE_COUNT - 1, 0), Terrain.WATER));
        fields.add(new Field(new Position(CastlePositioner.REQUIRED_CASTLE_COUNT, 0), Terrain.MOUNTAIN));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> castlePositioner.placeCastlesAndPlayer(fields));
        assertTrue(exception.getMessage().contains("Not enough grass fields"));
    }

    @Test
    void placeCastlesAndPlayer_shouldShuffleGrassFields() {
        for (int i = 0; i < CastlePositioner.REQUIRED_CASTLE_COUNT * 2; i++) {
            fields.add(new Field(new Position(i, 0), Terrain.GRASS));
        }

        when(mockRandom.nextInt(CastlePositioner.REQUIRED_CASTLE_COUNT)).thenReturn(0);

        castlePositioner.placeCastlesAndPlayer(fields);

        verify(mockRandom, atLeastOnce()).nextInt(anyInt());
    }
}
