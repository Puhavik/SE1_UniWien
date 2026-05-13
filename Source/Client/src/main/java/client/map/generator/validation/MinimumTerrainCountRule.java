package client.map.generator.validation;

import client.map.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
public class MinimumTerrainCountRule implements IMapValidationRule {
    private static final Logger logger = LoggerFactory.getLogger(MinimumTerrainCountRule.class);

    public static final int MIN_WATER_FIELDS = 7;
    public static final int MIN_MOUNTAIN_FIELDS = 5;
    public static final int MIN_GRASS_FIELDS = 24;

    private String errorMessage;

    @Override
    public boolean validate(List<Field> fields) {
        int waterCount = 0;
        int mountainCount = 0;
        int grassCount = 0;

        for (Field field : fields) {
            switch (field.getTerrain()) {
                case WATER -> waterCount++;
                case MOUNTAIN -> mountainCount++;
                case GRASS -> grassCount++;
            }
        }

        if (waterCount < MIN_WATER_FIELDS) {
            String message = "Not enough water fields. Found " + waterCount + ", required " + MIN_WATER_FIELDS;
            errorMessage = message;
            logger.error(message);
            return false;
        }

        if (mountainCount < MIN_MOUNTAIN_FIELDS) {
            String message = "Not enough mountain fields. Found " + mountainCount + ", required " + MIN_MOUNTAIN_FIELDS;
            errorMessage = message;
            logger.error(message);
            return false;
        }

        if (grassCount < MIN_GRASS_FIELDS) {
            String message = "Not enough grass fields. Found " + grassCount + ", required " + MIN_GRASS_FIELDS;
            errorMessage = message;
            logger.error(message);
            return false;
        }

        logger.debug("Terrain validation passed: water={}, mountain={}, grass={}", 
                    waterCount, mountainCount, grassCount);
        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
