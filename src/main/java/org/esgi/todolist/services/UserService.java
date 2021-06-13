package org.esgi.todolist.services;

import org.esgi.todolist.commons.exceptions.TodoListException;
import org.esgi.todolist.commons.exceptions.UserException;
import org.esgi.todolist.models.TodoList;
import org.esgi.todolist.models.User;
import org.esgi.todolist.repositories.TodoListRepository;
import org.esgi.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserService {

    private final TodoListService todoListService;
    private final UserRepository userRepository;
    private final TodoListRepository todoListRepository;
    private final int passwordMinLength;
    private final int passwordMaxLength;

    @Autowired
    public UserService(TodoListService todoListService, UserRepository userRepository,
                       TodoListRepository todoListRepository, @Value("${user.password.min-length}") int passwordMinLength,
                       @Value("${user.password.max-length}") int passwordMaxLength) {
        this.todoListService = todoListService;
        this.userRepository = userRepository;
        this.todoListRepository = todoListRepository;
        this.passwordMinLength = passwordMinLength;
        this.passwordMaxLength = passwordMaxLength;
    }

    public boolean isValid(User user) {
        return user != null
                && isValidEmail(user.getEmail())
                && StringUtils.hasText(user.getFirstname())
                && StringUtils.hasText(user.getLastname())
                && StringUtils.hasLength(user.getPassword())
                && user.getPassword().length() >= passwordMinLength
                && user.getPassword().length() < passwordMaxLength;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^(.+)@(.+)$");
    }

    @Transactional
    public User getUser(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public User createUser(User newUser) {
        if (!isValid(newUser)) {
            throw new UserException("User is not valid");
        }
        newUser.setId(0);
        return userRepository.save(newUser);
    }

    @Transactional
    public User updateUser(int userId, User updatedUser) {
        Optional<User> userFromDb = userRepository.findById(userId);
        if (userFromDb.isEmpty()) {
            return null;
        }

        if (!isValid(updatedUser)) {
            throw new UserException("User is not valid");
        }
        updatedUser.setId(userFromDb.get().getId());
        return userRepository.save(updatedUser);
    }

    @Transactional
    public void deleteUser(int userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        if (userFromDb.isEmpty()) {
            throw new UserException("User not found on id " + userId);
        }
        User user = userFromDb.get();
        if (user.getToDoList() != null) {
            todoListService.deleteTodoList(user.getToDoList().getId());
        }
        userRepository.delete(user);
    }

    @Transactional
    public User createTodolist(int userId, TodoList todoList) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserException("User not found on id " + userId);
        }
        if (user.getToDoList() == null) {
            if (todoList != null) {
                if (todoList.getName() != null) {
                    todoList.setName(StringUtils.trimWhitespace(todoList.getName()));
                }
                if (todoList.getDescription() != null) {
                    todoList.setDescription(StringUtils.trimWhitespace(todoList.getDescription()));
                }
                if (!isValidTodoList(todoList)) {
                    throw new TodoListException("Invalid TodoList");
                }
            } else {
                todoList = new TodoList();
            }
            todoList.setUser(user);
            todoList = todoListRepository.save(todoList);
            user.setToDoList(todoList);
            return user;
        } else {
            throw new UserException("User have already a todolist");
        }
    }

    @Transactional
    public boolean isValidTodoList(TodoList todoList) {
        boolean isValid = !StringUtils.hasLength(todoList.getName()) || todoList.getName().length() <= 255;
        return isValid && (!StringUtils.hasLength(todoList.getDescription()) || todoList.getDescription().length() <= 255);
    }
}
