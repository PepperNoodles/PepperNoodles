import { Page, expect } from "@playwright/test";

export const API = process.env.E2E_API_URL ?? "http://localhost:8080/api/v1";

/** Credentials for the seeded accounts. */
export interface TestAccount {
  email: string;
  password: string;
}

export const ACCOUNTS = {
  consumer: { email: "mei@example.com", password: "Password123!", name: "小美" },
  owner: { email: "owner.chun@peppernoodles.local", password: "Password123!" },
  admin: { email: "admin@peppernoodles.local", password: "Password123!" },
} as const;

/**
 * Signs in through the API and injects the tokens before any page script runs.
 *
 * <p>`addInitScript` matters here: writing to localStorage after a `goto` races
 * the AuthProvider, which has already decided the visitor is anonymous by then.
 * Injecting up front means every subsequent navigation boots authenticated.
 *
 * <p>Driving the login form for every test would make each one depend on that
 * one screen; the form itself is covered by its own test.
 */
export async function loginAs(page: Page, account: TestAccount) {
  const response = await page.request.post(`${API}/auth/login`, {
    data: { email: account.email, password: account.password },
  });
  expect(response.ok(), `login failed for ${account.email}`).toBeTruthy();
  const auth = await response.json();

  await page.addInitScript(
    ([access, refresh]) => {
      localStorage.setItem("pn.accessToken", access);
      localStorage.setItem("pn.refreshToken", refresh);
    },
    [auth.accessToken, auth.refreshToken],
  );
  return auth;
}

/** Empties the signed-in user's cart so cart tests start from a known state. */
export async function clearCart(page: Page) {
  await page.request.delete(`${API}/cart`, { headers: await authHeaders(page) });
}

/**
 * Authenticated request headers.
 *
 * <p>Signs in again over the API rather than reading the page's localStorage,
 * which is only populated once a navigation has happened.
 */
export async function authHeaders(page: Page, account: TestAccount = ACCOUNTS.consumer) {
  const response = await page.request.post(`${API}/auth/login`, {
    data: { email: account.email, password: account.password },
  });
  const auth = await response.json();
  return { Authorization: `Bearer ${auth.accessToken}` };
}

/** Creates a forum post through the API and returns its id. */
export async function createPost(page: Page, body: string) {
  const response = await page.request.post(`${API}/forum/posts`, {
    headers: await authHeaders(page),
    data: { body, tagIds: [] },
  });
  expect(response.ok()).toBeTruthy();
  const location = response.headers()["location"] ?? "";
  return Number(location.split("/").pop());
}

/** Makes sure the signed-in user is not following the given member. */
export async function ensureNotFollowing(page: Page, userId: number) {
  await page.request.delete(`${API}/users/${userId}/follow`, { headers: await authHeaders(page) });
}
