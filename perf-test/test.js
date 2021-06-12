import { test, get, post, generatePrefixForEmail } from "./utils.js";

export function testAddUser() {
  const emailPrefix = generatePrefixForEmail();
  const addUserRes = post("user/", {
    firstname: "Le Dieu",
    lastname: "Citron",
    email: emailPrefix + "@cretin.com",
    password: "celuiquiliscaest",
  });
  test("addUser", 201, addUserRes);

  return JSON.parse(addUserRes.body).id;
}

export function testGetUser(userId) {
  const getUserRes = get(`/user/${userId}`);
  test("getUser", 200, getUserRes);
}

export function testAddTodoList(userId) {
  const addTodolistRes = post(`/user/${userId}/todolist`, {
    name: "my todolist",
    description: "A average todolist, nothing more to say.",
  });
  test("addTodolist", 201, addTodolistRes);

  return JSON.parse(addTodolistRes.body).id;
}

export function testAddItem(todoListId) {
  const addItemInTodolistRes = post(`todolist/${todoListId}`, {
    name: "item's 2 name",
    content: "item's content",
  });
  test("addItem", 201, addItemInTodolistRes);
}

export function testGetTodoList(todoListId) {
  const getToDoListRes = get(`todolist/${todoListId}`);
  test("getTodolist", 200, getToDoListRes);
}
