package org.esgi.todolist.routes;

import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.User;
import org.esgi.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApi {
    private final UserService userService;

    @Autowired
    public UserApi(UserService userService) { this.userService = userService; }

    @PostMapping("/ToDoList")
    public ResponseEntity<User> CreateToDoList(@RequestBody User user) {
        User responseUser = this.userService.createTodolist(user);
        if (responseUser == null) {
            return new ResponseEntity<User>((User) null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<User>(responseUser, HttpStatus.OK);
    }

    @PostMapping("/items")
    public ResponseEntity<User> AddItem(@RequestBody User user, @RequestBody Item item) {
        return null;
    }
}
