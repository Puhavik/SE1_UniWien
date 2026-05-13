package client.controller;

import client.exceptions.ClientRegistrationException;
import client.exceptions.InvalidMoveStrategyException;
import client.gameState.ClientGameState;
import client.map.ClientMap;
import client.map.Field;
import client.map.Position;
import client.map.generator.MapGenerator;
import client.move.MoveGenerator;
import client.network.ActionState;
import client.network.ClientNetwork;
import client.view.ViewCLI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private static final int POLL_INTERVAL_MS = 400;

    private final ClientNetwork clientNetwork;
    private final MoveGenerator moveGenerator = new MoveGenerator();
    private final ViewCLI viewCLI = new ViewCLI();
    private final MapGenerator mapGenerator;

    private Optional<Position> treasurePosition = Optional.empty();

    private ClientGameState clientGameState;

    public ClientController(String serverBaseUrl, String gameId) throws ClientRegistrationException {
        if (serverBaseUrl == null || serverBaseUrl.isEmpty()) {
            throw new IllegalArgumentException("serverBaseUrl cannot be null or empty");
        }
        if (gameId == null || gameId.isEmpty()) {
            throw new IllegalArgumentException("gameId cannot be null or empty");
        }

        this.mapGenerator = new MapGenerator();
        this.mapGenerator.addValidationErrorListener(viewCLI::displayValidationError);
        this.clientNetwork = new ClientNetwork(serverBaseUrl, gameId);
        registerClient();
    }

    public void startPlaying() {
        playGame();
    }

    private void registerClient() throws ClientRegistrationException {
        clientNetwork.registerClient();
    }

    private void playGame() {
        while (true) {
            try {
                clientGameState = clientNetwork.requestGameState();
                ActionState currentState = clientGameState.getActionState();

                logger.info("Polled state: {}", currentState);

                if (currentState == ActionState.WON || currentState == ActionState.LOST) {
                    logger.info("Game finished with state: {}", currentState);
                    viewCLI.displayGameEndState(currentState);
                    break;
                } else if (currentState == ActionState.ACT) {
                    processActState();
                }

                TimeUnit.MILLISECONDS.sleep(POLL_INTERVAL_MS);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Polling interrupted", e);
                break;
            } catch (RuntimeException e) {
                logger.error("Unexpected error during game loop", e);
                break;
            }
        }
    }

    private void processActState() {
        try {
            if (clientGameState.isFullMapPresented()) {
                processMove();
            } else {
                processMapSubmission();
            }
        } catch (RuntimeException e) {
            logger.error("Error processing ACT state", e);
        }
    }

    private void processMove() {
        ClientMap map = clientGameState.getClientMap().orElseThrow(() -> new IllegalStateException("Client map is not available"));
        Position playerPosition = map.getPlayerPosition();
        if (treasurePosition.isPresent()) {
            clientGameState.setTreasurePosition(treasurePosition);
        }
        clientGameState.updateTreasureState(playerPosition);
        treasurePosition = clientGameState.getTreasurePosition();

        viewCLI.drawMap(map.getFields(), map.getMaxX(), map.getMaxY(), clientGameState.getTreasureStatus());

        try {
            moveGenerator.getNextMove(map, clientGameState.getTreasureStatus()).ifPresent(move -> clientNetwork.sendMove(Optional.of(move)));
        } catch (InvalidMoveStrategyException e) {
            logger.error("Invalid move strategy: {}", e.getMessage());
            viewCLI.displayValidationError("Move Strategy Error", e.getMessage(), "getNextMove", "MoveGenerator");
        }
    }

    private void processMapSubmission() {
        try {
            List<Field> listOfFields = mapGenerator.generateMap();
            viewCLI.drawMap(listOfFields, mapGenerator.getMapWidth(), mapGenerator.getMapHeight());
            clientNetwork.sendHalfMap(listOfFields);
        } catch (Exception e) {
            viewCLI.displayValidationError("Exception", e.getMessage(), "generateMap", "MapGenerator");
            throw e;
        }
    }
}
