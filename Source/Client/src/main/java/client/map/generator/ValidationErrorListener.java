package client.map.generator;

public interface ValidationErrorListener {
    void onValidationError(String errorType, String description, String methodName, String className);
}