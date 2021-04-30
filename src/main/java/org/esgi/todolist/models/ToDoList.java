package org.esgi.todolist.models;

import java.util.ArrayList;

public class ToDoList {
  private ArrayList<Item> items;

  public ToDoList(ArrayList<Item> items) {
    this.items = items;
  }

  public void add(Item item) {
    items.add(item);
  }

  public void remove(Item item) {
    items.remove(item);
  }

  public void contains(Item item) {
    items.contains(item);

  }

}
