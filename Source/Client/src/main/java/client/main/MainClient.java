package client.main;

import client.controller.ClientController;
import client.exceptions.ClientRegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainClient {

    private static final Logger logger = LoggerFactory.getLogger(MainClient.class);

    public static void main(String[] args) {

        if (args.length < 3) {
            logger.error("Usage: java MainClient <gameMode> <serverBaseUrl> <gameId>");
            return;
        }

        String serverBaseUrl = args[1];
        String gameId = args[2];

        try {
            ClientController client = new ClientController(serverBaseUrl, gameId);
            try {
                client.startPlaying();
            } catch (RuntimeException e) {
                logger.error("Exception during client execution", e);
            }
        } catch (ClientRegistrationException e) {
            logger.error("Client registration error: {}", e.getMessage());
        }
    }
}
