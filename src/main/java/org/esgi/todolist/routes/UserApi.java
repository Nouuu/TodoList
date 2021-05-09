package org.esgi.todolist.routes;

import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.User;
import org.esgi.todolist.models.UserWithItem;
import org.esgi.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserApi {
    private final UserService userService;

    @Autowired
    public UserApi(UserService userService) { this.userService = userService; }

    @PostMapping("/ToDoList")
    public ResponseEntity<User> CreateToDoList(@RequestBody User user) {
        User responseUser = this.userService.createTodolist(user);
        return new ResponseEntity<>(responseUser, HttpStatus.CREATED);
    }

    @PostMapping("/items")
    public ResponseEntity<User> AddItem(@RequestBody UserWithItem userWithItem) {
        User responseUser = this.userService.addItem(userWithItem.user, userWithItem.item);
        return new ResponseEntity<>(responseUser, HttpStatus.CREATED);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<User> UpdateItem(@RequestBody UserWithItem userWithItem, @PathVariable int id) {
        User responseUser = this.userService.updateItem(userWithItem.user, userWithItem.item, id);
        return new ResponseEntity<>(responseUser, HttpStatus.OK);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<User> DeleteItem(@RequestBody User user, @PathVariable int id) {
        User responseUser = this.userService.removeItem(user, id);
        return new ResponseEntity<>(responseUser, HttpStatus.NO_CONTENT);
    }
}
