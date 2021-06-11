package org.esgi.todolist.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.esgi.todolist.MvcHelper;
import org.esgi.todolist.commons.exceptions.ResponseError;
import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.TodoList;
import org.esgi.todolist.repositories.ItemRepository;
import org.esgi.todolist.repositories.TodoListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TodoListApiTest {

    private final TodoListApi todoListApi;

    private final ItemRepository itemRepository;
    private final TodoListRepository todoListRepository;
    private final ObjectMapper mapper;

    private MvcHelper mvcHelper;

    private TodoList todoList = new TodoList();

    @Autowired
    TodoListApiTest(TodoListApi todoListApi, ItemRepository itemRepository, TodoListRepository todoListRepository) {
        this.todoListApi = todoListApi;
        this.itemRepository = itemRepository;
        this.todoListRepository = todoListRepository;
        mapper = new ObjectMapper();
    }

    @BeforeEach
    void setUp() {
        mvcHelper = new MvcHelper(todoListApi, "todolist");
        itemRepository.deleteAll();
        todoListRepository.deleteAll();
        todoList = new TodoList();
        todoListRepository.save(todoList);
    }

    @Test
    void getTodoList() throws Exception {
        mvcHelper.invokeGetMethod(String.valueOf(todoList.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(todoList.toJSON())));
    }

    @Test
    void getTodoListWithItem() throws Exception {
        Item item = new Item("name", "content", LocalDateTime.now());
        item.setToDoList(todoList);
        itemRepository.save(item);
        todoList.add(item);

        mvcHelper.invokeGetMethod(String.valueOf(todoList.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(todoList.toJSON())));
    }

    @Test
    void getInexistingTodoList() throws Exception {
        mvcHelper.invokeGetMethod(String.valueOf(0))
                .andExpect(status().isNotFound());
    }

    @Test
    void addItem() throws Exception {
        Item item = new Item("name", "content", LocalDateTime.now());
        todoList.add(item);

        String result = mvcHelper.invokePostMethod(String.valueOf(todoList.getId()), item.toJSON())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        TodoList returnedTodoList = mapper.readValue(result, TodoList.class);
        Assertions.assertThat(returnedTodoList.getItems().size()).isEqualTo(1);
        Assertions.assertThat(returnedTodoList.getItems().get(0).getId()).isGreaterThan(0);
        returnedTodoList.getItems().get(0).setId(0);
        Assertions.assertThat(returnedTodoList.toJSON()).isEqualTo(todoList.toJSON());
    }

    @Test
    void addInvalidItem() throws Exception {
        Item item = new Item("", "content", LocalDateTime.now());
        todoList.add(item);

        String result = mvcHelper.invokePostMethod(String.valueOf(todoList.getId()), item.toJSON())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        ResponseError returnedError = mapper.readValue(result, ResponseError.class);
        Assertions.assertThat(returnedError.getMessage()).isEqualTo("Item not valid");
    }

    @Test
    void addItemInexistingTodoList() throws Exception {
        Item item = new Item("new item", "content", LocalDateTime.now());
        todoList.add(item);

        mvcHelper.invokePostMethod(String.valueOf(0), item.toJSON())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateItem() throws Exception {
        Item item = new Item("item name", "content", LocalDateTime.now());
        item.setToDoList(todoList);
        itemRepository.save(item);
        todoList.add(item);
        item.setName("updated name");
        String result = mvcHelper.invokePutMethod(todoList.getId() + "/items/" + item.getId(), item.toJSON())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TodoList returnedTodoList = mapper.readValue(result, TodoList.class);
        Assertions.assertThat(todoList.toJSON()).isEqualTo(returnedTodoList.toJSON());
    }

    @Test
    void updateInvalidItem() throws Exception {
        Item item = new Item("item name", "content", LocalDateTime.now());
        item.setToDoList(todoList);
        itemRepository.save(item);
        todoList.add(item);
        item.setName("");
        String result = mvcHelper.invokePutMethod(todoList.getId() + "/items/" + item.getId(), item.toJSON())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        ResponseError returnedError = mapper.readValue(result, ResponseError.class);
        Assertions.assertThat(returnedError.getMessage()).isEqualTo("Item not valid");
    }

    @Test
    void updateItemInexistingTodoList() throws Exception {
        Item item = new Item("item name", "content", LocalDateTime.now());
        item.setToDoList(todoList);
        itemRepository.save(item);
        todoList.add(item);
        item.setName("");

        mvcHelper.invokePutMethod("0/items/" + item.getId(), item.toJSON())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateItemInexistingItem() throws Exception {
        Item item = new Item("item name", "content", LocalDateTime.now());
        item.setToDoList(todoList);
        itemRepository.save(item);
        todoList.add(item);
        item.setName("");

        String result = mvcHelper.invokePutMethod(todoList.getId() + "/items/0", item.toJSON())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        ResponseError returnedError = mapper.readValue(result, ResponseError.class);
        Assertions.assertThat(returnedError.getMessage()).isEqualTo("Item not found in todoList");
    }


    @Test
    void deleteTodoList() throws Exception {
        mvcHelper.invokeDeleteMethod(String.valueOf(todoList.getId()), "")
                .andExpect(status().isNoContent());
        Assertions.assertThat(todoListRepository.findById(todoList.getId())).isEmpty();
    }

    @Test
    void deleteInexistingTodoList() throws Exception {
        String result = mvcHelper.invokeDeleteMethod(String.valueOf(0), "")
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        ResponseError returnedError = mapper.readValue(result, ResponseError.class);

        Assertions.assertThat(returnedError.getMessage()).isEqualTo("Trying to delete todo list but not found on id 0");
    }

    @Test
    void deleteItem() throws Exception {
        Item item = new Item("name", "content");
        item.setToDoList(todoList);
        itemRepository.save(item);

        mvcHelper.invokeDeleteMethod(todoList.getId() + "/items/" + item.getId(), "")
                .andExpect(status().isNoContent());
        Assertions.assertThat(itemRepository.findById(item.getId())).isEmpty();
    }

    @Test
    void deleteItemInexistingTodoList() throws Exception {
        Item item = new Item("name", "content");
        item.setToDoList(todoList);
        itemRepository.save(item);

        mvcHelper.invokeDeleteMethod("0/items/" + item.getId(), "")
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteInexistingItemTodoList() throws Exception {
        String result = mvcHelper.invokeDeleteMethod(todoList.getId() + "/items/0", "")
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        ResponseError returnedError = mapper.readValue(result, ResponseError.class);

        Assertions.assertThat(returnedError.getMessage()).isEqualTo("Item not found in todoList");
    }


}