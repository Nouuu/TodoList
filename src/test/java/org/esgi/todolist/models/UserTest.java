package org.esgi.todolist.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void toJSON() throws JsonProcessingException {
        User user = new User("Firstname", "Lastnale", "email@eee.fr", "password");
        String expected = "{\"firstname\":\"Firstname\",\"lastname\":\"Lastnale\",\"email\":\"email@eee.fr\",\"password\":\"password\",\"id\":0,\"toDoList\":null}";
        Assertions.assertThat(user.toJSON()).isEqualTo(expected);
    }

    @Test
    void toJSONWithEmptyTodoList() throws JsonProcessingException {
        User user = new User("Firstname", "Lastname", "email@eee.fr", "password");
        String expected = "{\"firstname\":\"Firstname\",\"lastname\":\"Lastname\",\"email\":\"email@eee.fr\",\"password\":\"password\",\"id\":0,\"toDoList\":{\"name\":null,\"description\":null,\"id\":0,\"items\":[]}}";
        user.setToDoList(new TodoList());
        Assertions.assertThat(user.toJSON()).isEqualTo(expected);

    }
}