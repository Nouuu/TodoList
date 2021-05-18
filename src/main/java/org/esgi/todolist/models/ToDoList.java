package org.esgi.todolist.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="TODOLIST")
public class ToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany
    private List<Item> items;

    public ToDoList() {
        this.items = new ArrayList<Item>();
    }

    public ToDoList(int id, ArrayList<Item> items) {
        this.id = id;
        this.items = items;
    }

    @JsonCreator
    public ToDoList(@JsonProperty("items") List<Item> items) {
        this.items = items;
    }

    public ToDoList add(Item item) {
        items.add(item);
        return this;
    }

    public void remove(int index) {
        items.remove(index);
    }

    public List<Item> getItems() {
        return items;
    }
}
