package client.map.generator;

import client.map.generator.terrain.BalancedTerrainRule;
import client.map.generator.terrain.EdgeWaterTerrainRule;
import client.map.generator.terrain.ITerrainGenerationRule;
import client.map.generator.validation.CastleValidationRule;
import client.map.generator.validation.IMapValidationRule;
import client.map.generator.validation.MinimumTerrainCountRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


public class MapGenerationStrategyFactory {
    private static final Logger logger = LoggerFactory.getLogger(MapGenerationStrategyFactory.class);

    public static IMapGenerationStrategy createDefaultStrategy() {
        return createDefaultStrategy(new Random());
    }

    public static IMapGenerationStrategy createDefaultStrategy(Random rand) {
        logger.info("Creating default map generation strategy");

        List<ITerrainGenerationRule> terrainRules = new ArrayList<>();
        terrainRules.add(new EdgeWaterTerrainRule(rand));
        terrainRules.add(new BalancedTerrainRule(rand));

        List<IMapValidationRule> validationRules = new ArrayList<>();
        validationRules.add(new MinimumTerrainCountRule());
        validationRules.add(new CastleValidationRule());

        CastlePositioner castlePositioner = new CastlePositioner(rand);

        return new DefaultMapGenerationStrategy(rand, Optional.of(terrainRules), Optional.of(validationRules), Optional.of(castlePositioner));
    }
}
