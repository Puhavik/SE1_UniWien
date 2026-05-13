package client.map.generator.terrain;

import client.map.Terrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;

public class EdgeWaterTerrainRule extends AbstractTerrainGenerationRule {
    private static final Logger logger = LoggerFactory.getLogger(EdgeWaterTerrainRule.class);

    private static final int MAP_WIDTH = 10;
    private static final int MAP_HEIGHT = 5;

    public EdgeWaterTerrainRule(Random rand) {
        super(rand);
    }

    @Override
    public Optional<Terrain> apply(int x, int y, int sideWater, int waterCount, int mountainCount, int grassCount,
                                  int minWaterCount, int minMountainCount, int minGrassCount) {
        boolean isEdge = (x == 0 || y == 0 || x == MAP_WIDTH - 1 || y == MAP_HEIGHT - 1);

        if (isEdge && sideWater < 2) {
            logger.debug("Edge tile with low side water at ({}, {}). Avoiding water.", x, y);
            return selectRandom(Terrain.GRASS, Terrain.MOUNTAIN);
        }

        return Optional.empty();
    }
}
