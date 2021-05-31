package org.esgi.todolist.services;

import org.assertj.core.api.Assertions;
import org.esgi.todolist.commons.exceptions.TodoListException;
import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.TodoList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class TodoListServiceTest {

    private final TodoListService todoListService;


    // Data to work with
    Item item1;
    Item item2;
    Item item3;
    Item item4;
    Item item5;
    TodoList todolist;

    @BeforeEach
    void setUp() {
        item1 = new Item("Item 1", "Content for item 1", LocalDateTime.now().minusMinutes(150));
        item2 = new Item("Item 2", "Content for item 2", LocalDateTime.now().minusMinutes(120));
        item3 = new Item("Item 3", "Content for item 3", LocalDateTime.now().minusMinutes(90));
        item4 = new Item("Item 4", "Content for item 4", LocalDateTime.now().minusMinutes(60));
        item5 = new Item("Item 5", "Content for item 5", LocalDateTime.now().minusMinutes(30));
        todolist = new TodoList();
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
    @DisplayName("Add item in todolist where already 10 items")
    void addItemAlreadyTenItems() {
        todolist.add(item1).add(item2).add(item3).add(item4).add(item5);

        Item newItem = new Item("new Item", "new item content");

        Assertions.assertThatThrownBy(() -> todoListService.addItem(todolist, newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Can't add more than 10 items in todo list");
    }

    @Test
    @DisplayName("Add invalid item in todolist")
    void addInvalidItem() {
        Item newItem = new Item("", "new item content");

        Assertions.assertThatThrownBy(() -> todoListService.addItem(todolist, newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Item not valid");
    }

    @Test
    @DisplayName("Add item in null todolist")
    void addItemNullList() {
        todolist = null;
        Item newItem = new Item("new Item", "new item content");

        Assertions.assertThatThrownBy(() -> todoListService.addItem(todolist, newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Todo List is null");
    }

    @Test
    @DisplayName("Remove item in null todolist")
    void removeItemNullList() {
        todolist = null;

        Assertions.assertThatThrownBy(() -> todoListService.removeItem(todolist, 0))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Todo List is null");
    }

    @Test
    @DisplayName("Remove item out of range in todolist")
    void removeItemOutOfRange() {
        Assertions.assertThatThrownBy(() -> todoListService.removeItem(todolist, -1))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Index out of range");
        Assertions.assertThatThrownBy(() -> todoListService.removeItem(todolist, 5))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Index out of range");
    }

    @Test
    @DisplayName("Remove item in todolist")
    void removeItem() {
        todoListService.removeItem(todolist, 0);

        Assertions.assertThat(
                todolist.getItems().stream().anyMatch(i -> i.getName().equals(item1.getName())))
                .isFalse();
    }

    ////

    @Test
    @DisplayName("Update with invalid item todolist")
    void updateInvalidItem() {
        Item newItem = new Item("", "new item content");

        Assertions.assertThatThrownBy(() -> todoListService.updateItem(todolist, 2, newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Item not valid");
    }

    @Test
    @DisplayName("Update item in null todolist")
    void updateItemNullList() {
        todolist = null;
        Item newItem = new Item("new Item", "new item content");

        Assertions.assertThatThrownBy(() -> todoListService.updateItem(todolist, 0, newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Todo List is null");
    }

    @Test
    @DisplayName("Update item out of range in todolist")
    void updateItemOutOfRange() {
        Item newItem = new Item("new Item", "new item content");

        Assertions.assertThatThrownBy(() -> todoListService.updateItem(todolist, -1, newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Index out of range");
        Assertions.assertThatThrownBy(() -> todoListService.updateItem(todolist, 5, newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Index out of range");
    }

    @Test
    @DisplayName("Update item in todolist")
    void updateItem() {
        Item newItem = new Item("new Item", "new item content");
        todoListService.updateItem(todolist, 1, newItem);

        Assertions.assertThat(todolist.getItems().get(1)).isEqualTo(newItem);
    }

    @Test
    @DisplayName("Is item valid with valid item")
    void isItemValid() {
        Item newItem = new Item("new Item", "new item content");

        Assertions.assertThat(todoListService.isItemValid(newItem, todolist, -1)).isTrue();
    }

    @Test
    @DisplayName("Is item valid with valid item but doublon")
    void isItemValidDoublon() {
        Item newItem = new Item("Item 5", "new item content");

        Assertions.assertThat(todoListService.isItemValid(newItem, todolist, -1)).isFalse();
    }

    @Test
    @DisplayName("Is item valid with valid item but doublon on ignored index")
    void isItemValidDoublonIgnoredIndex() {
        Item newItem = new Item("Item 5", "new item content");

        Assertions.assertThat(todoListService.isItemValid(newItem, todolist, 4)).isTrue();
    }

    @Test
    @DisplayName("Is item valid with null item")
    void isItemValidNull() {
        Assertions.assertThat(todoListService.isItemValid(null, todolist, -1)).isFalse();
    }

    @Test
    @DisplayName("Is item valid with empty item name")
    void isItemValidEmptyName() {
        Item newItem = new Item(null, "new item content");
        Assertions.assertThat(todoListService.isItemValid(newItem, todolist, -1)).isFalse();
        newItem.setName("");
        Assertions.assertThat(todoListService.isItemValid(newItem, todolist, -1)).isFalse();
        newItem.setName("            ");
        Assertions.assertThat(todoListService.isItemValid(newItem, todolist, -1)).isFalse();
    }

    @Test
    @DisplayName("Is item valid with no content")
    void isItemValidWithNoContent() {
        final Item item = new Item("test no content", "", LocalDateTime.now());
        Assertions.assertThat(todoListService.isItemValid(item, todolist, -1)).isFalse();
    }

    @Test
    @DisplayName("Is item valid with content over 1000 characters")
    void isItemValidLongContent() {
        Item newItem = new Item("new Item", "n".repeat(1001));
        Assertions.assertThat(todoListService.isItemValid(newItem, todolist, -1)).isFalse();
    }

}