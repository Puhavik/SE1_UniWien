package client.map.generator.validation;

import client.map.Field;
import client.map.Terrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CastleValidationRule implements IMapValidationRule {
    private static final Logger logger = LoggerFactory.getLogger(CastleValidationRule.class);

    public static final int REQUIRED_CASTLE_COUNT = 6;

    private String errorMessage;

    @Override
    public boolean validate(List<Field> fields) {
        List<Field> grassFields = fields.stream()
                .filter(field -> field.getTerrain() == Terrain.GRASS)
                .toList();

        if (grassFields.size() < REQUIRED_CASTLE_COUNT) {
            String message = "Not enough grass fields to place " + 
                           REQUIRED_CASTLE_COUNT + 
                           " castles. Found only " + grassFields.size() + " grass fields.";
            errorMessage = message;
            logger.error(message);
            return false;
        }

        logger.debug("Castle validation passed: {} grass fields available for {} castles", 
                    grassFields.size(), REQUIRED_CASTLE_COUNT);
        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
