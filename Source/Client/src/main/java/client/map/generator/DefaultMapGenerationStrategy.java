package client.map.generator;

import client.map.Field;
import client.map.Position;
import client.map.Terrain;
import client.map.generator.terrain.ITerrainGenerationRule;
import client.map.generator.validation.IMapValidationRule;
import client.map.generator.validation.MinimumTerrainCountRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DefaultMapGenerationStrategy extends AbstractMapGenerator {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMapGenerationStrategy.class);

    private final List<ITerrainGenerationRule> terrainRules;
    private final List<IMapValidationRule> validationRules;
    private final CastlePositioner castlePositioner;

    private int waterFieldOnEdge = 0;
    private int waterCount = 0;
    private int mountainCount = 0;
    private int grassCount = 0;

    public DefaultMapGenerationStrategy() {
        this(new Random(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    public DefaultMapGenerationStrategy(Random rand) {
        this(rand, Optional.empty(), Optional.empty(), Optional.empty());
    }

    public DefaultMapGenerationStrategy(
            Random rand,
            Optional<List<ITerrainGenerationRule>> terrainRules,
            Optional<List<IMapValidationRule>> validationRules,
            Optional<CastlePositioner> castlePositioner) {
        super(rand);

        this.terrainRules = terrainRules.orElse(new ArrayList<>());
        this.validationRules = validationRules.orElse(new ArrayList<>());
        this.castlePositioner = castlePositioner.orElse(new CastlePositioner(rand));

        logger.debug("Initialized DefaultMapGenerationStrategy with terrain and validation rules.");
    }

    @Override
    public List<Field> generateMap() {
        List<Field> fields = new ArrayList<>();

        try {
            generateTerrain(fields);

            validateMap(fields);

            castlePositioner.placeCastlesAndPlayer(fields);

            return fields;
        } catch (Exception e) {
            if (!(e instanceof IllegalStateException && e.getMessage().contains("Not enough grass fields"))) {
                notifyValidationError("Map Generation", e.getMessage(), "generateMap");
            }
            throw e;
        }
    }

    private void generateTerrain(List<Field> fields) {
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                Terrain terrain = determineTerrain(x, y);
                updateTerrainCounts(x, y, terrain);
                addField(fields, x, y, terrain);
            }
        }
    }

    private Terrain determineTerrain(int x, int y) {
        logger.debug("Determining terrain type at ({}, {})", x, y);

        for (ITerrainGenerationRule rule : terrainRules) {
            Optional<Terrain> terrainOptional = rule.apply(
                    x, y,
                    waterFieldOnEdge,
                    waterCount, mountainCount, grassCount,
                    MinimumTerrainCountRule.MIN_WATER_FIELDS,
                    MinimumTerrainCountRule.MIN_MOUNTAIN_FIELDS,
                    MinimumTerrainCountRule.MIN_GRASS_FIELDS
            );

            if (terrainOptional.isPresent()) {
                Terrain terrain = terrainOptional.get();
                logger.trace("Terrain rule applied at ({}, {}): {}", x, y, terrain);
                return terrain;
            }
        }

        logger.trace("No rule applied at ({}, {}), defaulting to GRASS.", x, y);
        notifyValidationError("Rule Application", "No terrain rule could be applied at position (" + x + ", " + y + "), defaulting to GRASS", "determineTerrain");
        return Terrain.GRASS;
    }

    private void updateTerrainCounts(int x, int y, Terrain terrain) {
        if (isEdgeWater(x, y, terrain)) {
            waterFieldOnEdge++;
            logger.debug("Edge water detected at ({}, {}). Total side water count: {}", x, y, waterFieldOnEdge);
        }

        switch (terrain) {
            case GRASS -> grassCount++;
            case WATER -> waterCount++;
            case MOUNTAIN -> mountainCount++;
        }
    }

    private boolean isEdgeWater(int x, int y, Terrain terrain) {
        return terrain == Terrain.WATER &&
                (x == 0 || y == 0 || x == MAP_WIDTH - 1 || y == MAP_HEIGHT - 1);
    }

    private void addField(List<Field> fields, int x, int y, Terrain terrain) {
        logger.trace("Field created at ({}, {}) with terrain: {}", x, y, terrain);
        fields.add(new Field(new Position(x, y), terrain));
    }

    private void validateMap(List<Field> fields) {
        for (IMapValidationRule rule : validationRules) {
            if (!rule.validate(fields)) {
                String errorMessage = rule.getErrorMessage();
                notifyValidationError("Map Validation", errorMessage, "validateMap");
                throw new IllegalStateException(errorMessage);
            }
        }
    }
}
