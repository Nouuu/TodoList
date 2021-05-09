package org.esgi.todolist.routes;


import org.esgi.todolist.MvcHelper;
import org.esgi.todolist.commons.exceptions.UserException;
import org.esgi.todolist.models.User;
import org.esgi.todolist.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
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
        String data = "{ \"firstname\": \"\", \"lastname\": \"lastename\", \"email\": \"email@email.email\", \"password\": \"fewfvwouefbw;f\"}";
        doThrow(new UserException("User is not valid")).when(userServiceMock).createTodolist(any(User.class));

        mvcHelper.invokePostMethod("ToDoList", data)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo(data)));

    }

    @Test
    void testCreateToDoListWithBadUser() throws Exception {
        String data = "{ \"firstname\": \"\", \"lastname\": \"lastename\", \"email\": \"email@email.email\", \"password\": \"fewfvwouefbw;f\"}";
        mvcHelper.invokePostMethod("ToDoList", data)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo(data)));
    }
}
