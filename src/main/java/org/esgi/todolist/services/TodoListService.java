package org.esgi.todolist.services;

import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.ToDoList;
import org.springframework.stereotype.Service;

@Service
public class TodoListService {

    public void addItem(ToDoList todolist, Item item) {

    }

    public void removeItem(ToDoList todolist, int itemIndex) {

    }

    public void updateItem(ToDoList todolist, int itemIndex, Item item) {

    }

    public boolean isItemValid(Item item) {
        return true;
    }
}
