package org.esgi.todolist.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserWithItem {
  public User user;
  public Item item;

  @JsonCreator
  public UserWithItem(@JsonProperty("user") User user, @JsonProperty("item") Item item) {
    this.user = user;
    this.item = item;
  }
}
