package client.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FieldTest {

    private Position position;
    private Field field;

    @BeforeEach
    void setUp() {
        position = new Position(3, 5);
        field = new Field(position, Terrain.GRASS);
    }

    @Test
    void constructor_shouldSetPositionAndTerrain() {
        assertEquals(position, field.getPosition());
        assertEquals(Terrain.GRASS, field.getTerrain());
        assertEquals(CastleState.NO_CASTLE, field.getCastleState());
        assertEquals(PlayerState.NONE, field.getPlayerState());
        assertEquals(TreasureState.NO_TREASURE, field.getTreasureState());
    }

    @Test
    void setCastleState_shouldUpdateCastleState() {
        field.setCastleState(CastleState.PLAYER_CASTLE);

        assertEquals(CastleState.PLAYER_CASTLE, field.getCastleState());
    }

    @Test
    void setPlayerState_shouldUpdatePlayerState() {
        field.setPlayerState(PlayerState.PLAYER);

        assertEquals(PlayerState.PLAYER, field.getPlayerState());
    }

    @Test
    void isPlayerCastle_withPlayerCastle_shouldReturnTrue() {
        field.setCastleState(CastleState.PLAYER_CASTLE);

        assertTrue(field.isPlayerCastle());
    }

    @Test
    void isPlayerCastle_withNoCastle_shouldReturnFalse() {
        assertFalse(field.isPlayerCastle());
    }

    @Test
    void resetPathfinding_shouldResetPathfindingValues() {
        field.setGScore(10);
        field.setFScore(20);
        field.setCameFrom(new Field(new Position(1, 1), Terrain.GRASS));

        field.resetPathfinding();

        assertEquals(Integer.MAX_VALUE, field.getGScore());
        assertFalse(field.getCameFrom().isPresent());
    }

    @Test
    void compareTo_shouldCompareByFScore() {
        Field field1 = new Field(new Position(1, 1), Terrain.GRASS);
        Field field2 = new Field(new Position(2, 2), Terrain.GRASS);
        field1.setFScore(10);
        field2.setFScore(20);

        assertTrue(field1.compareTo(field2) < 0);
        assertTrue(field2.compareTo(field1) > 0);
    }

    @Test
    void equals_withSamePosition_shouldReturnTrue() {
        Field field1 = new Field(new Position(3, 5), Terrain.GRASS);
        Field field2 = new Field(new Position(3, 5), Terrain.WATER); // Different terrain, same position

        assertEquals(field1, field2);
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    void equals_withDifferentPosition_shouldReturnFalse() {
        Field field1 = new Field(new Position(3, 5), Terrain.GRASS);
        Field field2 = new Field(new Position(5, 3), Terrain.GRASS);

        assertNotEquals(field1, field2);
    }

    @Test
    void setCameFrom_withMockedField_shouldSetCameFromField() {
        Field mockField = Mockito.mock(Field.class);

        field.setCameFrom(mockField);

        Optional<Field> cameFrom = field.getCameFrom();
        assertTrue(cameFrom.isPresent());
        assertSame(mockField, cameFrom.get());

        verify(mockField, times(0)).getPosition();
    }
}
