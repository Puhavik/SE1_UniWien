package client.map.generator;

import client.map.Field;

import java.util.List;

public interface IMapGenerationStrategy {

    List<Field> generateMap();

    int getMapWidth();

    int getMapHeight();

    void addValidationErrorListener(ValidationErrorListener listener);
}
