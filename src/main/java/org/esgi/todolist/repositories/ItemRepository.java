package org.esgi.todolist.repositories;

import org.esgi.todolist.models.Item;
import org.esgi.todolist.models.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> getAllByToDoListOrderByCreatedAtAsc(TodoList toDoList);
}
