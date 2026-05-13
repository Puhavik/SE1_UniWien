package client.network;

import client.map.*;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.FullMapNode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class NodeConverterTest {

    @Test
    void convertFromClient_withNullField_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> NodeConverter.convertFromClient(null));
    }

    @Test
    void convertFromClient_withGrassField_shouldReturnCorrectPlayerHalfMapNode() {
        Field field = new Field(new Position(3, 5), Terrain.GRASS);

        PlayerHalfMapNode result = NodeConverter.convertFromClient(field);

        assertEquals(3, result.getX());
        assertEquals(5, result.getY());
        assertEquals(ETerrain.Grass, result.getTerrain());
        assertFalse(result.isFortPresent());
    }

    @Test
    void convertFromClient_withPlayerCastle_shouldReturnNodeWithFortPresent() {
        Field field = new Field(new Position(3, 5), Terrain.GRASS);
        field.setCastleState(CastleState.PLAYER_CASTLE);

        PlayerHalfMapNode result = NodeConverter.convertFromClient(field);

        assertTrue(result.isFortPresent());
    }

    @Test
    void convertFromServer_withNullFullMapNode_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> NodeConverter.convertFromServer(null));
    }

    @Test
    void convertFromServer_withGrassNode_shouldReturnCorrectField() {
        FullMapNode mockNode = Mockito.mock(FullMapNode.class);
        when(mockNode.getX()).thenReturn(3);
        when(mockNode.getY()).thenReturn(5);
        when(mockNode.getFortState()).thenReturn(EFortState.NoOrUnknownFortState);
        when(mockNode.getPlayerPositionState()).thenReturn(EPlayerPositionState.NoPlayerPresent);
        when(mockNode.getTerrain()).thenReturn(ETerrain.Grass);

        Field result = NodeConverter.convertFromServer(mockNode);

        assertEquals(3, result.getPosition().x());
        assertEquals(5, result.getPosition().y());
        assertEquals(Terrain.GRASS, result.getTerrain());
        assertEquals(CastleState.NO_CASTLE, result.getCastleState());
        assertEquals(PlayerState.NONE, result.getPlayerState());
    }

    @Test
    void convertFromServer_withPlayerCastle_shouldReturnFieldWithPlayerCastle() {
        FullMapNode mockNode = Mockito.mock(FullMapNode.class);
        when(mockNode.getX()).thenReturn(3);
        when(mockNode.getY()).thenReturn(5);
        when(mockNode.getFortState()).thenReturn(EFortState.MyFortPresent);
        when(mockNode.getPlayerPositionState()).thenReturn(EPlayerPositionState.NoPlayerPresent);
        when(mockNode.getTerrain()).thenReturn(ETerrain.Grass);

        Field result = NodeConverter.convertFromServer(mockNode);

        assertEquals(CastleState.PLAYER_CASTLE, result.getCastleState());
    }

    @Test
    void convertFromServer_withEnemyCastle_shouldReturnFieldWithEnemyCastle() {
        FullMapNode mockNode = Mockito.mock(FullMapNode.class);
        when(mockNode.getX()).thenReturn(3);
        when(mockNode.getY()).thenReturn(5);
        when(mockNode.getFortState()).thenReturn(EFortState.EnemyFortPresent);
        when(mockNode.getPlayerPositionState()).thenReturn(EPlayerPositionState.NoPlayerPresent);
        when(mockNode.getTerrain()).thenReturn(ETerrain.Grass);

        Field result = NodeConverter.convertFromServer(mockNode);

        assertEquals(CastleState.ENEMY_CASTLE, result.getCastleState());
    }

    @Test
    void convertFromServer_withPlayerPosition_shouldReturnFieldWithPlayerState() {
        FullMapNode mockNode = Mockito.mock(FullMapNode.class);
        when(mockNode.getX()).thenReturn(3);
        when(mockNode.getY()).thenReturn(5);
        when(mockNode.getFortState()).thenReturn(EFortState.NoOrUnknownFortState);
        when(mockNode.getPlayerPositionState()).thenReturn(EPlayerPositionState.MyPlayerPosition);
        when(mockNode.getTerrain()).thenReturn(ETerrain.Grass);

        Field result = NodeConverter.convertFromServer(mockNode);

        assertEquals(PlayerState.PLAYER, result.getPlayerState());
    }

    @Test
    void convertFromServer_withEnemyPosition_shouldReturnFieldWithEnemyState() {
        FullMapNode mockNode = Mockito.mock(FullMapNode.class);
        when(mockNode.getX()).thenReturn(3);
        when(mockNode.getY()).thenReturn(5);
        when(mockNode.getFortState()).thenReturn(EFortState.NoOrUnknownFortState);
        when(mockNode.getPlayerPositionState()).thenReturn(EPlayerPositionState.EnemyPlayerPosition);
        when(mockNode.getTerrain()).thenReturn(ETerrain.Grass);

        Field result = NodeConverter.convertFromServer(mockNode);

        assertEquals(PlayerState.ENEMY, result.getPlayerState());
    }

    @Test
    void convertFromServer_withBothPlayerPosition_shouldReturnFieldWithBothState() {
        FullMapNode mockNode = Mockito.mock(FullMapNode.class);
        when(mockNode.getX()).thenReturn(3);
        when(mockNode.getY()).thenReturn(5);
        when(mockNode.getFortState()).thenReturn(EFortState.NoOrUnknownFortState);
        when(mockNode.getPlayerPositionState()).thenReturn(EPlayerPositionState.BothPlayerPosition);
        when(mockNode.getTerrain()).thenReturn(ETerrain.Grass);

        Field result = NodeConverter.convertFromServer(mockNode);

        assertEquals(PlayerState.BOTH, result.getPlayerState());
    }
}
