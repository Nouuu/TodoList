package org.esgi.todolist.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.esgi.todolist.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

  private UserService userService;

  @Autowired
  public UserServiceTest(UserService userService) {
    this.userService = userService;
  }

  @Test
  public void testIfUserWithoutFirstNameIsValid() {
    final User user = new User("", "lastname", "email@email.fr", "123145f");
    assertFalse(userService.isValid(user));
  }

  @Test
  public void testIfUserWithoutLastNameIsValid() {
    final User user = new User("firstName", "", "email@email.fr", "123145f");
    assertFalse(userService.isValid(user));
  }

  @Test
  public void testIfUserWithoutEmailIsValid() {
    final User user = new User("firstName", "lastname", "", "123145f");
    assertFalse(userService.isValid(user));
  }

  @Test
  public void testIfUserWithoutPasswordIsValid() {
    final User user = new User("firstName", "lastname", "email@email.fr", "");
    assertFalse(userService.isValid(user));
  }

  @Test
  public void testIfUserWithWrongEmailIsValid() {
    final User user1 = new User("firstName", "lastname", "emailemail.fr", "asd154684f5dv2c");
    final User user2 = new User("firstName", "lastname", "emailemailfr", "asd154684f5dv2c");
    final User user3 = new User("firstName", "lastname", "###²@email.fv", "asd154684f5dv2c");
    assertFalse(userService.isValid(user1));
    assertFalse(userService.isValid(user2));
    assertFalse(userService.isValid(user3));
  }

  @Test
  public void testGoodUserIsValid() {
    final User user = new User("firstName", "lastName", "email@email.fr", "123145f");
    assertTrue(userService.isValid(user));
  }
}
