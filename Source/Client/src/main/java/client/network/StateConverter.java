package client.network;

import client.gameState.ClientGameState;
import client.map.ClientMap;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

public class StateConverter {
    private static final Logger logger = LoggerFactory.getLogger(StateConverter.class);

    private StateConverter() {
    }

    public static ClientGameState convertFromServer(GameState gameState, UniquePlayerIdentifier playerId) {
        Objects.requireNonNull(gameState, "GameState must not be null");
        Objects.requireNonNull(playerId, "PlayerId must not be null");

        logger.info("Converting GameState for player: {}", playerId.getUniquePlayerID());

        Optional<PlayerState> playerStateOptional = gameState.getPlayers().stream()
                .filter(player -> player.getUniquePlayerID().equals(playerId.getUniquePlayerID()))
                .findFirst();

        Optional<ActionState> actionState = playerStateOptional
                .map(PlayerState::getState)
                .map(StateConverter::switchActionState);

        boolean hasTreasure = playerStateOptional
                .map(PlayerState::hasCollectedTreasure)
                .orElse(false);

        boolean fullMapPresented = gameState.getMap().getMapNodes().size() == 100;
        logger.debug("Full map presented: {}", fullMapPresented);

        actionState.ifPresentOrElse(
                state -> logger.info("ActionState: {}, Treasure: {}", state, hasTreasure),
                () -> logger.warn("No player state found for ID: {}", playerId.getUniquePlayerID())
        );

        if (fullMapPresented) {
            ClientMap fullMap = MapConverter.convertFromServer(gameState.getMap());
            return new ClientGameState(
                    actionState.orElse(ActionState.WAIT),
                    hasTreasure,
                    true,
                    Optional.of(fullMap)
            );
        }

        return new ClientGameState(
                actionState.orElse(ActionState.WAIT),
                hasTreasure,
                false
        );
    }

    private static ActionState switchActionState(EPlayerGameState state) {
        logger.trace("Mapping server state [{}] to client ActionState", state);
        return switch (state) {
            case Lost -> ActionState.LOST;
            case Won -> ActionState.WON;
            case MustAct -> ActionState.ACT;
            case MustWait -> ActionState.WAIT;
        };
    }
}
