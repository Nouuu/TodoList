package org.esgi.todolist.commons.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {TodoListException.class, UserException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError badRequestException(Exception exception) {
        logger.error(exception.getMessage());
        return new ResponseError(
                HttpStatus.BAD_REQUEST.value(),
                exception.getClass().toString(),
                exception.getMessage());
    }

}
