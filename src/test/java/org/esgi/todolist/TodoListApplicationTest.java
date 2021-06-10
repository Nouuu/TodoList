package org.esgi.todolist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TodoListApplicationTest {

    @Test
    void contextLoads() {
        TodoListApplication.main(new String[]{});
    }
}