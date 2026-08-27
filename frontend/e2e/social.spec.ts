import { expect, test } from "@playwright/test";
import { ACCOUNTS, createPost, ensureNotFollowing, loginAs } from "./helpers";

test.describe("forum", () => {
  test.beforeEach(async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
  });

  /** The compose form itself, driven through the UI. */
  test("a member can publish a post and see it in the list", async ({ page }) => {
    const body = `E2E 測試文章 ${Date.now()}`;

    await page.goto("/forum/new");
    await page.getByLabel("內容").fill(body);
    await page.getByRole("button", { name: "發表", exact: true }).click();

    await expect(page).toHaveURL("/forum");
    await expect(page.getByText(body)).toBeVisible();
  });

  test("a comment and a reply appear on the post", async ({ page }) => {
    const id = await createPost(page, `留言測試 ${Date.now()}`);
    await page.goto(`/forum/${id}`);

    await page.getByLabel("留言內容").fill("這是一則留言");
    await page.getByRole("button", { name: "送出留言" }).click();
    await expect(page.getByText("這是一則留言")).toBeVisible();

    await page.getByRole("button", { name: "回覆", exact: true }).first().click();
    await page.getByLabel("回覆內容").fill("這是一則回覆");
    // "送出" alone also matches "送出留言", so anchor it.
    await page.getByRole("button", { name: "送出", exact: true }).click();

    await expect(page.getByText("這是一則回覆")).toBeVisible();
  });

  test("bookmarking a post puts it in 我的收藏", async ({ page }) => {
    const body = `收藏測試 ${Date.now()}`;
    const id = await createPost(page, body);
    await page.goto(`/forum/${id}`);

    await page.getByRole("button", { name: /^收藏/ }).click();
    await expect(page.getByRole("button", { name: /^已收藏/ })).toBeVisible();

    await page.goto("/forum");
    await page.getByRole("button", { name: "我的收藏" }).click();
    await expect(page.getByText(body)).toBeVisible();
  });

  test("the author can delete their own post", async ({ page }) => {
    const body = `刪除測試 ${Date.now()}`;
    const id = await createPost(page, body);
    await page.goto(`/forum/${id}`);

    page.on("dialog", (dialog) => dialog.accept());
    await page.getByRole("button", { name: "刪除文章" }).click();

    await expect(page).toHaveURL("/forum");
    await expect(page.getByText(body)).toHaveCount(0);
  });

  test("a logged-out reader sees the post but no compose or delete controls", async ({ page, browser }) => {
    const body = `唯讀測試 ${Date.now()}`;
    const id = await createPost(page, body);

    // A separate context, so the signed-in init script does not apply.
    const anonymous = await browser.newContext();
    const anonymousPage = await anonymous.newPage();
    await anonymousPage.goto(`http://localhost:3000/forum/${id}`);

    await expect(anonymousPage.getByText(body)).toBeVisible();
    await expect(anonymousPage.getByRole("button", { name: "刪除文章" })).toHaveCount(0);
    await expect(anonymousPage.getByLabel("留言內容")).toHaveCount(0);
    await anonymous.close();
  });
});

test.describe("member pages", () => {
  test.beforeEach(async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
  });

  test("a wall message can be posted and liked", async ({ page }) => {
    const body = `牆上留言 ${Date.now()}`;
    await page.goto("/members/6"); // 偉哥

    await page.getByLabel("留言內容").fill(body);
    await page.getByRole("button", { name: "留言", exact: true }).click();
    await expect(page.getByText(body)).toBeVisible();

    // Wall messages are list items now (they were divs inside a <ul>).
    const message = page.locator("li", { hasText: body }).first();
    await message.getByRole("button", { name: /^按讚/ }).click();
    await expect(message.getByRole("button", { name: /收回讚，目前 1 個讚/ })).toBeVisible();
  });

  test("following and unfollowing updates the button state", async ({ page }) => {
    await ensureNotFollowing(page, 7); // 玲玲
    await page.goto("/members/7");

    await page.getByRole("button", { name: "追蹤", exact: true }).click();
    await expect(page.getByRole("button", { name: "已追蹤", exact: true })).toBeVisible();

    await page.getByRole("button", { name: "已追蹤", exact: true }).click();
    await expect(page.getByRole("button", { name: "追蹤", exact: true })).toBeVisible();
  });

  test("the friend list shows accepted friends", async ({ page }) => {
    await page.goto("/friends");

    await expect(page.getByRole("heading", { name: "好友", exact: true })).toBeVisible();
    await expect(page.getByRole("heading", { name: /我的好友/ })).toBeVisible();
  });
});
