package client.map.generator;

import client.map.Field;
import client.map.Position;
import client.map.Terrain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MapGeneratorTest {

    private IMapGenerationStrategy mockStrategy;
    private List<Field> mockFields;

    @BeforeEach
    void setUp() {
        mockStrategy = Mockito.mock(IMapGenerationStrategy.class);
        
        mockFields = new ArrayList<>();
        mockFields.add(new Field(new Position(0, 0), Terrain.GRASS));
        mockFields.add(new Field(new Position(0, 1), Terrain.WATER));
        mockFields.add(new Field(new Position(1, 0), Terrain.MOUNTAIN));
        
        when(mockStrategy.generateMap()).thenReturn(mockFields);
        when(mockStrategy.getMapWidth()).thenReturn(1);
        when(mockStrategy.getMapHeight()).thenReturn(1);
    }

    @Test
    void constructor_withNoArguments_shouldCreateDefaultStrategy() {
        MapGenerator mapGenerator = new MapGenerator();
        
        assertNotNull(mapGenerator);
        List<Field> fields = mapGenerator.generateMap();
        assertNotNull(fields);
        assertFalse(fields.isEmpty());
    }

    @Test
    void constructor_withRandomArgument_shouldCreateDefaultStrategy() {
        Random random = new Random(42);
        
        MapGenerator mapGenerator = new MapGenerator(random);
        
        assertNotNull(mapGenerator);
        List<Field> fields = mapGenerator.generateMap();
        assertNotNull(fields);
        assertFalse(fields.isEmpty());
    }

    @Test
    void constructor_withStrategyArgument_shouldUseProvidedStrategy() {
        MapGenerator mapGenerator = new MapGenerator(mockStrategy);
        
        List<Field> fields = mapGenerator.generateMap();
        assertEquals(mockFields, fields);
        verify(mockStrategy).generateMap();
    }

    @Test
    void generateMap_shouldDelegateToStrategy() {
        MapGenerator mapGenerator = new MapGenerator(mockStrategy);
        
        List<Field> fields = mapGenerator.generateMap();
        
        assertEquals(mockFields, fields);
        verify(mockStrategy).generateMap();
    }

    @Test
    void getMapWidth_shouldDelegateToStrategy() {
        MapGenerator mapGenerator = new MapGenerator(mockStrategy);
        
        int width = mapGenerator.getMapWidth();
        
        assertEquals(1, width);
        verify(mockStrategy).getMapWidth();
    }

    @Test
    void getMapHeight_shouldDelegateToStrategy() {
        MapGenerator mapGenerator = new MapGenerator(mockStrategy);
        
        int height = mapGenerator.getMapHeight();
        
        assertEquals(1, height);
        verify(mockStrategy).getMapHeight();
    }

    @Test
    void addValidationErrorListener_shouldDelegateToStrategy() {
        MapGenerator mapGenerator = new MapGenerator(mockStrategy);
        ValidationErrorListener listener = (errorType, description, methodName, className) -> {};
        
        mapGenerator.addValidationErrorListener(listener);
        
        verify(mockStrategy).addValidationErrorListener(listener);
    }
}