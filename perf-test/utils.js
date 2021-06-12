import http from "k6/http";
import { check } from "k6";
const BASE_URL = "http://localhost:8080";

export function generatePrefixForEmail() {
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

export function get(slug) {
  return http.get(`${BASE_URL}/${slug}`);
}

export function post(slug, body) {
  return http.post(`${BASE_URL}/${slug}`, JSON.stringify(body), {
    headers: {
      "Content-Type": "application/json",
    },
  });
}

export function test(requestName, status, req) {
  const testName = `${requestName} status: ${status}`;
  const testParams = {};
  testParams[testName] = (r) => r.status === status;
  check(req, testParams);

  if (req.status < 200 || req.status > 299) {
    console.log(req.status);
    if (req.status !== 404) {
      console.log(req.body);
    }
  }
}
