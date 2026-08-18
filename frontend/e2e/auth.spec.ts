import { expect, test } from "@playwright/test";
import { ACCOUNTS } from "./helpers";

test.describe("sign in", () => {
  /** The one journey that must go through the real form, not the API. */
  test("a member can sign in through the form and lands signed in", async ({ page }) => {
    await page.goto("/login");

    await page.getByLabel("電子信箱").fill(ACCOUNTS.consumer.email);
    await page.getByLabel("密碼").fill(ACCOUNTS.consumer.password);
    await page.getByRole("button", { name: "登入" }).click();

    await expect(page).toHaveURL("/");
    await expect(page.getByRole("link", { name: ACCOUNTS.consumer.name })).toBeVisible();
    await expect(page.getByRole("button", { name: "登出" })).toBeVisible();
  });

  test("a wrong password shows the server's message and stays put", async ({ page }) => {
    await page.goto("/login");

    await page.getByLabel("電子信箱").fill(ACCOUNTS.consumer.email);
    await page.getByLabel("密碼").fill("WrongPassword1!");
    await page.getByRole("button", { name: "登入" }).click();

    await expect(page.getByRole("alert")).toBeVisible();
    await expect(page).toHaveURL(/\/login/);
  });

  test("signing out clears the session", async ({ page }) => {
    await page.goto("/login");
    await page.getByLabel("電子信箱").fill(ACCOUNTS.consumer.email);
    await page.getByLabel("密碼").fill(ACCOUNTS.consumer.password);
    await page.getByRole("button", { name: "登入" }).click();
    await expect(page.getByRole("button", { name: "登出" })).toBeVisible();

    await page.getByRole("button", { name: "登出" }).click();

    await expect(page.getByRole("link", { name: "登入" }).first()).toBeVisible();
    expect(await page.evaluate(() => localStorage.getItem("pn.accessToken"))).toBeNull();
  });

  test("the forgot-password page does not reveal whether an account exists", async ({ page }) => {
    await page.goto("/forgot-password");

    await page.getByLabel("電子信箱").fill("definitely-not-registered@example.com");
    await page.getByRole("button", { name: "寄送重設連結" }).click();

    await expect(page.getByRole("heading", { name: /請查看信箱/ })).toBeVisible();
    await expect(page.getByText(/如果.*是已註冊的帳號/)).toBeVisible();
  });

  test("the company sign-up form is reachable from the member one", async ({ page }) => {
    await page.goto("/register");
    await page.getByRole("link", { name: "註冊企業會員" }).click();

    await expect(page.getByRole("heading", { name: "企業會員註冊" })).toBeVisible();
    await expect(page.getByLabel("公司 / 店家名稱")).toBeVisible();
  });
});
