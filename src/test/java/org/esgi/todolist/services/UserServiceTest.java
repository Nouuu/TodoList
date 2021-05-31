/*
package org.esgi.todolist.services;

import org.assertj.core.api.Assertions;
import org.esgi.todolist.commons.exceptions.TodoListException;
import org.esgi.todolist.commons.exceptions.UserException;
import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.TodoList;
import org.esgi.todolist.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DataJpaTest
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
    public void testIfNullUserIsValid() {
        assertFalse(userService.isValid(null));
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
    public void testIfUserWithTooLongPasswordIsValid() {
        final User user = new User("firstName", "lastname", "email@email.fr", "0123456789012345678901234567890123456789");
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
        userService.createTodolist(user);
        assertNotNull(user.getToDoList());
    }

    @Test
    public void getToDoListWhenUserAlreadyHasOne() {
        final User user = new User("firstName", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        userService.createTodolist(user);
        Assertions.assertThatThrownBy(() -> userService.createTodolist(user))
                .isInstanceOf(UserException.class)
                .hasMessage("User have already a todolist");
    }


    @Test
    public void addItemWhenToDoListIsNotCreate() {
        final User user = new User("FirstName", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        final Item item = new Item("test", "content de test", LocalDateTime.now());

        Assertions.assertThatThrownBy(() -> userService.addItem(user, item))
                .isInstanceOf(UserException.class)
                .hasMessage("User invalid or don't have list");
    }

    @Test
    public void addItemWhenNoUser() {
        Assertions.assertThatThrownBy(() -> userService.addItem(null, null))
                .isInstanceOf(UserException.class).hasMessage("User invalid or don't have list");
    }

    @Test
    public void addItemToSendMail() {
        final User user = new User("firstname", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        final Item item0 = new Item("test0", "content de test", LocalDateTime.now().minusMinutes(280));
        final Item item1 = new Item("test1", "content de test", LocalDateTime.now().minusMinutes(240));
        final Item item2 = new Item("test2", "content de test", LocalDateTime.now().minusMinutes(200));
        final Item item3 = new Item("test3", "content de test", LocalDateTime.now().minusMinutes(1600));
        final Item item4 = new Item("test4", "content de test", LocalDateTime.now().minusMinutes(120));
        final Item item5 = new Item("test5", "content de test", LocalDateTime.now().minusMinutes(80));
        final Item item6 = new Item("test6", "content de test", LocalDateTime.now().minusMinutes(40));
        final Item item7 = new Item("test7", "content de test", LocalDateTime.now());

        userService.createTodolist(user);
        user.getToDoList().add(item0).add(item1).add(item2).add(item3).add(item4).add(item5).add(item6);

        doAnswer(invocation -> {
            TodoList todolist = invocation.getArgument(0);
            Item item = invocation.getArgument(1);
            item.setCreatedAt(LocalDateTime.now());
            todolist.add(item);
            return null;
        }).when(todoListServiceMock).addItem(Mockito.any(TodoList.class), Mockito.any(Item.class));

        doCallRealMethod().when(emailSenderServiceMock).sendWarningMessage(any(String.class));

        userService.addItem(user, item7);
        assertEquals(user.getToDoList().getItems().get(7), item7);
        verify(emailSenderServiceMock, times(1)).sendWarningMessage(any(String.class));
    }

    @Test
    @DisplayName("Test add item error user handling")
    public void testUserAddItemError() {
        final User user = new User("FirstName", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        final Item item = new Item("test", "content de test", LocalDateTime.now());
        user.createTodolist();
        Mockito.doThrow(new TodoListException("You need to wait 30 minutes between to tasks"))
                .when(todoListServiceMock).addItem(any(), any());

        assertThrows(TodoListException.class, () -> {
            userService.addItem(user, item);
        });
    }

    @Test
    public void addItemWhenEverythingIsOK() {
        final User user = new User("FirstName", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        final Item item = new Item("test", "content de test", LocalDateTime.now());
        userService.createTodolist(user);

        doAnswer(invocation -> {
            TodoList todolist = invocation.getArgument(0);
            Item itemAdd = invocation.getArgument(1);
            itemAdd.setCreatedAt(LocalDateTime.now());
            todolist.add(itemAdd);
            return null;
        }).when(todoListServiceMock).addItem(Mockito.any(TodoList.class), Mockito.any(Item.class));


        userService.addItem(user, item);
        assertEquals(user.getToDoList().getItems().get(0), item);
        userService.addItem(user, item);
    }

    @Test
    public void removeItemWithoutTodolist() {
        final User user = new User("FirstName", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");

        Assertions.assertThatThrownBy(() -> userService.removeItem(user, 0))
                .isInstanceOf(UserException.class)
                .hasMessage("User invalid or don't have list");
    }

    @Test
    public void removeItemWhenNoUser() {
        Assertions.assertThatThrownBy(() -> userService.removeItem(null, 0))
                .isInstanceOf(UserException.class).hasMessage("User invalid or don't have list");
    }

    @Test
    public void removeItemWhenEverythingIsOK() {
        final User user = new User("FirstName", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        final Item item = new Item("test", "content de test", LocalDateTime.now());
        userService.createTodolist(user);

        doAnswer(invocation -> {
            TodoList todolist = invocation.getArgument(0);
            int index = invocation.getArgument(1);
            todolist.getItems().remove(index);
            return null;
        }).when(todoListServiceMock).removeItem(Mockito.any(TodoList.class), Mockito.any(int.class));

        user.getToDoList().add(item);
        userService.removeItem(user, 0);
        assertEquals(user.getToDoList().getItems().size(), 0);
    }

    @Test
    public void updateItemWithNoTodolist() {
        final User user = new User("firstname", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        final Item item = new Item("test", "content de test", LocalDateTime.now());

        Assertions.assertThatThrownBy(() -> {
            userService.updateItem(user, item, 0);
        }).isInstanceOf(UserException.class).hasMessage("User invalid or don't have list");
    }

    @Test
    public void updateItemWithNoUser() {
        Assertions.assertThatThrownBy(() -> userService.updateItem(null, null, 0))
                .isInstanceOf(UserException.class).hasMessage("User invalid or don't have list");
    }

    @Test
    public void updateItemWhenEverythingIsOK() {
        final User user = new User("firstname", "lastName", "email@email.fr", "1231fqzefqzefgzrg5f");
        final Item item = new Item("test", "content de test", LocalDateTime.now().minusMinutes(40));
        final Item itemUpdate = new Item("testUpdate", "content update", LocalDateTime.now());

        doAnswer(invocation -> {
            TodoList todolist = invocation.getArgument(0);
            int index = invocation.getArgument(1);
            Item itemToUpdate = invocation.getArgument(2);
            itemToUpdate.setCreatedAt(LocalDateTime.now());
            todolist.getItems().set(index, itemToUpdate);
            return null;
        }).when(todoListServiceMock).updateItem(Mockito.any(TodoList.class), Mockito.any(int.class), Mockito.any(Item.class));

        userService.createTodolist(user);
        user.getToDoList().add(item);
        userService.updateItem(user, itemUpdate, 0);
        assertEquals(user.getToDoList().getItems().get(0), itemUpdate);
    }
}
*/
