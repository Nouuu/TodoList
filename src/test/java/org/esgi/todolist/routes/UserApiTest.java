package org.esgi.todolist.routes;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.esgi.todolist.MvcHelper;
import org.esgi.todolist.commons.exceptions.ResponseError;
import org.esgi.todolist.models.TodoList;
import org.esgi.todolist.models.User;
import org.esgi.todolist.repositories.ItemRepository;
import org.esgi.todolist.repositories.TodoListRepository;
import org.esgi.todolist.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserApiTest {

    private final UserApi userApi;

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final TodoListRepository todoListRepository;
    private final ObjectMapper mapper;

    private MvcHelper mvcHelper;

    private User user;

    @Autowired
    public UserApiTest(UserApi userApi, UserRepository userRepository, ItemRepository itemRepository, TodoListRepository todoListRepository) {
        this.userApi = userApi;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.todoListRepository = todoListRepository;
        mapper = new ObjectMapper();
    }

    @BeforeEach
    void setUp() {
        mvcHelper = new MvcHelper(userApi, "user");
        itemRepository.deleteAll();
        todoListRepository.deleteAll();
        userRepository.deleteAll();
        user = new User("Firstname", "Lastname", "example@email.com", "validpassword");
        userRepository.save(user);
    }

    @Test
    void getUser() throws Exception {
        mvcHelper.invokeGetMethod(String.valueOf(user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(user.toJSON())));
    }

    @Test
    void getUserWithTodoList() throws Exception {
        TodoList todoList = new TodoList();
        todoList.setUser(user);
        todoListRepository.save(todoList);
        user.setToDoList(todoList);
        mvcHelper.invokeGetMethod(String.valueOf(user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(user.toJSON())));
    }

    @Test
    void getUserNotFound() throws Exception {
        mvcHelper.invokeGetMethod(String.valueOf(0))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser() throws Exception {
        User newUser = new User("newfirstname", "newlastname", "newemail@ee.com", "newvalidpassword");
        String result = mvcHelper.invokePostMethod("", newUser.toJSON())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        User returnedUser = mapper.readValue(result, User.class);
        Assertions.assertThat(returnedUser.getId()).isGreaterThan(0);
        returnedUser.setId(0);
        Assertions.assertThat(returnedUser.toJSON()).isEqualTo(newUser.toJSON());
    }

    @Test
    void createInvalidUser() throws Exception {
        User invalidUser = new User("", "newlastname", "newemail@ee.com", "newvalidpassword");
        String result = mvcHelper.invokePostMethod("", invalidUser.toJSON())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        ResponseError response = mapper.readValue(result, ResponseError.class);
        Assertions.assertThat(response.getMessage()).isEqualTo("User is not valid");
    }


    @Test
    void updateUser() throws Exception {
        user.setFirstname("updatedFirstname");
        mvcHelper.invokePutMethod(String.valueOf(user.getId()), user.toJSON())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(user.toJSON())));
    }

    @Test
    void updateInexistingUser() throws Exception {
        mvcHelper.invokePutMethod(String.valueOf(0), user.toJSON())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateInvalidUser() throws Exception {
        user.setFirstname("");
        String result = mvcHelper.invokePutMethod(String.valueOf(user.getId()), user.toJSON())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        ResponseError response = mapper.readValue(result, ResponseError.class);
        Assertions.assertThat(response.getMessage()).isEqualTo("User is not valid");
    }

    @Test
    void deleteUser() throws Exception {
        mvcHelper.invokeDeleteMethod(String.valueOf(user.getId()), "")
                .andExpect(status().isNoContent())
                .andExpect(content().string(equalTo("User " + user.getId() + " deleted")));
        Assertions.assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    void deleteInexistingUser() throws Exception {
        String result = mvcHelper.invokeDeleteMethod(String.valueOf(0), "")
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        ResponseError response = mapper.readValue(result, ResponseError.class);
        Assertions.assertThat(response.getMessage()).isEqualTo("User not found on id 0");
    }

    @Test
    void createTodoList() throws Exception {
        TodoList todoList = new TodoList();
        user.setToDoList(todoList);

        String result = mvcHelper.invokePostMethod(user.getId() + "/todolist", "")
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        User returnedUser = mapper.readValue(result, User.class);
        Assertions.assertThat(returnedUser.getToDoList().getId()).isGreaterThan(0);
        returnedUser.getToDoList().setId(0);
        Assertions.assertThat(returnedUser.toJSON()).isEqualTo(user.toJSON());
    }

    @Test
    void createTodoListInexistingUser() throws Exception {
        String result = mvcHelper.invokePostMethod("0/todolist", "")
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        ResponseError response = mapper.readValue(result, ResponseError.class);
        Assertions.assertThat(response.getMessage()).isEqualTo("User not found on id 0");
    }

    @Test
    void createTodoListAlreadyExists() throws Exception {
        TodoList todoList = new TodoList();
        todoList.setUser(user);
        todoListRepository.save(todoList);

        String result = mvcHelper.invokePostMethod(user.getId() + "/todolist", "")
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        ResponseError response = mapper.readValue(result, ResponseError.class);
        Assertions.assertThat(response.getMessage()).isEqualTo("User have already a todolist");

    }




    /*@Test
    void testCreateToDoListWithGoodUser() throws Exception {
        String expected = "";

//        doReturn(new User("firstname", "lastname", "email@email.email", "fewfvwouefbw;f", new ToDoList())).when(userServiceMock).createTodolist(any());
        mvcHelper.invokePostMethod("ToDoList", data)
                .andExpect(status().isCreated())
                .andExpect(content().string(equalTo(returnData)));

    }

    @Test
    void testAddItemWhenEverythingIsOk() throws Exception {
        String data = "{\n" +
                "    \"user\": {\n" +
                "        \"firstname\": \"firstname\",\n" +
                "        \"lastname\": \"lastname\",\n" +
                "        \"email\": \"email@email.email\",\n" +
                "        \"password\": \"wrhvfewfvweb\",\n" +
                "        \"toDoList\": {\n" +
                "            \"items\": []\n" +
                "        }\n" +
                "    },\n" +
                "    \"item\": {\n" +
                "        \"name\": \"name\",\n" +
                "        \"content\": \"name's content\",\n" +
                "        \"createdAt\": \"2021-05-09T11:02:15.202Z\"\n" +
                "    }\n" +
                "}";


        mvcHelper.invokePostMethod("items", data)
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateItemWhenEverythingIsOk() throws Exception {
        String data = "{\n" +
                "    \"user\": {\n" +
                "        \"firstname\": \"firstname\",\n" +
                "        \"lastname\": \"lastname\",\n" +
                "        \"email\": \"email@email.email\",\n" +
                "        \"password\": \"wrhvfewfvweb\",\n" +
                "        \"toDoList\": {\n" +
                "            \"items\": [\n" +
                "                {\n" +
                "                \"name\": \"name\",\n" +
                "                \"content\": \"name's content update\",\n" +
                "                \"createdAt\": \"2021-05-09T11:02:15.202Z\"  \n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    },\n" +
                "    \"item\": {\n" +
                "        \"name\": \"name\",\n" +
                "        \"content\": \"name's content\",\n" +
                "        \"createdAt\": \"2021-05-09T11:02:15.202Z\"\n" +
                "    }\n" +
                "}";
        mvcHelper.invokePutMethod("items/0", data)
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteItemWhenEverythingIsOk() throws Exception {
        String data = "{\n" +
                "  \"firstname\": \"firstname\",\n" +
                "  \"lastname\": \"lastname\",\n" +
                "  \"email\": \"email@email.email\",\n" +
                "  \"password\": \"wefhewbfwef\",\n" +
                "  \"toDoList\": {\n" +
                "    \"items\": [\n" +
                "      {\n" +
                "        \"name\": \"name\",\n" +
                "        \"content\": \"name's content\",\n" +
                "        \"createdAt\": \"2021-05-09T12:48:20.322Z\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        mvcHelper.invokeDeleteMethod("items/0", data)
                .andExpect(status().isNoContent());
    }

    @Test
    void testExceptionThrowedResponseEntity() throws Exception {
        String dataInvalidEmail = "{ \"firstname\": \"lastname\", \"lastname\": \"lastname\", \"email\": \"invalidemail\", \"password\": \"fewfvwouefbw;f\"}";

//        doThrow(new UserException("User is not valid")).when(userServiceMock).createTodolist(any(User.class));

        String expected = new ResponseError(HttpStatus.BAD_REQUEST.value(),
                UserException.class.toString(),
                "User is not valid").toString();

        mvcHelper.invokePostMethod("ToDoList", dataInvalidEmail)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo(expected)));
    }*/
}
