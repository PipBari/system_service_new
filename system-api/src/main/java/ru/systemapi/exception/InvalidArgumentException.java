package ru.systemapi.exception;

public class InvalidArgumentException extends RuntimeException {

    public InvalidArgumentException(String message) {
        super("Invalid request: " + message);
    }
}
