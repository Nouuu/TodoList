package org.esgi.todolist.services;

import org.esgi.todolist.models.PingModel;
import org.springframework.stereotype.Service;

@Service
public class PingService {

    public String sayHello() {
        System.out.println("Hello World");
        return "Pong";
    }

    public PingModel ping2(String name) {
        return new PingModel(name, "youlo", "un message");
    }
}
