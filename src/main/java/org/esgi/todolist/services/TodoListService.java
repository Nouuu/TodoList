package org.esgi.todolist.services;

import org.esgi.todolist.commons.exceptions.TodoListException;
import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.ToDoList;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TodoListService {

    public void addItem(ToDoList todolist, Item item) {
        if (todolist.getItems().size() >= 10) {
            throw new TodoListException("Can't add more than 10 items in todo list");
        }

        if (!isItemValid(item)) {
            throw new TodoListException("Item not valid");
        }

        Item lastItem = todolist.getItems().get(todolist.getItems().size() - 1);
        LocalDateTime now = LocalDateTime.now();
        if (now.minusMinutes(30).isBefore(lastItem.getCreatedAt())) {
            throw new TodoListException("You need to wait 30 minutes between to tasks");
        }
        item.setCreatedAt(now);
        todolist.add(item);
    }

    public void removeItem(ToDoList todolist, int itemIndex) {

    }

    public void updateItem(ToDoList todolist, int itemIndex, Item item) {

    }

    public boolean isItemValid(Item item) {
        return true;
    }
}
