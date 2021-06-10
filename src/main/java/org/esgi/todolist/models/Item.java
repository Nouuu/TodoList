package org.esgi.todolist.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.esgi.todolist.commons.date_serializer.CustomDateSerializer;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "ITEM")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @JsonIgnore
    @ManyToOne()
    private TodoList toDoList;

    @CreatedDate
    @JsonSerialize(using = CustomDateSerializer.class)
    private LocalDateTime createdAt;


    @JsonCreator
    public Item(@JsonProperty("name") String name,
                @JsonProperty("content") String content) {
        this.name = name;
        this.content = content;
    }

    public Item(String name, String content, LocalDateTime createdAt) {
        this.name = name;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Item(int id, String name, String content, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Item(String name, String content, LocalDateTime createdAt, TodoList toDoList) {
        this.name = name;
        this.content = content;
        this.toDoList = toDoList;
        this.createdAt = createdAt;
    }

    public Item() {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TodoList getToDoList() {
        return toDoList;
    }

    public void setToDoList(TodoList toDoList) {
        this.toDoList = toDoList;
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
        /*return "{\"id\":" + this.id +
                ",\"name\":\"" + this.name +
                "\",\"content\":\"" + this.content +
                "\",\"toDoList\":" + this.toDoList.getId() +
                ",\"createdAt\":\"" + this.createdAt.toString() + "\"}";*/
    }
}
