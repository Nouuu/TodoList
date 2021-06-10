package org.esgi.todolist.models;

import org.apache.tomcat.util.buf.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "TODOLIST")
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "toDoList", fetch = FetchType.EAGER)
    private List<Item> items;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    public TodoList() {
        this.items = new ArrayList<>();
    }

    public TodoList(User user) {
        this.items = new ArrayList<>();
        this.user = user;
    }

    public TodoList(int id, ArrayList<Item> items) {
        this.id = id;
        this.items = items;
    }

    public TodoList add(Item item) {
        items.add(item);
        return this;
    }

    public void remove(int index) {
        items.remove(index);
    }

    public List<Item> getItems() {
        return items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String toJSON() {
        List<String> itemsJSON = this.items.stream().map(item -> item.toJSON()).collect(Collectors.toList());
        return "{'id':"+this.id+
                ",'items':['"+ StringUtils.join(itemsJSON,',')+
                "],'user':"+this.user.getId()+"}";
    }
}
