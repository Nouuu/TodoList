package org.esgi.todolist.commons.exceptions;

public class ResponseError {
    private final int status;
    private final String exception;
    private final String message;

    public ResponseError(int status, String exception, String message) {
        this.status = status;
        this.exception = exception;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"status\":" + status +
                ",\"exception\":\"" + exception + '\"' +
                ",\"message\":\"" + message + '\"' +
                '}';
    }
}
