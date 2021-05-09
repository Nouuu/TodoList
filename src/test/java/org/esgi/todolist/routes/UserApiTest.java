package org.esgi.todolist.routes;


import org.esgi.todolist.MvcHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserApiTest {

    @Autowired
    private UserApi userApi;

    private MvcHelper mvcHelper;

    @BeforeEach
    void setUp() {
        mvcHelper = new MvcHelper(userApi, "");
    }

    @Test
    void testCreateToDoListWithGoodUser() throws Exception {
        String data = "{ \"firstname\": \"firstname\", \"lastname\": \"lastename\", \"email\": \"email@email.email\", \"password\": \"fewfvwouefbw;f\"}";
            mvcHelper.invokePostMethod("ToDoList", data)
                    .andExpect(status().isCreated())
                    .andExpect(content().string(equalTo(data)));

        }

    @Test
    void testCreateToDoListWithBadUser() {
        String data = "{ \"firstname\": \"\", \"lastname\": \"lastename\", \"email\": \"email@email.email\", \"password\": \"fewfvwouefbw;f\"}";
        try {
            mvcHelper.invokePostMethod("ToDoList", data)
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(equalTo(data)));
        } catch (Exception e) {

        }
    }
}
