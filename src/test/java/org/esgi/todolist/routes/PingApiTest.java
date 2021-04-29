package org.esgi.todolist.routes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PingApiTest {

    private MockMvc mvc;

    @Autowired
    public PingApiTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    void ping() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/ping/")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Pong")));
    }
}