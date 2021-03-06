package org.esgi.todolist.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TODOLIST")
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "toDoList", fetch = FetchType.EAGER)
    private List<Item> items;

    @OneToOne()
    @JsonIgnore
    private User user;

    public TodoList() {
        this.items = new ArrayList<>();
    }

    public TodoList(User user) {
        this.items = new ArrayList<>();
        this.user = user;
    }

    public TodoList add(Item item) {
        items.add(item);
        return this;
    }

    public List<Item> getItems() {
        return items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
/*
        return "{\"id\":" + this.id +
                ",\"items\":[" + this.items.stream().map(Item::toJSON).collect(Collectors.joining(", ")) +
                "],\"user\":" + this.user.getId() + "}";
*/
    }
}
