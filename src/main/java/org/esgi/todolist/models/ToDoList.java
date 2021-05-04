package org.esgi.todolist.models;

import java.util.ArrayList;

public class ToDoList {
    private ArrayList<Item> items;

    public ToDoList() {
        this.items = new ArrayList<Item>();
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
