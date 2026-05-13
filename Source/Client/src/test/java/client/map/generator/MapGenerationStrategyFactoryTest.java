package client.map.generator;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MapGenerationStrategyFactoryTest {

    @Test
    void createDefaultStrategy_withNoArguments_shouldReturnNonNullStrategy() {
        IMapGenerationStrategy strategy = MapGenerationStrategyFactory.createDefaultStrategy();
        
        assertNotNull(strategy);
        assertTrue(strategy instanceof DefaultMapGenerationStrategy);
    }

    @Test
    void createDefaultStrategy_withRandomArgument_shouldReturnNonNullStrategy() {
        Random mockRandom = Mockito.mock(Random.class);
        
        IMapGenerationStrategy strategy = MapGenerationStrategyFactory.createDefaultStrategy(mockRandom);
        
        assertNotNull(strategy);
        assertTrue(strategy instanceof DefaultMapGenerationStrategy);
    }

    @Test
    void createDefaultStrategy_shouldReturnStrategyThatCanGenerateMap() {
        IMapGenerationStrategy strategy = MapGenerationStrategyFactory.createDefaultStrategy();
        
        assertDoesNotThrow(() -> strategy.generateMap());
    }
}