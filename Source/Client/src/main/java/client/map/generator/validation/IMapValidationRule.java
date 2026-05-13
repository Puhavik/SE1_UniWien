package client.map.generator.validation;

import client.map.Field;

import java.util.List;

public interface IMapValidationRule {

    boolean validate(List<Field> fields);

    String getErrorMessage();
}