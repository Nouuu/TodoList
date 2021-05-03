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
    private final EmailSenderService emailSenderService;

    @Autowired
    public UserService(TodoListService todoListService, EmailSenderService emailSenderService) {
        this.todoListService = todoListService;
        this.emailSenderService = emailSenderService;
    }

    public boolean isValid(User user) {
        return user != null
                && isValidEmail(user.getEmail())
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
        if (user.getToDoList() != null) {
            user.createTodolist();
        }
        return user;
    }

    public User addItem(User user, Item item) {
        if (!isValid(user) || user.getToDoList() == null) {
            throw new UserException("User invalid or don't have list");
        }

        this.todoListService.addItem(user.getToDoList(), item);

        if (user.getToDoList().getItems().size() == 8) {
            emailSenderService.sendWarningMessage(user.getEmail());
        }

        return user;
    }

    public User removeItem(User user, int itemIndex) {
        if (!isValid(user) || user.getToDoList() == null) {
            throw new UserException("User invalid or don't have list");
        }

        this.todoListService.removeItem(user.getToDoList(), itemIndex);

        return user;
    }

    public User updateItem(User user, Item item, int itemIndex) {
        if (!isValid(user) || user.getToDoList() == null) {
            throw new UserException("User invalid or don't have list");
        }

        this.todoListService.updateItem(user.getToDoList(), itemIndex, item);

        return user;
    }
}
