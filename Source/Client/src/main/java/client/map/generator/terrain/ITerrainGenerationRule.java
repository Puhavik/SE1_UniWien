package client.map.generator.terrain;

import client.map.Terrain;

import java.util.Optional;

public interface ITerrainGenerationRule {

    Optional<Terrain> apply(
            int x, int y,
            int sideWater,
            int waterCount, int mountainCount, int grassCount,
            int minWaterCount, int minMountainCount, int minGrassCount
    );
}