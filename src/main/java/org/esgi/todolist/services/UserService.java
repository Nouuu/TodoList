package org.esgi.todolist.services;

import org.esgi.todolist.commons.exceptions.UserException;
import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {

    private final TodoListService todoListService;

    @Autowired
    public UserService(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    public boolean isValid(User user) {
        return isValidEmail(user.getEmail())
                && StringUtils.hasText(user.getFirstname())
                && StringUtils.hasText(user.getLastname())
                && StringUtils.hasLength(user.getPassword())
                && user.getPassword().length() >= 8
                && user.getPassword().length() < 40;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^(.+)@(.+)$");
    }

    public User createTodolist(User user) {
        if (!this.isValid(user)) {
            throw new UserException("User is not valid");
        }
        user.createTodolist();
        return user;
    }

    public User addItem(User user, Item item) {
        return null;
    }

    public User removeItem(User user, int itemIndex) {
        return null;
    }

    public User updateItem(User user, Item item, int itemIndex) {
        return null;
    }


}
