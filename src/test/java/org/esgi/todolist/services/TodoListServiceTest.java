package org.esgi.todolist.services;

import org.assertj.core.api.Assertions;
import org.esgi.todolist.commons.exceptions.TodoListException;
import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.TodoList;
import org.esgi.todolist.repositories.ItemRepository;
import org.esgi.todolist.repositories.TodoListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class TodoListServiceTest {

    private final TodoListService todoListService;
    private final TodoListRepository todoListRepository;
    private final ItemRepository itemRepository;


    // Data to work with
    Item item1;
    Item item2;
    Item item3;
    Item item4;
    Item item5;
    TodoList todolist;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        todoListRepository.deleteAll();
        todolist = new TodoList();
        todolist = todoListRepository.save(todolist);

        item1 = new Item("Item 1", "Content for item 1", LocalDateTime.now().minusMinutes(150));
        item1.setToDoList(todolist);
        item1 = itemRepository.save(item1);
        item2 = new Item("Item 2", "Content for item 2", LocalDateTime.now().minusMinutes(120));
        item2.setToDoList(todolist);
        item2 = itemRepository.save(item2);
        item3 = new Item("Item 3", "Content for item 3", LocalDateTime.now().minusMinutes(90));
        item3.setToDoList(todolist);
        item3 = itemRepository.save(item3);
        item4 = new Item("Item 4", "Content for item 4", LocalDateTime.now().minusMinutes(60));
        item4.setToDoList(todolist);
        item4 = itemRepository.save(item4);
        item5 = new Item("Item 5", "Content for item 5", LocalDateTime.now().minusMinutes(30));
        item5.setToDoList(todolist);
        item5 = itemRepository.save(item5);
    }

    @Autowired
    public TodoListServiceTest(TodoListService todoListService, TodoListRepository todoListRepository, ItemRepository itemRepository) {
        this.todoListService = todoListService;
        this.todoListRepository = todoListRepository;
        this.itemRepository = itemRepository;
    }

    @Test
    @DisplayName("Add item in todolist")
    void addItem() {
        Item newItem = new Item("new Item", "new item content");
        todolist = todoListService.addItem(todolist.getId(), newItem);
        List<Item> items = itemRepository.getAllByToDoListOrderByCreatedAtAsc(todolist);
        Assertions.assertThat(items.stream()
                .filter(i -> i.getName().equals(newItem.getName()))
                .count())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("Add item in todolist without waiting 30 minutes")
    void addItemWithin30Minutes() {
        item5.setCreatedAt(LocalDateTime.now().minusMinutes(29));
        itemRepository.save(item5);
        Item newItem = new Item("new Item", "new item content");

        Assertions.assertThatThrownBy(() -> todoListService.addItem(todolist.getId(), newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("You need to wait 30 minutes between to tasks");
    }

    @Test
    @DisplayName("Add item in todolist where already 10 items")
    void addItemAlreadyTenItems() {
        itemRepository.save(new Item("new Item 6", "new item content", LocalDateTime.now().minusMinutes(150), todolist));
        itemRepository.save(new Item("new Item 7", "new item content", LocalDateTime.now().minusMinutes(120), todolist));
        itemRepository.save(new Item("new Item 8", "new item content", LocalDateTime.now().minusMinutes(90), todolist));
        itemRepository.save(new Item("new Item 9", "new item content", LocalDateTime.now().minusMinutes(60), todolist));
        itemRepository.save(new Item("new Item 10", "new item content", LocalDateTime.now().minusMinutes(30), todolist));

        Item newItem = new Item("new Item", "new item content");

        Assertions.assertThatThrownBy(() -> todoListService.addItem(todolist.getId(), newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Can't add more than 10 items in todo list");
    }

    @Test
    @DisplayName("Add invalid item in todolist")
    void addInvalidItem() {
        Item newItem = new Item("", "new item content");

        Assertions.assertThatThrownBy(() -> todoListService.addItem(todolist.getId(), newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Item not valid");
    }

    @Test
    @DisplayName("Add item in null todolist")
    void addItemNullList() {
        itemRepository.deleteAll();
        todoListRepository.deleteAll();
        Item newItem = new Item("new Item", "new item content");

        Assertions.assertThat(todoListService.addItem(todolist.getId(), newItem)).isNull();
    }

    @Test
    @DisplayName("Remove item in null todolist")
    void removeItemNullList() {
        itemRepository.deleteAll();
        todoListRepository.deleteAll();

        Assertions.assertThat(todoListService.removeItem(todolist.getId(), 0)).isNull();
    }

    @Test
    @DisplayName("Remove item not in todolist")
    void removeItemOutOfRange() {
        Assertions.assertThatThrownBy(() -> todoListService.removeItem(todolist.getId(), 1000))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Item not found in todoList");

        item5.setToDoList(null);
        itemRepository.save(item5);

        Assertions.assertThatThrownBy(() -> todoListService.removeItem(todolist.getId(), item5.getId()))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Item not found in todoList");
    }

    @Test
    @DisplayName("Remove item in todolist")
    void removeItem() {
        todolist = todoListService.removeItem(todolist.getId(), item1.getId());

        Assertions.assertThat(
                todolist.getItems().stream().anyMatch(i -> i.getName().equals(item1.getName())))
                .isFalse();
    }

    @Test
    @DisplayName("Update with invalid item todolist")
    void updateInvalidItem() {
        Item newItem = new Item("", "new item content");

        Assertions.assertThatThrownBy(() -> todoListService.updateItem(todolist.getId(), item1.getId(), newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Item not valid");
    }

    @Test
    @DisplayName("Update item in null todolist")
    void updateItemNullList() {
        todolist = new TodoList();
        todolist.setId(0);
        Item newItem = new Item("new Item", "new item content");

        Assertions.assertThat(todoListService.updateItem(todolist.getId(), item1.getId(), newItem)).isNull();
    }

    @Test
    @DisplayName("Update item out of range in todolist")
    void updateItemOutOfRange() {
        Item newItem = new Item("new Item", "new item content");

        Assertions.assertThatThrownBy(() -> todoListService.updateItem(todolist.getId(), 1000, newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Item not found in todoList");

        item5.setToDoList(null);
        itemRepository.save(item5);

        Assertions.assertThatThrownBy(() -> todoListService.updateItem(todolist.getId(), item5.getId(), newItem))
                .isInstanceOf(TodoListException.class)
                .hasMessage("Item not found in todoList");
    }

    @Test
    @DisplayName("Update item in todolist")
    void updateItem() {
        Item newItem = new Item("new Item", "new item content");
        todolist = todoListService.updateItem(todolist.getId(), item1.getId(), newItem);

        Assertions.assertThat(todolist.getItems().stream()
                .filter(i -> i.getName().equals(newItem.getName()))
                .count())
                .isEqualTo(1);
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

        Assertions.assertThat(todoListService.isItemValid(newItem, todolist, item5.getId())).isTrue();
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
        newItem.setName("     \t \n       ");
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