package client.network;

import client.map.Terrain;
import messagesbase.messagesfromclient.ETerrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class TerrainConverter {
    private static final Logger logger = LoggerFactory.getLogger(TerrainConverter.class);

    private TerrainConverter() {
    }

    public static ETerrain convertFromClient(Terrain terrain) {
        Objects.requireNonNull(terrain, "Client terrain must not be null");

        logger.debug("Converting client terrain: {}", terrain);

        ETerrain convertedTerrain = switch (terrain) {
            case WATER -> ETerrain.Water;
            case GRASS -> ETerrain.Grass;
            case MOUNTAIN -> ETerrain.Mountain;
        };

        return convertedTerrain;
    }

    public static Terrain convertFromServer(ETerrain eTerrain) {
        Objects.requireNonNull(eTerrain, "ETerrain must not be null");

        logger.debug("Converting server terrain: {}", eTerrain);

        Terrain convertedTerrain = switch (eTerrain) {
            case Water -> Terrain.WATER;
            case Grass -> Terrain.GRASS;
            case Mountain -> Terrain.MOUNTAIN;
        };

        return convertedTerrain;
    }
}
