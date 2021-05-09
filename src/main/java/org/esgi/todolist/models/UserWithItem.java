package org.esgi.todolist.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserWithItem {
  public User user;
  public Item item;

  @JsonCreator
  public UserWithItem(
    @JsonProperty("firstname") String firstname,
    @JsonProperty("lastname") String lastname,
    @JsonProperty("email") String email,
    @JsonProperty("password") String password,
    @JsonProperty("toDoList") ToDoList toDoList,
    @JsonProperty("name") String name,
    @JsonProperty("content") String content,
    @JsonProperty("createdAt") LocalDateTime createdAt) {
    this.user = new User(firstname, lastname, email, password, toDoList);
    this.item = new Item(name, content, createdAt);
  }
}
