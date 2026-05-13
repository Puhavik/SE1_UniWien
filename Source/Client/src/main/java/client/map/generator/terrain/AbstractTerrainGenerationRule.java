package client.map.generator.terrain;

import client.map.Terrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;

public abstract class AbstractTerrainGenerationRule implements ITerrainGenerationRule {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractTerrainGenerationRule.class);
    protected final Random rand;
    
    protected AbstractTerrainGenerationRule(Random rand) {
        this.rand = rand;
    }

    protected Optional<Terrain> selectRandom(Terrain... options) {
        Terrain selected = options[rand.nextInt(options.length)];
        logger.trace("Selected terrain: {}", selected);
        return Optional.of(selected);
    }
}