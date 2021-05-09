package org.esgi.todolist.commons.exceptions;

public class ResponseError {
    private int status;
    private String exception;
    private String message;

    public ResponseError(int status, String exception, String message) {
        this.status = status;
        this.exception = exception;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
