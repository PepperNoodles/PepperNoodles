import { expect, test } from "@playwright/test";
import { ACCOUNTS, API, authHeaders, loginAs } from "./helpers";

/**
 * 好友聊天 over the live socket.
 *
 * <p>mei (id 5) and 偉哥 (id 6) are seeded as accepted friends, which the
 * backend requires before either may message the other.
 */
test.describe("live chat", () => {
  test("the socket reports itself connected", async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
    await page.goto("/friends/6");

    // Was a five-second poll before; this only passes if STOMP actually connects.
    await expect(page.getByText("已連線")).toBeVisible({ timeout: 15_000 });
  });

  test("a message sent from the other side arrives without a reload", async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
    await page.goto("/friends/6");
    await expect(page.getByText("已連線")).toBeVisible({ timeout: 15_000 });

    const body = `即時訊息 ${Date.now()}`;
    // 偉哥 posts over the API; mei's open page should receive the push.
    const response = await page.request.post(`${API}/chat/messages`, {
      headers: await authHeaders(page, { email: "wei@example.com", password: "Password123!" }),
      data: { recipientId: 5, body },
    });
    expect(response.ok()).toBeTruthy();

    await expect(page.getByText(body)).toBeVisible({ timeout: 15_000 });
  });

  test("sending from the page shows the message immediately", async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
    await page.goto("/friends/6");
    await expect(page.getByText("已連線")).toBeVisible({ timeout: 15_000 });

    const body = `我送出的 ${Date.now()}`;
    await page.getByLabel("訊息內容").fill(body);
    await page.getByRole("button", { name: "送出" }).click();

    await expect(page.getByText(body)).toBeVisible();
    // Not duplicated when the socket echoes the same message back.
    await expect(page.getByText(body)).toHaveCount(1);
  });

  test("history still loads for a conversation", async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
    await page.goto("/friends/6");

    await expect(page.getByRole("heading", { name: "聊天室" })).toBeVisible();
    await expect(page.getByLabel("訊息內容")).toBeVisible();
  });
})
