package org.esgi.todolist.services;

import org.assertj.core.api.Assertions;
import org.esgi.todolist.commons.exceptions.UserException;
import org.esgi.todolist.models.User;
import org.esgi.todolist.repositories.ItemRepository;
import org.esgi.todolist.repositories.TodoListRepository;
import org.esgi.todolist.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    private final int passwordMinLength;
    private final int passwordMaxLength;
    User user;

    @InjectMocks
    private final UserService userService;

    @MockBean
    private TodoListService todoListServiceMock;

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
}
