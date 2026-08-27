import { expect, test } from "@playwright/test";
import { ACCOUNTS, API, authHeaders, createPost, loginAs } from "./helpers";

/** The five screens that existed only as endpoints until now. */

test.describe("restaurant review replies", () => {
  test("a diner can reply to a review and delete their own reply", async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
    await page.goto("/restaurants/8");

    await page.getByRole("button", { name: "回覆", exact: true }).first().click();
    const body = `E2E 回覆 ${Date.now()}`;
    await page.getByLabel("回覆內容").fill(body);
    await page.getByRole("button", { name: "送出", exact: true }).click();

    await expect(page.getByText(body)).toBeVisible();

    await page
      .locator("li li", { hasText: body }) // a reply is a list item inside a review
      .getByRole("button", { name: "刪除" })
      .click();
    await expect(page.getByText(body)).toHaveCount(0);
  });

  /** The owner's reply is badged as coming from the shop. */
  test("a reply from the restaurant owner is marked 店家", async ({ page }) => {
    await loginAs(page, ACCOUNTS.owner); // owner.chun owns restaurant 8
    await page.goto("/restaurants/8");

    await page.getByRole("button", { name: "回覆", exact: true }).first().click();
    const body = `謝謝光臨 ${Date.now()}`;
    await page.getByLabel("回覆內容").fill(body);
    await page.getByRole("button", { name: "送出", exact: true }).click();

    const reply = page.locator("li li", { hasText: body }); // reply inside a review
    await expect(reply).toBeVisible();
    await expect(reply.getByText("店家回覆", { exact: true })).toBeVisible();

    await reply.getByRole("button", { name: "刪除" }).click();
  });

  test("a logged-out visitor gets no reply control", async ({ page }) => {
    await page.goto("/restaurants/8");
    await expect(page.getByRole("button", { name: "回覆", exact: true })).toHaveCount(0);
  });
});

test.describe("avatar upload", () => {
  test("uploading a picture replaces the placeholder", async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
    await page.goto("/profile");

    await expect(page.getByRole("heading", { name: "大頭貼" })).toBeVisible();

    // A one-pixel PNG is enough to exercise the multipart path end to end.
    await page.getByLabel("上傳大頭貼").setInputFiles({
      name: "avatar.png",
      mimeType: "image/png",
      buffer: Buffer.from(
        "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==",
        "base64",
      ),
    });

    await expect(page.getByAltText("目前的大頭貼")).toBeVisible({ timeout: 15_000 });
  });
});

test.describe("audit log", () => {
  test("an admin can read who did what", async ({ page }) => {
    await loginAs(page, ACCOUNTS.admin);

    // Produce an entry to read back.
    const headers = await authHeaders(page, ACCOUNTS.admin);
    await page.request.post(`${API}/admin/users/6/suspend`, {
      headers,
      data: { reason: "E2E 稽核測試" },
    });
    await page.request.post(`${API}/admin/users/6/reinstate`, { headers });

    await page.goto("/admin");
    await page.getByRole("link", { name: "操作紀錄" }).click();

    await expect(page).toHaveURL("/admin/audit-log");
    await expect(page.getByRole("heading", { name: "操作紀錄" })).toBeVisible();
    await expect(page.getByText("停權會員").filter({ visible: true }).first()).toBeVisible();
    // The reason is stored as JSON and unwrapped for display.
    await expect(page.getByText("E2E 稽核測試").filter({ visible: true }).first()).toBeVisible();
  });

  test("a consumer is refused the audit log", async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
    await page.goto("/admin/audit-log");

    await expect(page.getByText("這個頁面只有管理員能存取。")).toBeVisible();
  });
});

test.describe("resend verification", () => {
  test("the option appears after a failed sign-in and confirms without leaking", async ({ page }) => {
    await page.goto("/login");

    await page.getByLabel("電子信箱").fill("pending@example.com");
    await page.getByLabel("密碼").fill("WrongPassword1!");
    await page.getByRole("button", { name: "登入" }).click();

    const resend = page.getByRole("button", { name: /重寄驗證信/ });
    await expect(resend).toBeVisible();

    await resend.click();
    await expect(page.getByText(/若這個信箱尚未驗證/)).toBeVisible();
  });
});

test.describe("forum post editing", () => {
  test("the author can edit the body and see the change", async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
    const id = await createPost(page, `編輯前 ${Date.now()}`);

    await page.goto(`/forum/${id}`);
    await page.getByRole("link", { name: "編輯文章" }).click();
    await expect(page).toHaveURL(`/forum/${id}/edit`);

    const updated = `編輯後 ${Date.now()}`;
    await page.getByLabel("內容").fill(updated);
    await page.getByRole("button", { name: "儲存變更" }).click();

    await expect(page).toHaveURL(`/forum/${id}`);
    await expect(page.getByText(updated)).toBeVisible();
  });

  test("an image can be attached to a post", async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
    const id = await createPost(page, `配圖測試 ${Date.now()}`);

    await page.goto(`/forum/${id}/edit`);
    await page.getByLabel("上傳文章配圖").setInputFiles({
      name: "cover.png",
      mimeType: "image/png",
      buffer: Buffer.from(
        "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==",
        "base64",
      ),
    });

    await expect(page.getByText("尚無配圖")).toHaveCount(0, { timeout: 15_000 });
  });

  test("a non-author cannot open the edit page", async ({ page, browser }) => {
    await loginAs(page, ACCOUNTS.consumer);
    const id = await createPost(page, `他人文章 ${Date.now()}`);

    const context = await browser.newContext();
    const otherPage = await context.newPage();
    await loginAs(otherPage, ACCOUNTS.owner);
    await otherPage.goto(`http://localhost:3000/forum/${id}/edit`);

    await expect(otherPage.getByText("只有作者本人可以編輯這篇文章。")).toBeVisible();
    await context.close();
  });
});
