import { expect, test } from "@playwright/test";
import { ACCOUNTS, loginAs } from "./helpers";

test.describe("store management", () => {
  test.beforeEach(async ({ page }) => {
    await loginAs(page, ACCOUNTS.owner);
  });

  test("the hub lists the owner's restaurants and products", async ({ page }) => {
    await page.goto("/company");

    await expect(page.getByRole("heading", { name: "店家管理" })).toBeVisible();
    await expect(page.getByRole("heading", { name: /我的餐廳/ })).toBeVisible();
    await expect(page.getByRole("heading", { name: /我的商品/ })).toBeVisible();
    await expect(page.getByRole("link", { name: "登錄餐廳" })).toBeVisible();
  });

  /** Regression: hours were rendering as three identical shifted rows per day. */
  test("the edit form loads real business hours, one row per day", async ({ page }) => {
    await page.goto("/company");
    await page.getByText("管理這間餐廳").first().click();

    await expect(page.getByRole("heading", { name: /^管理「/ })).toBeVisible();
    await expect(page.getByRole("heading", { name: "營業時間" })).toBeVisible();

    const opens = page.getByLabel("週一 開始時間");
    await expect(opens).toHaveCount(1);
    await expect(opens).toHaveValue("10:00");
    await expect(page.getByLabel("週一 結束時間")).toHaveValue("22:00");
  });

  test("an event can be added and removed", async ({ page }) => {
    const name = `E2E 活動 ${Date.now()}`;
    await page.goto("/company");
    await page.getByText("管理這間餐廳").first().click();

    await page.getByLabel("活動名稱").fill(name);
    await page.getByLabel("活動說明").fill("測試用活動");
    await page.getByLabel("開始日期").fill("2026-08-18");
    await page.getByLabel("結束日期").fill("2026-08-25");
    await page.getByRole("button", { name: "新增活動" }).click();

    await expect(page.getByText(name)).toBeVisible();

    await page
      .locator("li", { hasText: name })
      .getByRole("button", { name: "刪除" })
      .click();
    await expect(page.getByText(name)).toHaveCount(0);
  });

  test("the sales report renders totals and a chart", async ({ page }) => {
    await page.goto("/company/reports");

    await expect(page.getByRole("heading", { name: "銷售報表" })).toBeVisible();
    await expect(page.getByText("總營收")).toBeVisible();
    await expect(page.getByRole("img", { name: "每月營收長條圖" })).toBeVisible();
  });

  test("the product form is reachable and requires a restaurant", async ({ page }) => {
    await page.goto("/company/products/new");

    await expect(page.getByRole("heading", { name: "新增商品" })).toBeVisible();
    await expect(page.getByLabel("所屬餐廳")).toBeVisible();
    await expect(page.getByLabel("商品名稱")).toBeVisible();
  });

  test("a consumer cannot reach the company hub", async ({ browser }) => {
    // Fresh context: beforeEach already injected the owner's tokens into `page`.
    const context = await browser.newContext();
    const consumerPage = await context.newPage();
    await loginAs(consumerPage, ACCOUNTS.consumer);
    await consumerPage.goto("http://localhost:3000/company");

    await expect(consumerPage.getByText("這個頁面只有企業會員能存取。")).toBeVisible();
    await context.close();
  });
});

test.describe("back office", () => {
  test("an admin sees the dashboard, inquiries and member list", async ({ page }) => {
    await loginAs(page, ACCOUNTS.admin);
    await page.goto("/admin");

    await expect(page.getByRole("heading", { name: "後台" })).toBeVisible();
    await expect(page.getByText("會員總數")).toBeVisible();
    await expect(page.getByRole("heading", { name: /聯絡我們/ })).toBeVisible();
    await expect(page.getByRole("heading", { name: "會員管理" })).toBeVisible();
  });

  test("a consumer is refused the back office", async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
    await page.goto("/admin");

    await expect(page.getByText("這個頁面只有管理員能存取。")).toBeVisible();
  });
});
