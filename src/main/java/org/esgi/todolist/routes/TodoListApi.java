package org.esgi.todolist.routes;

import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.TodoList;
import org.esgi.todolist.services.TodoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("todolist")
public class TodoListApi {
    private final TodoListService todoListService;

    @Autowired
    public TodoListApi(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @GetMapping("{todoListId}")
    public ResponseEntity<TodoList> GetTodoList(@PathVariable int todoListId) {
        TodoList response = this.todoListService.getTodoList(todoListId);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("{todoListId}")
    public ResponseEntity<TodoList> AddItem(@RequestBody Item item, @PathVariable int todoListId) {
        TodoList response = this.todoListService.addItem(todoListId, item);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("{todoListId}/items/{itemId}")
    public ResponseEntity<TodoList> UpdateItem(@RequestBody Item item, @PathVariable int itemId, @PathVariable int todoListId) {
        TodoList response = this.todoListService.updateItem(todoListId, itemId, item);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("{todoListId}/items/{itemId}")
    public ResponseEntity<TodoList> DeleteItem(@PathVariable int itemId, @PathVariable int todoListId) {
        TodoList response = this.todoListService.removeItem(todoListId, itemId);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{todoListId}")
    public ResponseEntity<String> DeleteTodoList(@PathVariable int todoListId) {
        this.todoListService.deleteTodoList(todoListId);
        return new ResponseEntity<>(
                "TodoList " + todoListId + " deleted",
                HttpStatus.NO_CONTENT
        );
    }
}
