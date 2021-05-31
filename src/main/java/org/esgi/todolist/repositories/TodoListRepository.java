package org.esgi.todolist.repositories;

import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.TodoList;
import org.esgi.todolist.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoListRepository extends JpaRepository<TodoList, Integer> {
    Optional<TodoList> getByUser(User user);

    Optional<TodoList> getByItemsContaining(Item items);
}
