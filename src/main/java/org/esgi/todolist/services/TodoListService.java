package org.esgi.todolist.services;

import org.esgi.todolist.commons.exceptions.TodoListException;
import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.TodoList;
import org.esgi.todolist.repositories.ItemRepository;
import org.esgi.todolist.repositories.TodoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class TodoListService {
    private final int contentMaxLength;
    private final ItemRepository itemRepository;
    private final TodoListRepository todoListRepository;
    private final EmailSenderService emailSenderService;

    @Autowired
    public TodoListService(@Value("${item.content.max-length}") int contentMaxLength, ItemRepository itemRepository, TodoListRepository todoListRepository, EmailSenderService emailSenderService) {
        this.contentMaxLength = contentMaxLength;
        this.itemRepository = itemRepository;
        this.todoListRepository = todoListRepository;
        this.emailSenderService = emailSenderService;
    }

    @Transactional
    public TodoList getTodoList(int todoListId) {
        return todoListRepository.findById(todoListId).orElse(null);
    }

    @Transactional
    public TodoList addItem(int todoListId, Item item) {
        TodoList todoList = todoListRepository.findById(todoListId).orElse(null);
        if (todoList == null) {
            return null;
        }

        if (todoList.getItems().size() >= 10) {
            throw new TodoListException("Can't add more than 10 items in todo list");
        }

        if (!isItemValid(item, todoList, -1)) {
            throw new TodoListException("Item not valid");
        }

        LocalDateTime now = LocalDateTime.now();
        if (todoList.getItems().size() > 0) {
            Item lastItem = todoList.getItems().get(todoList.getItems().size() - 1);
            if (now.minusMinutes(30).isBefore(lastItem.getCreatedAt())) {
                throw new TodoListException("You need to wait 30 minutes between two tasks");
            }
        }
        item.setCreatedAt(now);
        item.setToDoList(todoList);
        itemRepository.save(item);
        todoList.add(item);
//        todoList = todoListRepository.findById(todoList.getId()).orElse(null);
//
//        assert todoList != null;
        if (todoList.getItems().size() == 8) {
            emailSenderService.sendWarningMessage(todoList.getUser().getEmail());
        }

        return todoList;
    }

    @Transactional
    public TodoList removeItem(int todoListId, int itemId) {
        TodoList todoList = todoListRepository.findById(todoListId).orElse(null);
        if (todoList == null) {
            return null;
        }
        if (todoList.getItems().stream().noneMatch(i -> i.getId() == itemId)) {
            throw new TodoListException("Item not found in todoList");
        }
        itemRepository.deleteById(itemId);
        todoList.getItems().removeIf(i -> i.getId() == itemId);
        return todoListRepository.findById(todoListId).orElse(null);
    }

    @Transactional
    public TodoList updateItem(int todoListId, int itemId, Item item) {
        TodoList todoList = todoListRepository.findById(todoListId).orElse(null);
        if (todoList == null) {
            return null;
        }
        Item itemFromDB = todoList.getItems().stream().filter(i -> i.getId() == itemId).findFirst().orElse(null);
        if (itemFromDB == null) {
            throw new TodoListException("Item not found in todoList");
        }
        if (!isItemValid(item, todoList, itemId)) {
            throw new TodoListException("Item not valid");
        }
        item.setToDoList(todoList);
        item.setCreatedAt(itemFromDB.getCreatedAt());
        item.setId(itemId);
        itemRepository.save(item);

        return todoListRepository.getByItemsContaining(item).orElse(null);
    }

    @Transactional
    public void deleteTodoList(int todoListId) {
        TodoList todoList = todoListRepository.findById(todoListId).orElse(null);
        if (todoList == null) {
            throw new TodoListException("Trying to delete todo list but not found on id " + todoListId);
        }
        itemRepository.deleteAll(todoList.getItems());
        todoListRepository.delete(todoList);
    }

    public boolean isItemValid(Item item, TodoList toDoList, int ignoreId) {
        if (item == null) {
            return false;
        }
        if (!StringUtils.hasText(item.getName())) {
            return false;
        }

        boolean isDoublon = itemRepository.getAllByToDoListOrderByCreatedAtAsc(toDoList).stream()
                .anyMatch(i -> i.getName().equals(item.getName())
                        && i.getId() != ignoreId);
        if (isDoublon) {
            return false;
        }
        if (!StringUtils.hasText(item.getContent()) || item.getContent().length() > contentMaxLength) {
            return false;
        }
        return true;
    }
}
