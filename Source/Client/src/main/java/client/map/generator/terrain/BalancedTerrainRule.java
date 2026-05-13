package client.map.generator.terrain;

import client.map.Terrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;

public class BalancedTerrainRule extends AbstractTerrainGenerationRule {
    private static final Logger logger = LoggerFactory.getLogger(BalancedTerrainRule.class);
    
    public BalancedTerrainRule(Random rand) {
        super(rand);
    }
    
    @Override
    public Optional<Terrain> apply(int x, int y, int sideWater, int waterCount, int mountainCount, int grassCount,
                                  int minWaterCount, int minMountainCount, int minGrassCount) {
        logger.debug("Applying balanced terrain rule at ({}, {})", x, y);
        
        if (waterCount < minWaterCount && mountainCount < minMountainCount && grassCount < minGrassCount) {
            return selectRandom(Terrain.GRASS, Terrain.WATER, Terrain.MOUNTAIN);
        }
        
        if (waterCount >= minWaterCount && mountainCount >= minMountainCount && grassCount >= minGrassCount) {
            return selectRandom(Terrain.GRASS, Terrain.MOUNTAIN);
        }
        
        if (waterCount >= minWaterCount && mountainCount >= minMountainCount) {
            return Optional.of(Terrain.GRASS);
        }
        
        if (grassCount >= minGrassCount) {
            return selectRandom(Terrain.WATER, Terrain.MOUNTAIN);
        }
        
        if (waterCount >= minWaterCount) {
            return selectRandom(Terrain.GRASS, Terrain.MOUNTAIN);
        }
        
        return selectRandom(Terrain.GRASS, Terrain.WATER);
    }
}