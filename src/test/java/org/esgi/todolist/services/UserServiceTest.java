package org.esgi.todolist.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.esgi.todolist.commons.exceptions.TodoListException;
import org.esgi.todolist.commons.exceptions.UserException;
import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.ToDoList;
import org.esgi.todolist.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private final UserService userService;

    @MockBean
    private TodoListService todoListServiceMock;

    @MockBean
    private EmailSenderService emailSenderServiceMock;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void testIfUserWithoutFirstNameIsValid() {
        final User user = new User("", "lastname", "email@email.fr", "123145dvdvezf");
        assertFalse(userService.isValid(user));
    }

    @Test
    public void testIfUserWithoutLastNameIsValid() {
        final User user = new User("firstName", "", "email@email.fr", "12314qfqdsf5f");
        assertFalse(userService.isValid(user));
    }

    @Test
    public void testIfUserWithoutEmailIsValid() {
        final User user = new User("firstName", "lastname", "", "qsfqfdqferg'24");
        assertFalse(userService.isValid(user));
    }

    @Test
    public void testIfUserWithoutPasswordIsValid() {
        final User user = new User("firstName", "lastname", "email@email.fr", "");
        assertFalse(userService.isValid(user));
    }

    @Test
    public void testUserWithShortPasswordIsValid() {
        final User user = new User("firstName", "lastname", "email@email.fr", "123azer");
        assertFalse(userService.isValid(user));
    }

    @Test
    public void testIfUserWithWrongEmailIsValid() {
        final User user1 = new User("firstName", "lastname", "emailemail.fr", "asd154684f5dv2c");
        final User user2 = new User("firstName", "lastname", "emailemailfr", "asd154684f5dv2c");
        final User user3 = new User("firstName", "lastname", "email@", "asd154684f5dv2c");
        assertFalse(userService.isValid(user1));
        assertFalse(userService.isValid(user2));
        assertFalse(userService.isValid(user3));
    }

    @Test
    public void testGoodUserIsValid() {
        final User user = new User("firstName", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        assertTrue(userService.isValid(user));
    }

    @Test
    public void createToDoListWithBadUser() {
        User user = new User("", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        assertThrows(UserException.class, () -> {
            userService.createTodolist(user);
        });
    }

    @Test
    public void createToDoListWithGoodUser() {
        final User user = new User("firstName", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        assertDoesNotThrow(() -> {
            userService.createTodolist(user);
        });
    }

    @Test
    public void getToDoListWhenUserAlreadyHasOne() {
        final User user = new User("firstName", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        assertDoesNotThrow(() -> {
            userService.createTodolist(user);
            userService.createTodolist(user);
        });
    }

    @Test
    public void addItemWhenIsNotValid() {
        final User user = new User("", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        final Item item = new Item("test", "content de test", LocalDateTime.now());
        assertThrows(UserException.class, () -> {
            userService.createTodolist(user);
            userService.addItem(user, item);
        });
    }

    @Test
    public void addItemWhenToDoListIsNotCreate() {
        final User user = new User("FirstName", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        final Item item = new Item("test", "content de test", LocalDateTime.now());
        assertThrows(UserException.class, () -> {
            userService.addItem(user, item);
        });
    }

    @Test
    @DisplayName("Test add item error user handling")
    public void testUserAddItemError() {
        final User user = new User("FirstName", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        final Item item = new Item("test", "content de test", LocalDateTime.now());
        user.createTodolist();
        Mockito.doThrow(new TodoListException("You need to wait 30 minutes between to tasks"))
                .when(todoListServiceMock).addItem(Mockito.any(), Mockito.any());

        assertThrows(TodoListException.class, () -> {
            userService.addItem(user, item);
        });
    }

    @Test
    @DisplayName("Test add item do nothing don't send email")
    public void testUserAddItemMock() {
        final User user = new User("FirstName", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        final Item item = new Item("test", "content de test", LocalDateTime.now());
        user.createTodolist();

        Mockito.doNothing().when(emailSenderServiceMock).sendWarningMessage(Mockito.any());

        assertThrows(TodoListException.class, () -> {
            userService.addItem(user, item);
        });
    }

    @Test
    @DisplayName("Test item valid")
    public void testUserIsValidItemMock() {
        final User user = new User("FirstName", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        final Item item = new Item("test", "content de test", LocalDateTime.now());
        user.createTodolist();

        Mockito.doReturn(true).when(todoListServiceMock).isItemValid(Mockito.any(Item.class), Mockito.any(ToDoList.class),Mockito.any());

        assertThrows(TodoListException.class, () -> {
            userService.addItem(user, item);
        });
    }
}
