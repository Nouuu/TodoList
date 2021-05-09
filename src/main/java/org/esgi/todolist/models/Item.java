package org.esgi.todolist.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Item {
    private String name;
    private String content;
    private LocalDateTime createdAt;

    @JsonCreator
    public Item(@JsonProperty("name") String name,
                @JsonProperty("content") String content,
                @JsonProperty("createdAt") LocalDateTime createdAt) {
        this.name = name;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Item(String name, String content) {
        this.name = name;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }
}
