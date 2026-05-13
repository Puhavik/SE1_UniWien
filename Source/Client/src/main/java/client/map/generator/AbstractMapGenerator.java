package client.map.generator;

import client.map.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractMapGenerator implements IMapGenerationStrategy {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractMapGenerator.class);

    protected static final int MAP_WIDTH = 10;
    protected static final int MAP_HEIGHT = 5;

    protected final Random rand;
    protected final List<ValidationErrorListener> validationErrorListeners = new ArrayList<>();

    protected AbstractMapGenerator(Random rand) {
        this.rand = rand;
    }

    public abstract List<Field> generateMap();

    public int getMapWidth() {
        return MAP_WIDTH - 1;
    }

    public int getMapHeight() {
        return MAP_HEIGHT - 1;
    }

    public void addValidationErrorListener(ValidationErrorListener listener) {
        validationErrorListeners.add(listener);
    }

    protected void notifyValidationError(String errorType, String description, String methodName) {
        for (ValidationErrorListener listener : validationErrorListeners) {
            listener.onValidationError(errorType, description, methodName, getClass().getSimpleName());
        }
    }
}
