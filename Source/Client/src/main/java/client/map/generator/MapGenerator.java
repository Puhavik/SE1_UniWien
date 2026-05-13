package client.map.generator;

import client.map.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MapGenerator {
    private static final Logger logger = LoggerFactory.getLogger(MapGenerator.class);
    private final IMapGenerationStrategy mapGenerator;


    public MapGenerator() {
        this(new Random());
    }

    public MapGenerator(Random rand) {
        logger.debug("Initializing MapGenerator with default strategy.");
        this.mapGenerator = MapGenerationStrategyFactory.createDefaultStrategy(rand);
    }

    public MapGenerator(IMapGenerationStrategy strategy) {
        logger.debug("Initializing MapGenerator with custom strategy.");
        this.mapGenerator = strategy;
    }

    public List<Field> generateMap() {
        return mapGenerator.generateMap();
    }

    public int getMapWidth() {
        return mapGenerator.getMapWidth();
    }

    public int getMapHeight() {
        return mapGenerator.getMapHeight();
    }

    public void addValidationErrorListener(ValidationErrorListener listener) {
        mapGenerator.addValidationErrorListener(listener);
    }
}
