package org.esgi.todolist.services;

import org.esgi.todolist.commons.exceptions.TodoListException;
import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.ToDoList;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Service
public class TodoListService {
    private final int contentMaxLength = 1000;

    public void addItem(ToDoList todolist, Item item) {
        if (todolist.getItems().size() >= 10) {
            throw new TodoListException("Can't add more than 10 items in todo list");
        }

        if (!isItemValid(item, todolist, -1)) {
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
        if (todolist == null) {
            throw new TodoListException("Todo List is null");
        }
        if (itemIndex < 0 || itemIndex >= todolist.getItems().size()) {
            throw new TodoListException("Index out of range");
        }

        todolist.remove(itemIndex);
    }

    public void updateItem(ToDoList todolist, int itemIndex, Item item) {
        if (todolist == null) {
            throw new TodoListException("Todo List is null");
        }
        if (itemIndex < 0 || itemIndex >= todolist.getItems().size()) {
            throw new TodoListException("Index out of range");
        }
        if (!isItemValid(item, todolist, itemIndex)) {
            throw new TodoListException("Invalid item");
        }
        todolist.getItems().set(itemIndex, item);
    }

    public boolean isItemValid(Item item, ToDoList toDoList, int ignoreIndex) {
        if (!StringUtils.hasText(item.getName())) {
            return false;
        }

        boolean isDoublon = IntStream.range(0, toDoList.getItems().size())
                .anyMatch(index -> toDoList.getItems().get(index).getName().equals(item.getName())
                        && index != ignoreIndex);
        if (isDoublon) {
            return false;
        }
        if (StringUtils.hasText(item.getContent()) && item.getContent().length() > contentMaxLength) {
            return false;
        }
        return true;
    }
}
