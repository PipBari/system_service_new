package ru.systemapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class ExceptionHandlers {

    private static final String ERROR_STATUS = "error";
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlers.class);

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<StatusResponse> handleBadRequestException(InvalidArgumentException e) {
        var status = HttpStatus.BAD_REQUEST;
        logger.error("Bad Request Error: ", e);
        return ResponseEntity.status(status).body(new StatusResponse(ERROR_STATUS, e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StatusResponse> handleNotFoundException(ResourceNotFoundException e) {
        var status = HttpStatus.NOT_FOUND;
        logger.error("Resource Not Found: ", e);
        return ResponseEntity.status(status).body(new StatusResponse(ERROR_STATUS, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StatusResponse> handleGenericException(Exception e) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("Internal Server Error: ", e);
        return ResponseEntity.status(status).body(new StatusResponse(ERROR_STATUS, "An unexpected error occurred"));
    }
}
