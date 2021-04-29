package org.esgi.todolist.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PingServiceTest {

    private PingService pingService;

    @Autowired
    public PingServiceTest(PingService pingService) {
        this.pingService = pingService;
    }

    @Test
    void sayHello() {
        Assertions.assertEquals(pingService.sayHello(), "Pong");
    }
}