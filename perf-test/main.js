import { group, sleep } from "k6";
import {
  testAddUser,
  testGetUser,
  testAddTodoList,
  testAddItem,
  testGetTodoList,
} from "./test.js";

const SLEEP_DURATION = 0.1;

export let options = {
  vus: 1000,
  duration: "20s",
};

export default function () {
  group("User Login for add Item", () => {
    const userId = testAddUser();
    testGetUser(userId);
    sleep(SLEEP_DURATION);
    const todoListId = testAddTodoList(userId);
    sleep(SLEEP_DURATION);
    testAddItem(todoListId);
    testGetTodoList(todoListId);
    sleep(SLEEP_DURATION);
  });
}
