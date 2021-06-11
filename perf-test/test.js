import http from "k6/http";

import { sleep, check } from "k6";

function generatePrefixForEmail() {
  const chars = "abcdefghijklmnopqrstuvwxyz";
  const getSize = () => Math.random() * 10 + 6;
  function shuffle(s) {
    return s
      .split("")
      .sort((a, b) => 0.5 - Math.random())
      .join("");
  }
  const shuffled = shuffle(chars);
  shuffled.slice(0, getSize());
  return shuffle(chars);
}

export let options = {
  vus: 50,
  duration: "10s",
};

export default function () {
  const emailPrefix = generatePrefixForEmail();
  const res = http.post(
    "http://localhost:8080/user/",
    JSON.stringify({
      firstname: "Le Dieu",
      lastname: "Citron",
      email: emailPrefix + "@cretin.com",
      password: "celuiquiliscaest",
    }),
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );
  check(res, { "status: 201": (r) => r.status === 201 });

  sleep(1);
}
