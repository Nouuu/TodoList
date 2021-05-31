package org.esgi.todolist.services;

import org.esgi.todolist.commons.exceptions.UserException;
import org.esgi.todolist.models.User;
import org.esgi.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserService {

    private final TodoListService todoListService;
    private final UserRepository userRepository;

    @Autowired
    public UserService(TodoListService todoListService, UserRepository userRepository) {
        this.todoListService = todoListService;
        this.userRepository = userRepository;
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
            throw new UserException("User noot found on id " + userId);
        }
        User user = userFromDb.get();
        if (user.getToDoList() != null) {
            todoListService.deleteTodoList(user.getToDoList().getId());
        }
        userRepository.delete(user);
    }

    @Transactional
    public User createTodolist(int userId) {
        Optional<User> userFromDB = userRepository.findById(userId);
        if (userFromDB.isEmpty()) {
            throw new UserException("User not found on id " + userId);
        }
        User user = userFromDB.get();

        if (user.getToDoList() == null) {
            user.createTodolist();
            return userRepository.save(user);
        } else {
            throw new UserException("User have already a todolist");
        }
    }
}
