package ru.systemapi.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super("Resource not found: " + message);
    }
}
