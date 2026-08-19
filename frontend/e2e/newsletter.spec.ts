import { expect, test } from "@playwright/test";
import { ACCOUNTS, API, authHeaders, loginAs, tokenFromMail } from "./helpers";

/** 電子報訂閱 — the 2021 form finally has a backend. */
test.describe("newsletter", () => {
  test("the home page carries the Subscribe band", async ({ page }) => {
    await page.goto("/");
    await expect(page.getByRole("heading", { name: "Subscribe For Newsletter" })).toBeVisible();
    await expect(page.getByLabel("訂閱電子報的信箱")).toBeVisible();
  });

  test("subscribing, confirming and unsubscribing all work end to end", async ({ page }) => {
    const email = `e2e.reader.${Date.now()}@example.com`;

    await page.goto("/");
    await page.getByLabel("訂閱電子報的信箱").fill(email);
    await page.getByRole("button", { name: "Send Now" }).click();
    await expect(page.getByText(/確認信已寄出/)).toBeVisible();

    const confirmToken = await tokenFromMail(page, "newsletter/confirm");
    expect(confirmToken, "no confirmation link in the mail").toBeTruthy();

    await page.goto(`/newsletter/confirm?token=${confirmToken}`);
    await expect(page.getByRole("heading", { name: "訂閱完成 ✓" })).toBeVisible();

    // The welcome mail carries the way out.
    const unsubToken = await tokenFromMail(page, "newsletter/unsubscribe");
    expect(unsubToken, "no unsubscribe link in the welcome mail").toBeTruthy();

    await page.goto(`/newsletter/unsubscribe?token=${unsubToken}`);
    await expect(page.getByRole("heading", { name: "已取消訂閱" })).toBeVisible();
  });

  test("a used confirmation link reports failure rather than pretending", async ({ page }) => {
    const email = `e2e.once.${Date.now()}@example.com`;
    await page.goto("/");
    await page.getByLabel("訂閱電子報的信箱").fill(email);
    await page.getByRole("button", { name: "Send Now" }).click();
    await expect(page.getByText(/確認信已寄出/)).toBeVisible();

    const token = await tokenFromMail(page, "newsletter/confirm");
    await page.goto(`/newsletter/confirm?token=${token}`);
    await expect(page.getByRole("heading", { name: "訂閱完成 ✓" })).toBeVisible();

    await page.goto(`/newsletter/confirm?token=${token}`);
    await expect(page.getByRole("heading", { name: "確認失敗" })).toBeVisible();
  });

  test("a malformed link is handled rather than hanging", async ({ page }) => {
    await page.goto("/newsletter/confirm");
    await expect(page.getByText("確認連結不完整。")).toBeVisible();

    await page.goto("/newsletter/unsubscribe?token=nonsense");
    await expect(page.getByRole("heading", { name: "取消訂閱失敗" })).toBeVisible();
  });

  test("stats are admin-only and count mailable addresses", async ({ page }) => {
    const asAdmin = await page.request.get(`${API}/newsletter/stats`, {
      headers: await authHeaders(page, ACCOUNTS.admin),
    });
    expect(asAdmin.status()).toBe(200);
    const stats = await asAdmin.json();
    expect(stats.total).toBeGreaterThanOrEqual(stats.mailable);

    const asConsumer = await page.request.get(`${API}/newsletter/stats`, {
      headers: await authHeaders(page, ACCOUNTS.consumer),
    });
    expect(asConsumer.status()).toBe(403);
  });
});

test.describe("contact us", () => {
  test("a logged-out visitor must supply a reply address", async ({ page }) => {
    await page.goto("/contact");

    await expect(page.getByRole("heading", { name: "聯絡我們" })).toBeVisible();
    await expect(page.getByLabel(/聯絡信箱/)).toBeVisible();

    await page.getByLabel(/聯絡信箱/).fill(`visitor.${Date.now()}@example.com`);
    await page.getByLabel(/訊息內容/).fill("請問如何成為企業會員？");
    await page.getByRole("button", { name: "送出訊息" }).click();

    await expect(page.getByRole("heading", { name: "訊息已送出 ✓" })).toBeVisible();
  });

  test("a signed-in member is not asked for an address", async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
    await page.goto("/contact");

    await expect(page.getByLabel(/聯絡信箱/)).toHaveCount(0);

    await page.getByLabel(/訊息內容/).fill("這是會員的提問");
    await page.getByRole("button", { name: "送出訊息" }).click();

    await expect(page.getByRole("heading", { name: "訊息已送出 ✓" })).toBeVisible();
  });

  test("the footer links to the contact page", async ({ page }) => {
    await page.goto("/");
    await page.getByRole("link", { name: "聯絡我們" }).click();
    await expect(page).toHaveURL("/contact");
  });
});

test.describe("admin restaurant management", () => {
  test.beforeEach(async ({ page }) => {
    await loginAs(page, ACCOUNTS.admin);
  });

  test("the list shows every restaurant with its owner", async ({ page }) => {
    await page.goto("/admin");
    await page.getByRole("link", { name: "餐廳管理" }).click();

    await expect(page).toHaveURL("/admin/restaurants");
    await expect(page.getByRole("heading", { name: "餐廳管理" })).toBeVisible();
    await expect(page.getByText(/共 \d+ 間/)).toBeVisible();
    await expect(page.getByText("owner.chun@peppernoodles.local").first()).toBeVisible();
  });

  test("searching by owner e-mail narrows the list", async ({ page }) => {
    await page.goto("/admin/restaurants");

    await page.getByLabel("搜尋餐廳").fill("owner.chun@peppernoodles.local");
    await page.getByRole("button", { name: "搜尋" }).click();

    await expect(page.getByText("共 2 間")).toBeVisible();
    await expect(page.getByText("owner.din@peppernoodles.local")).toHaveCount(0);
  });

  test("an admin can jump from the list into managing a restaurant", async ({ page }) => {
    await page.goto("/admin/restaurants");
    // The header also has a 管理 link, so scope to the table body.
    await page.locator("tbody").getByRole("link", { name: "管理" }).first().click();

    await expect(page).toHaveURL(/\/company\/restaurants\/\d+$/);
    await expect(page.getByRole("heading", { name: /^管理「/ })).toBeVisible();
  });

  test("a consumer is refused the restaurant list", async ({ browser }) => {
    const context = await browser.newContext();
    const consumerPage = await context.newPage();
    await loginAs(consumerPage, ACCOUNTS.consumer);
    await consumerPage.goto("http://localhost:3000/admin/restaurants");

    await expect(consumerPage.getByText("這個頁面只有管理員能存取。")).toBeVisible();
    await context.close();
  });
});
