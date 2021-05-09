package org.esgi.todolist.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ToDoList {
    private ArrayList<Item> items;

    public ToDoList() {
        this.items = new ArrayList<Item>();
    }

    @JsonCreator
    public ToDoList(@JsonProperty("items") ArrayList<Item> items) {
        this.items = items;
    }

    public ToDoList add(Item item) {
        items.add(item);
        return this;
    }

    public void remove(int index) {
        items.remove(index);
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
