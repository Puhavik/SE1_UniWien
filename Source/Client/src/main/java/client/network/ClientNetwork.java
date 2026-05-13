package client.network;

import client.exceptions.ClientRegistrationException;
import client.gameState.ClientGameState;
import client.map.Field;
import client.move.Move;
import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public class ClientNetwork {
    private static final Logger logger = LoggerFactory.getLogger(ClientNetwork.class);

    private final String gameId;
    private final WebClient baseWebClient;
    private final MoveConverter moveConverter = new MoveConverter();
    private Optional<UniquePlayerIdentifier> playerId = Optional.empty();

    public ClientNetwork(String serverBaseUrl, String gameId) {
        this.gameId = gameId;
        this.baseWebClient = WebClient.builder()
                .baseUrl(serverBaseUrl + "/games")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                .build();
    }

    public void registerClient() throws ClientRegistrationException {
        if (playerId.isPresent()) {
            throw new ClientRegistrationException("Player is already registered!");
        }

        try {
            PlayerRegistration playerReg = new PlayerRegistration("Vikentiy", "Pukhaev", "pukhaevv97");
            Mono<ResponseEnvelope<UniquePlayerIdentifier>> webAccess = baseWebClient.method(HttpMethod.POST)
                    .uri("/" + gameId + "/players")
                    .body(BodyInserters.fromValue(playerReg))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();
            validateResponse(resultReg, "Client registration");

            playerId = Optional.of(resultReg.getData().orElseThrow(() -> new ClientRegistrationException("Registration failed - no valid player ID received")));
            logger.info("Client registered successfully with ID: {}", playerId.get().getUniquePlayerID());
        } catch (Exception e) {
            throw new ClientRegistrationException("Failed to register client with server", e);
        }
    }

    public ClientGameState requestGameState() {
        logger.info("Requesting game state");

        UniquePlayerIdentifier playerIdValue = playerId.orElseThrow(() -> new IllegalStateException("Player is not registered"));

        Mono<ResponseEnvelope<GameState>> webAccess = baseWebClient.method(HttpMethod.GET)
                .uri("/" + gameId + "/states/" + playerIdValue.getUniquePlayerID())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        ResponseEnvelope<GameState> requestedState = webAccess.block();
        validateResponse(requestedState, "Game state request");

        return StateConverter.convertFromServer(requestedState.getData().orElseThrow(() -> new IllegalStateException("No game state data received")), playerIdValue);
    }

    public void sendHalfMap(List<Field> fields) {
        logger.info("Sending client map to server.");

        try {
            UniquePlayerIdentifier playerIdValue = playerId.orElseThrow(() -> new IllegalStateException("Player is not registered"));
            ResponseEnvelope<?> response = makePostRequest("/" + gameId + "/halfmaps", new PlayerHalfMap(playerIdValue, MapConverter.convertFromClient(fields)));
            validateResponse(response, "Sending half map");
            logger.info("✅ Half map successfully sent to server.");
        } catch (IllegalArgumentException e) {
            logger.error("Invalid client map: {}", e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Unexpected error while sending half map: {}", e.getMessage(), e);
        }
    }

    public void sendMove(Optional<Move> moveOptional) {
        if (moveOptional.isEmpty()) {
            logger.warn("No move to send — skipping.");
            return;
        }

        Move move = moveOptional.get();
        logger.info("Sending player move: {}", move);

        UniquePlayerIdentifier playerIdValue = playerId.orElseThrow(() -> new IllegalStateException("Player is not registered"));
        PlayerMove playerMove = PlayerMove.of(playerIdValue, moveConverter.convertFromClient(move));
        ResponseEnvelope<?> response = makePostRequest("/" + gameId + "/moves", playerMove);
        validateResponse(response, "Sending player move");

        logger.info("✅ Player move successfully sent.");
    }

    private ResponseEnvelope<?> makePostRequest(String uri, Object body) {
        logger.debug("Making POST request to URI: {}", uri);
        return baseWebClient.method(HttpMethod.POST)
                .uri(uri)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(ResponseEnvelope.class)
                .block();
    }

    private void validateResponse(ResponseEnvelope<?> response, String actionDescription) {
        Optional.ofNullable(response)
                .orElseThrow(() -> new IllegalStateException(actionDescription + " failed - no response from server"));

        if (response.getState() == ERequestState.Error) {
            throw new IllegalStateException(actionDescription + " error: " + response.getExceptionMessage());
        }
    }
}
