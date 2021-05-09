package org.esgi.todolist.routes;


import org.assertj.core.api.Assertions;
import org.esgi.todolist.MvcHelper;
import org.esgi.todolist.commons.exceptions.UserException;
import org.esgi.todolist.models.ToDoList;
import org.esgi.todolist.models.User;
import org.esgi.todolist.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserApiTest {

    @InjectMocks
    @Autowired
    private UserApi userApi;

    @MockBean
    private UserService userServiceMock;

    private MvcHelper mvcHelper;

    @BeforeEach
    void setUp() {
        mvcHelper = new MvcHelper(userApi, "");
    }

    @Test
    void testCreateToDoListWithGoodUser() throws Exception {
        String data = "{ \"firstname\": \"lastname\", \"lastname\": \"lastname\", \"email\": \"email@email.email\", \"password\": \"fewfvwouefbw;f\"}";
        String returnData = "{\"firstname\":\"firstname\",\"lastname\":\"lastname\",\"email\":\"email@email.email\",\"password\":\"fewfvwouefbw;f\",\"toDoList\":{\"items\":[]}}";

        doReturn(new User("firstname", "lastname", "email@email.email", "fewfvwouefbw;f", new ToDoList())).when(userServiceMock).createTodolist(Mockito.any());
        mvcHelper.invokePostMethod("ToDoList", data)
                .andExpect(status().isCreated())
                .andExpect(content().string(equalTo(returnData)));

    }

    @Test
    void testCreateToDoListWithBadUser() {
        String data = "{ \"firstname\": \"\", \"lastname\": \"lastname\", \"email\": \"email@email.email\", \"password\": \"fewfvwouefbw;f\"}";
        doThrow(new UserException("User is not valid")).when(userServiceMock).createTodolist(any(User.class));
        Assertions.assertThatThrownBy(() -> {
            mvcHelper.invokePostMethod("ToDoList", data)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(equalTo(data)));
        }).isInstanceOf(UserException.class).hasMessage("User is not valid");
    }

    @Test
    void testAddItemWhenEverythingIsOk() throws Exception {
        String data = "{\"firstname\": \"firstname\",\"lastname\": \"lastname\",\"email\": \"email@email.email\",\"password\": \"wrhvfewfvweb\",\"ToDoList\": {\"items\": []},{\"name\": \"name\",\"content\": \"name's content\",\"createdAt\": \"2021-05-09T11:02:15.202Z\"}";
        String returnData = "";
        mvcHelper.invokePostMethod("items", data)
                .andExpect(status().isCreated())
                .andExpect(content().string(equalTo(returnData)));
    }

    @Test
    void testUpdateItem() {

    }
}
