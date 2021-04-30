package org.esgi.todolist.models;

import java.util.ArrayList;

public class ToDoList {
    private ArrayList<Item> items;

    public ToDoList() {
        this.items = new ArrayList<Item>();
    }

    public void add(Item item) {
        items.add(item);
    }

    public void remove(int index) {
        items.remove(index);
    }

    public boolean contains(Item item) {
        return items.contains(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
