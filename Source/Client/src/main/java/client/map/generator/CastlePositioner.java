package client.map.generator;

import client.map.CastleState;
import client.map.Field;
import client.map.PlayerState;
import client.map.Terrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CastlePositioner {
    private static final Logger logger = LoggerFactory.getLogger(CastlePositioner.class);

    public static final int REQUIRED_CASTLE_COUNT = 6;

    private final Random rand;

    public CastlePositioner(Random rand) {
        this.rand = rand;
    }

    public void placeCastlesAndPlayer(List<Field> fields) {
        logger.info("Setting castle positions and selecting one for the player.");

        List<Field> grassFields = fields.stream()
                .filter(field -> field.getTerrain() == Terrain.GRASS)
                .collect(Collectors.toList());

        if (grassFields.size() < REQUIRED_CASTLE_COUNT) {
            String errorMessage = "Not enough grass fields to place " + 
                                 REQUIRED_CASTLE_COUNT +
                                 " castles. Found only " + grassFields.size() + " grass fields.";
            logger.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        Collections.shuffle(grassFields, rand);

        List<Field> selectedCastleFields = new ArrayList<>(
            grassFields.subList(0, REQUIRED_CASTLE_COUNT)
        );

        for (Field castleField : selectedCastleFields) {
            castleField.setCastleState(CastleState.PLAYER_CASTLE);
        }

        Field playerStartField = selectedCastleFields.get(rand.nextInt(selectedCastleFields.size()));
        playerStartField.setPlayerState(PlayerState.PLAYER);

        logger.info("Player start position set at {} with {} castles placed.",
                   playerStartField.getPosition(), REQUIRED_CASTLE_COUNT);
    }
}
