package client.network;

import client.map.*;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromserver.FullMapNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class NodeConverter {
    private static final Logger logger = LoggerFactory.getLogger(NodeConverter.class);

    private NodeConverter() {
    }

    public static PlayerHalfMapNode convertFromClient(Field clientField) {
        Objects.requireNonNull(clientField, "Field cannot be null");

        Position position = clientField.getPosition();
        ETerrain terrain = TerrainConverter.convertFromClient(clientField.getTerrain());

        logger.debug("Converting Field at ({}, {}) — Castle: {}, Terrain: {}",
                position.x(), position.y(), clientField.isPlayerCastle(), terrain);

        return new PlayerHalfMapNode(
                position.x(),
                position.y(),
                clientField.isPlayerCastle(),
                terrain
        );
    }

    public static Field convertFromServer(FullMapNode fullMapNode) {
        Objects.requireNonNull(fullMapNode, "FullMapNode cannot be null");

        Position position = new Position(fullMapNode.getX(), fullMapNode.getY());
        Terrain terrain = TerrainConverter.convertFromServer(fullMapNode.getTerrain());

        logger.debug("Converting FullMapNode at ({}, {}) — Fort: {}, Player: {}, Terrain: {}",
                position.x(), position.y(),
                fullMapNode.getFortState(),
                fullMapNode.getPlayerPositionState(),
                terrain
        );

        return createFieldWithStates(fullMapNode, position, terrain);
    }

    private static Field createFieldWithStates(FullMapNode fullMapNode, Position position, Terrain terrain) {
        Field field = new Field(position, terrain);

        field.setCastleState(switch (fullMapNode.getFortState()) {
            case MyFortPresent -> CastleState.PLAYER_CASTLE;
            case EnemyFortPresent -> CastleState.ENEMY_CASTLE;
            case NoOrUnknownFortState -> CastleState.NO_CASTLE;
        });

        field.setPlayerState(switch (fullMapNode.getPlayerPositionState()) {
            case MyPlayerPosition -> PlayerState.PLAYER;
            case EnemyPlayerPosition -> PlayerState.ENEMY;
            case BothPlayerPosition -> PlayerState.BOTH;
            case NoPlayerPresent -> PlayerState.NONE;
        });

        return field;
    }
}
