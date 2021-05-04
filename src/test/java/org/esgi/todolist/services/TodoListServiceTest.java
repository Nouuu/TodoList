package org.esgi.todolist.services;

import org.assertj.core.api.Assertions;
import org.esgi.todolist.commons.exceptions.TodoListException;
import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.ToDoList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TodoListServiceTest {

    private final TodoListService todoListService;


    // Data to work with
    Item item1;
    Item item2;
    Item item3;
    Item item4;
    Item item5;
    ToDoList todolist;

    @BeforeEach
    void setUp() {
        item1 = new Item("Item 1", "Content for item 1", LocalDateTime.now().minusMinutes(150));
        item2 = new Item("Item 2", "Content for item 2", LocalDateTime.now().minusMinutes(120));
        item3 = new Item("Item 3", "Content for item 3", LocalDateTime.now().minusMinutes(90));
        item4 = new Item("Item 4", "Content for item 4", LocalDateTime.now().minusMinutes(60));
        item5 = new Item("Item 5", "Content for item 5", LocalDateTime.now().minusMinutes(30));
        todolist = new ToDoList();
        todolist.add(item1).add(item2).add(item3).add(item4).add(item5);
    }

    @Autowired
    public TodoListServiceTest(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @Test
    @DisplayName("Add item in todolist")
    void addItem() {
        Item newItem = new Item("new Item", "new item content");
        todoListService.addItem(todolist, newItem);

        Assertions.assertThat(todolist.getItems().get(todolist.getItems().size() - 1)).isEqualTo(newItem);
    }

    @Test
    @DisplayName("Add item in todolist without waiting 30 minutes")
    void addItemWithin30Minutes() {
        item5.setCreatedAt(LocalDateTime.now().minusMinutes(29));
        Item newItem = new Item("new Item", "new item content");

        Assertions.assertThatThrownBy(() -> todoListService.addItem(todolist, newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("You need to wait 30 minutes between to tasks");
    }

    @Test
    void removeItem() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void isItemValid() {
    }
}