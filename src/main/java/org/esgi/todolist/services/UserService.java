package org.esgi.todolist.services;

import org.esgi.todolist.models.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {


    public boolean isValid(User user) {
        return isValidEmail(user.getEmail())
                && StringUtils.hasText(user.getFirstname())
                && StringUtils.hasText(user.getLastname());
    }

    private boolean isValidEmail(String email) {
        return email.matches("^(.+)@(.+)$");
    }

    private User createTodolist(User user) {
        return null;
    }

    private User addItem(User user) {
        return null;
    }

}
