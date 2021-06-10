package org.esgi.todolist.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TodoListTest {

    @Test
    void toJSON() throws JsonProcessingException {
        TodoList todoList = new TodoList();
        String expected = "{\"id\":0,\"items\":[]}";
        Assertions.assertThat(todoList.toJSON()).isEqualTo(expected);
    }

    @Test
    void toJSONWithItems() throws JsonProcessingException {
        TodoList todoList = new TodoList();
        LocalDateTime now = LocalDateTime.now();
        todoList.add(new Item("item 1", "content", now));
        todoList.add(new Item("item 2", "content", now));
        String expected = "{\"id\":0,\"items\":[{\"name\":\"item 1\",\"content\":\"content\",\"id\":0," +
                "\"createdAt\":\"" + now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\"},{\"name\":\"item 2\",\"content\":\"content\",\"id\":0," +
                "\"createdAt\":\"" + now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\"}]}";
        Assertions.assertThat(todoList.toJSON()).isEqualTo(expected);

    }
}