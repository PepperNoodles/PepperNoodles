import { expect, test } from "@playwright/test";

/** Pages a logged-out visitor can reach. */
test.describe("public browsing", () => {
  test("the home page shows the hero and the data-backed sections", async ({ page }) => {
    await page.goto("/");

    await expect(page.getByRole("heading", { name: "走吧!美食之旅!" })).toBeVisible();
    await expect(page.getByText("Explore the Food")).toBeVisible();

    // Sections restored from the 2021 index.jsp, each backed by real data.
    for (const heading of ["熱門地點", "如何搜尋", "客戶評論", "最新餐廳"]) {
      await expect(page.getByRole("heading", { name: heading })).toBeVisible();
    }
  });

  test("the hero search sends the visitor to the restaurant list", async ({ page }) => {
    await page.goto("/");

    await page.getByLabel("搜尋", { exact: true }).fill("春水堂");
    await page.getByRole("button", { name: "Search" }).click();

    await expect(page).toHaveURL(/\/restaurants\?q=/);
    await expect(page.getByText("春水堂 中山店")).toBeVisible();
  });

  test("a restaurant page shows hours, tags and reviews", async ({ page }) => {
    await page.goto("/restaurants");
    await page.getByText("春水堂 中山店").first().click();

    await expect(page.getByRole("heading", { name: "春水堂 中山店" })).toBeVisible();
    await expect(page.getByRole("heading", { name: "營業時間" })).toBeVisible();
    // Regression: hours were being shifted by the host's UTC offset.
    await expect(page.getByText("10:00–22:00").first()).toBeVisible();
    await expect(page.getByRole("heading", { name: /評論/ })).toBeVisible();
  });

  test("the shop lists products and filters by price", async ({ page }) => {
    await page.goto("/shop");

    await expect(page.getByRole("heading", { name: "New Upcoming Products" })).toBeVisible();
    await expect(page.getByText(/共 \d+ 件商品/)).toBeVisible();

    await page.getByLabel("最低").fill("300");
    await page.getByLabel("最高").fill("400");
    await page.getByRole("button", { name: "篩選" }).click();

    await expect(page.getByText(/共 \d+ 件商品/)).toBeVisible();
    const prices = await page.locator("text=/^NT\\$\\d+$/").allTextContents();
    for (const price of prices) {
      const value = Number(price.replace(/[^0-9]/g, ""));
      expect(value).toBeGreaterThanOrEqual(300);
      expect(value).toBeLessThanOrEqual(400);
    }
  });

  test("the forum is readable while logged out but offers no compose button", async ({ page }) => {
    await page.goto("/forum");

    await expect(page.getByRole("heading", { name: "專欄文章" })).toBeVisible();
    await expect(page.getByRole("link", { name: "發表文章" })).toHaveCount(0);
  });

  test("the map renders tiles and markers", async ({ page }) => {
    await page.goto("/map");

    await expect(page.getByRole("heading", { name: "美食地圖" })).toBeVisible();
    await expect(page.locator(".leaflet-container")).toBeVisible();
    await expect(page.locator(".leaflet-marker-icon").first()).toBeVisible({ timeout: 15_000 });
    await expect(page.getByText(/畫面內共 \d+ 間餐廳/)).toBeVisible();
  });

  test("protected pages send a logged-out visitor to sign in", async ({ page }) => {
    await page.goto("/profile");
    await expect(page.getByRole("link", { name: "登入" }).first()).toBeVisible();
  });
});
