package org.esgi.todolist.commons.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseError {
    private final int status;
    private final String exception;
    private final String message;

    @JsonCreator
    public ResponseError(@JsonProperty("status") int status,
                         @JsonProperty("exception") String exception,
                         @JsonProperty("message") String message) {
        this.status = status;
        this.exception = exception;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
