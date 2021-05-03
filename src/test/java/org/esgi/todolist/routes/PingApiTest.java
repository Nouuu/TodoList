package org.esgi.todolist.routes;

import org.esgi.todolist.MvcHelper;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private PingApi pingApi;

    private MvcHelper mvcHelper;

    @BeforeEach
    void setUp() {
        mvcHelper = new MvcHelper(pingApi, "");
    }

    @Test
    void ping() throws Exception {
        mvcHelper.invokeGetMethod("/ping/")
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Pong")));
    }
}