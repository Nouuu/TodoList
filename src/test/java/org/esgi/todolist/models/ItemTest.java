package org.esgi.todolist.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@ActiveProfiles("test")
class ItemTest {

    @Test
    void toJSON() throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        String expected = "{\"name\":\"New item\",\"content\":\"content\",\"id\":0,\"createdAt\":\"" + now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\"}";
        Item item = new Item("New item", "content", now);
        Assertions.assertThat(item.toJSON()).isEqualTo(expected);
    }
}