package org.esgi.todolist.repositories;

import org.esgi.todolist.models.TodoList;
import org.esgi.todolist.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> getByToDoList(TodoList toDoList);
}
