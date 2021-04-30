package org.esgi.todolist.models;


public class User {
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String password;

    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public static boolean isValidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);

    }

}
