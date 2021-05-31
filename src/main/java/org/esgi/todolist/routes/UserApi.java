package org.esgi.todolist.routes;

import org.esgi.todolist.models.User;
import org.esgi.todolist.models.UserWithItem;
import org.esgi.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserApi {
    private final UserService userService;

    @Autowired
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        return new ResponseEntity<>(
                userService.getUser(userId),
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return new ResponseEntity<>(
                userService.createUser(user),
                HttpStatus.OK
        );
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User updatedUser) {
        return new ResponseEntity<>(
                userService.updateUser(userId, updatedUser),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(
                "User " + userId + " deleted",
                HttpStatus.NO_CONTENT
        );
    }

    @PostMapping("/{userId}/todolist")
    public ResponseEntity<User> createToDoList(@PathVariable int userId) {
        User responseUser = this.userService.createTodolist(userId);
        return new ResponseEntity<>(responseUser, HttpStatus.CREATED);
    }
}
