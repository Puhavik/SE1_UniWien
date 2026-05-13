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

class AbstractMapGeneratorTest {

    private static class TestMapGenerator extends AbstractMapGenerator {
        public TestMapGenerator(Random rand) {
            super(rand);
        }

        @Override
        public List<Field> generateMap() {
            List<Field> fields = new ArrayList<>();
            for (int x = 0; x < MAP_WIDTH; x++) {
                for (int y = 0; y < MAP_HEIGHT; y++) {
                    fields.add(new Field(new Position(x, y), Terrain.GRASS));
                }
            }
            return fields;
        }

        public void testNotifyValidationError(String errorType, String description, String methodName) {
            notifyValidationError(errorType, description, methodName);
        }
    }

    private TestMapGenerator mapGenerator;
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        mockRandom = Mockito.mock(Random.class);
        mapGenerator = new TestMapGenerator(mockRandom);
    }

    @Test
    void constructor_shouldInitializeWithRandomInstance() {
        assertNotNull(mapGenerator);
    }

    @Test
    void getMapWidth_shouldReturnCorrectWidth() {
        int width = mapGenerator.getMapWidth();
        
        assertEquals(AbstractMapGenerator.MAP_WIDTH - 1, width);
    }

    @Test
    void getMapHeight_shouldReturnCorrectHeight() {
        int height = mapGenerator.getMapHeight();
        
        assertEquals(AbstractMapGenerator.MAP_HEIGHT - 1, height);
    }

    @Test
    void generateMap_shouldReturnCorrectNumberOfFields() {
        List<Field> fields = mapGenerator.generateMap();
        
        assertEquals(AbstractMapGenerator.MAP_WIDTH * AbstractMapGenerator.MAP_HEIGHT, fields.size());
    }

    @Test
    void addValidationErrorListener_shouldAddListener() {
        ValidationErrorListener mockListener = Mockito.mock(ValidationErrorListener.class);
        
        mapGenerator.addValidationErrorListener(mockListener);
        mapGenerator.testNotifyValidationError("TestError", "Test description", "testMethod");
        
        verify(mockListener).onValidationError("TestError", "Test description", "testMethod", "TestMapGenerator");
    }

    @Test
    void notifyValidationError_withMultipleListeners_shouldNotifyAll() {
        ValidationErrorListener mockListener1 = Mockito.mock(ValidationErrorListener.class);
        ValidationErrorListener mockListener2 = Mockito.mock(ValidationErrorListener.class);
        
        mapGenerator.addValidationErrorListener(mockListener1);
        mapGenerator.addValidationErrorListener(mockListener2);
        mapGenerator.testNotifyValidationError("TestError", "Test description", "testMethod");
        
        verify(mockListener1).onValidationError("TestError", "Test description", "testMethod", "TestMapGenerator");
        verify(mockListener2).onValidationError("TestError", "Test description", "testMethod", "TestMapGenerator");
    }

    @Test
    void notifyValidationError_withNoListeners_shouldNotThrowException() {
        mapGenerator.testNotifyValidationError("TestError", "Test description", "testMethod");
    }
}