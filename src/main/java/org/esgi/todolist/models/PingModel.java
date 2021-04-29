package org.esgi.todolist.models;

public class PingModel {
    private final String name;
    private final String lastName;
    private final String message;

    public PingModel(String name, String lastName, String message) {
        this.name = name;
        this.lastName = lastName;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMessage() {
        return message;
    }


}
