package org.esgi.todolist.models;


public class User {
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String password;
    private ToDoList toDoList;

    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.toDoList = null;
    }

    public void createTodolist() {
        this.toDoList = new ToDoList();
    }

    public ToDoList getToDoList() {
        return toDoList;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
