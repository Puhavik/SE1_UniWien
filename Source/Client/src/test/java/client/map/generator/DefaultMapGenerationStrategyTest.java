package client.map.generator;

import client.map.Field;
import client.map.Position;
import client.map.Terrain;
import client.map.generator.terrain.ITerrainGenerationRule;
import client.map.generator.validation.IMapValidationRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DefaultMapGenerationStrategyTest {

    private DefaultMapGenerationStrategy strategy;
    private Random mockRandom;
    private ITerrainGenerationRule mockTerrainRule;
    private IMapValidationRule mockValidationRule;
    private CastlePositioner mockCastlePositioner;
    private List<ITerrainGenerationRule> terrainRules;
    private List<IMapValidationRule> validationRules;

    @BeforeEach
    void setUp() {
        mockRandom = Mockito.mock(Random.class);
        mockTerrainRule = Mockito.mock(ITerrainGenerationRule.class);
        mockValidationRule = Mockito.mock(IMapValidationRule.class);
        mockCastlePositioner = Mockito.mock(CastlePositioner.class);
        
        terrainRules = new ArrayList<>();
        terrainRules.add(mockTerrainRule);
        
        validationRules = new ArrayList<>();
        validationRules.add(mockValidationRule);
        
        when(mockTerrainRule.apply(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
            .thenReturn(Optional.of(Terrain.GRASS));
        
        when(mockValidationRule.validate(anyList())).thenReturn(true);
        
        strategy = new DefaultMapGenerationStrategy(
            mockRandom,
            Optional.of(terrainRules),
            Optional.of(validationRules),
            Optional.of(mockCastlePositioner)
        );
    }

    @Test
    void constructor_withNoArguments_shouldInitializeWithDefaultValues() {
        DefaultMapGenerationStrategy defaultStrategy = new DefaultMapGenerationStrategy();
        
        assertNotNull(defaultStrategy);
    }

    @Test
    void constructor_withRandomArgument_shouldInitializeWithRandomInstance() {
        DefaultMapGenerationStrategy randomStrategy = new DefaultMapGenerationStrategy(mockRandom);
        
        assertNotNull(randomStrategy);
    }

    @Test
    void constructor_withAllArguments_shouldInitializeWithProvidedValues() {
        assertNotNull(strategy);
    }

    @Test
    void generateMap_shouldCreateCorrectNumberOfFields() {
        List<Field> fields = strategy.generateMap();
        
        assertEquals(AbstractMapGenerator.MAP_WIDTH * AbstractMapGenerator.MAP_HEIGHT, fields.size());
    }

    @Test
    void generateMap_shouldApplyTerrainRules() {
        strategy.generateMap();
        
        verify(mockTerrainRule, atLeastOnce()).apply(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    void generateMap_shouldValidateMap() {
        strategy.generateMap();
        
        verify(mockValidationRule).validate(anyList());
    }

    @Test
    void generateMap_shouldPlaceCastlesAndPlayer() {
        strategy.generateMap();
        
        verify(mockCastlePositioner).placeCastlesAndPlayer(anyList());
    }

    @Test
    void generateMap_whenValidationFails_shouldThrowException() {
        when(mockValidationRule.validate(anyList())).thenReturn(false);
        when(mockValidationRule.getErrorMessage()).thenReturn("Validation failed");
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> strategy.generateMap());
        assertEquals("Validation failed", exception.getMessage());
    }

    @Test
    void generateMap_whenNoTerrainRuleApplies_shouldDefaultToGrass() {
        when(mockTerrainRule.apply(anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
            .thenReturn(Optional.empty());
        
        List<Field> fields = strategy.generateMap();
        
        for (Field field : fields) {
            assertEquals(Terrain.GRASS, field.getTerrain());
        }
    }

    @Test
    void generateMap_whenCastlePositionerThrowsException_shouldPropagateException() {
        doThrow(new IllegalStateException("Not enough grass fields")).when(mockCastlePositioner).placeCastlesAndPlayer(anyList());
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> strategy.generateMap());
        assertEquals("Not enough grass fields", exception.getMessage());
    }
}