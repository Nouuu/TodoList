import { sleep } from "k6";
import {
  testAddUser,
  testGetUser,
  testAddTodoList,
  testAddItem,
  testGetTodoList,
} from "./test.js";

export let options = {
  vus: 100,
  duration: "10s",
};

export default function () {
  const userId = testAddUser();
  testGetUser(userId);
  const todoListId = testAddTodoList(userId);
  testAddItem(todoListId);
  testGetTodoList(todoListId);

  sleep(1);
}
