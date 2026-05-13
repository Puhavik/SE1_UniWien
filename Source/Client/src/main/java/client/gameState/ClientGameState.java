package client.gameState;

import client.map.ClientMap;
import client.map.Field;
import client.map.Position;
import client.map.TreasureState;
import client.network.ActionState;

import java.util.Optional;

public class ClientGameState {

    private final ActionState actionState;
    private boolean hasCollectedTreasure;
    private final boolean fullMapPresented;
    private final Optional<ClientMap> clientMap;
    private Optional<Position> treasurePosition = Optional.empty();

    public ClientGameState(ActionState actionState, boolean hasCollectedTreasure, boolean fullMapPresented) {
        this(actionState, hasCollectedTreasure, fullMapPresented, Optional.empty());
    }

    public ClientGameState(ActionState actionState, boolean hasCollectedTreasure, boolean fullMapPresented, Optional<ClientMap> clientMap) {
        this.actionState = actionState;
        this.hasCollectedTreasure = hasCollectedTreasure;
        this.fullMapPresented = fullMapPresented;
        this.clientMap = clientMap;
    }

    public ActionState getActionState() {
        return actionState;
    }

    public Optional<ClientMap> getClientMap() {
        return clientMap;
    }

    public boolean isFullMapPresented() {
        return fullMapPresented;
    }

    public boolean getTreasureStatus() {
        return hasCollectedTreasure;
    }

    public void setTreasureCollected(boolean treasureCollected) {
        this.hasCollectedTreasure = treasureCollected;
    }

    public Optional<Position> getTreasurePosition() {
        return treasurePosition;
    }

    public void setTreasurePosition(Optional<Position> position) {
        this.treasurePosition = position;
    }

    public void updateTreasureState(Position playerPosition) {
        if (!clientMap.isPresent()) {
            return;
        }

        ClientMap map = clientMap.get();
        Optional<Field> playerField = map.findField(playerPosition);

        if (playerField.isPresent()) {
            Field field = playerField.get();

            if (hasCollectedTreasure) {
                if (!treasurePosition.isPresent()) {
                    treasurePosition = Optional.of(playerPosition);
                }

                if (treasurePosition.isPresent() && playerPosition.equals(treasurePosition.get())) {
                    field.setTreasureState(TreasureState.COLLECTED);
                }
            } 
            else if (field.getTreasureState() == TreasureState.NO_TREASURE) {
                field.setTreasureState(TreasureState.DISCOVERED);
                treasurePosition = Optional.of(playerPosition);
            }
        }

        if (treasurePosition.isPresent()) {
            map.findField(treasurePosition.get()).ifPresent(field -> {
                if (hasCollectedTreasure) {
                    field.setTreasureState(TreasureState.COLLECTED);
                } else if (field.getTreasureState() == TreasureState.NO_TREASURE) {
                    field.setTreasureState(TreasureState.DISCOVERED);
                }
            });
        }
    }
}
