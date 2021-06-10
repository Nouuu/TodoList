package org.esgi.todolist.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.esgi.todolist.commons.exceptions.UserException;
import org.esgi.todolist.models.User;
import org.esgi.todolist.repositories.ItemRepository;
import org.esgi.todolist.repositories.TodoListRepository;
import org.esgi.todolist.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    private final int passwordMinLength;
    private final int passwordMaxLength;
    User user;

    private final UserService userService;

    private final UserRepository userRepository;
    private final TodoListRepository todoListRepository;
    private final ItemRepository itemRepository;


    @Autowired
    public UserServiceTest(UserService userService, UserRepository userRepository,
                           @Value("${user.password.min-length}") int passwordMinLength,
                           @Value("${user.password.max-length}") int passwordMaxLength,
                           TodoListRepository todoListRepository, ItemRepository itemRepository) {
        this.passwordMinLength = passwordMinLength;
        this.passwordMaxLength = passwordMaxLength;
        this.userService = userService;
        this.userRepository = userRepository;
        this.todoListRepository = todoListRepository;
        this.itemRepository = itemRepository;
    }

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        todoListRepository.deleteAll();
        userRepository.deleteAll();
        user = new User("Firstname", "Lastname", "valid@email.com", "v".repeat(passwordMinLength));
        user = userRepository.save(user);
    }

    @Test
    @DisplayName("Is valid user on null object")
    public void testIfNullUserIsValid() {
        assertFalse(userService.isValid(null));
    }

    @Test
    @DisplayName("Is valid user empty firstname")
    public void testIfUserWithoutFirstNameIsValid() {
        user.setFirstname("");
        assertFalse(userService.isValid(user));
    }

    @Test
    @DisplayName("Is valid user empty lastname")
    public void testIfUserWithoutLastNameIsValid() {
        user.setLastname("");
        assertFalse(userService.isValid(user));
    }

    @Test
    @DisplayName("Is valid user empty email")
    public void testIfUserWithoutEmailIsValid() {
        user.setEmail("");
        assertFalse(userService.isValid(user));
    }

    @Test
    @DisplayName("Is valid user empty password")
    public void testIfUserWithoutPasswordIsValid() {
        user.setPassword("");
        assertFalse(userService.isValid(user));
    }

    @Test
    public void testIfUserWithTooLongPasswordIsValid() {
        user.setPassword("3".repeat(passwordMaxLength * 2));
        assertFalse(userService.isValid(user));
    }

    @Test
    public void testUserWithShortPasswordIsValid() {
        user.setPassword("1".repeat(passwordMinLength / 2));
        assertFalse(userService.isValid(user));
    }

    @Test
    public void testIfUserWithWrongEmailIsValid() {
        user.setEmail("emailemail.fr");
        assertFalse(userService.isValid(user));
        user.setEmail("emailemailfr");
        assertFalse(userService.isValid(user));
        user.setEmail("email@");
        assertFalse(userService.isValid(user));
    }

    @Test
    public void testGoodUserIsValid() {
        assertTrue(userService.isValid(user));
    }

    @Test
    public void createToDoListWithUnknowUser() {
        Assertions.assertThatThrownBy(() -> userService.createTodolist(0))
                .isInstanceOf(UserException.class)
                .hasMessage("User not found on id " + 0);
    }

    @Test
    public void createToDoListWithGoodUser() {
        user = userService.createTodolist(user.getId());
        assertNotNull(user.getToDoList());
    }

    @Test
    public void getToDoListWhenUserAlreadyHasOne() {
        userService.createTodolist(user.getId());
        Assertions.assertThatThrownBy(() -> userService.createTodolist(user.getId()))
                .isInstanceOf(UserException.class)
                .hasMessage("User have already a todolist");
    }

    @Test
    public void getUser() throws JsonProcessingException {
        Assertions.assertThat(userService.getUser(user.getId()).toJSON()).isEqualTo(user.toJSON());
    }

    @Test
    public void getUserNotFound() throws JsonProcessingException {
        Assertions.assertThat(userService.getUser(0)).isNull();
    }

    @Test
    public void createUser() throws JsonProcessingException {
        User newUser = new User("firstname", "lastname", "email@email.com", "validpassword");
        newUser = userService.createUser(newUser);
        Assertions.assertThat(newUser.getFirstname()).isEqualTo("firstname");
        Assertions.assertThat(newUser.getLastname()).isEqualTo("lastname");
        Assertions.assertThat(newUser.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(newUser.getPassword()).isEqualTo("validpassword");
    }

    @Test
    public void createInvalidUser() {
        User newUser = new User("", "lastname", "email@email.com", "validpassword");
        Assertions.assertThatThrownBy(() -> userService.createUser(newUser))
                .isInstanceOf(UserException.class)
                .hasMessage("User is not valid");
    }

    @Test
    public void updateUser() {
        user.setFirstname("newFirstname");
        User updatedUser = userService.updateUser(user.getId(), user);
        Assertions.assertThat(updatedUser.getFirstname()).isEqualTo("newFirstname");
        Assertions.assertThat(updatedUser.getLastname()).isEqualTo(user.getLastname());
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());

    }

    @Test
    public void updateInvalidUser() {
        user.setLastname("");
        Assertions.assertThatThrownBy(() -> userService.updateUser(user.getId(), user))
                .isInstanceOf(UserException.class)
                .hasMessage("User is not valid");
    }

    @Test
    public void updateNullUser() {
        Assertions.assertThat(userService.updateUser(0, user)).isNull();
    }

    @Test
    public void deleteUser() {
        userService.deleteUser(user.getId());
        Assertions.assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    public void deleteUserWithTodoList() {
        user = userService.createTodolist(user.getId());
        userService.deleteUser(user.getId());
        Assertions.assertThat(userRepository.findById(user.getId())).isEmpty();
        Assertions.assertThat(itemRepository.findById(user.getToDoList().getId())).isEmpty();
    }

    @Test
    public void deleteInexistingUser() {
        Assertions.assertThatThrownBy(()->userService.deleteUser(0))
                .isInstanceOf(UserException.class)
        .hasMessage("User not found on id 0");
    }
}
