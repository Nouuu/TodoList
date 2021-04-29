package org.esgi.todolist.routes;

import org.esgi.todolist.models.PingModel;
import org.esgi.todolist.services.PingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping/")
public class PingApi {
    private final PingService pingService;

    @Autowired
    public PingApi(PingService pingService) {
        this.pingService = pingService;
    }

    @GetMapping("/")
    public String ping() {
        return pingService.sayHello();
    }

    @GetMapping("/{name}")
    public PingModel ping2(@PathVariable String name) {
        return pingService.ping2(name);
    }
}
