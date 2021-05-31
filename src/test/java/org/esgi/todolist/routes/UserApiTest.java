package org.esgi.todolist.routes;


import org.esgi.todolist.MvcHelper;
import org.esgi.todolist.commons.exceptions.ResponseError;
import org.esgi.todolist.commons.exceptions.UserException;
import org.esgi.todolist.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserApiTest {

//    @InjectMocks
    @Autowired
    private UserApi userApi;

//    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private UserService userServiceMock;

    private MvcHelper mvcHelper;

    @BeforeEach
    void setUp() {
        mvcHelper = new MvcHelper(userApi, "");
    }

    @Test
    void testCreateToDoListWithGoodUser() throws Exception {
        String data = "{ " +
                "\"firstname\": \"firstname\", " +
                "\"lastname\": \"lastname\", " +
                "\"email\": \"email@email.email\", " +
                "\"password\": \"fewfvwouefbw;f\"" +
                "}";
        String returnData = "{" +
                "\"firstname\":\"firstname\"," +
                "\"lastname\":\"lastname\"," +
                "\"email\":\"email@email.email\"," +
                "\"password\":\"fewfvwouefbw;f\"," +
                "\"toDoList\":{\"items\":[]}" +
                "}";

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
    }
}
