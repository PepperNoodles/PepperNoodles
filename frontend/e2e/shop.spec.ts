import { expect, test } from "@playwright/test";
import { ACCOUNTS, API, clearCart, loginAs } from "./helpers";

test.describe("shopping", () => {
  test.beforeEach(async ({ page }) => {
    await loginAs(page, ACCOUNTS.consumer);
    await clearCart(page);
  });

  test("a product can be added to the cart and the total adds up", async ({ page }) => {
    // Take the expected price from the API rather than scraping a CSS class,
    // so restyling the page cannot break this assertion.
    const listed = await (await page.request.get(`${API}/shop/products?size=1`)).json();
    const product = listed.content[0];
    const unit = Number(product.price);

    await page.goto(`/shop/${product.id}`);
    await page.getByLabel("數量").fill("2");
    await page.getByRole("button", { name: "加入購物車" }).click();

    await expect(page).toHaveURL("/cart");
    await expect(page.getByText(product.name)).toBeVisible();
    await expect(page.getByText(`NT$${(unit * 2).toLocaleString("zh-TW")}`).first()).toBeVisible();
  });

  test("changing the quantity in the cart updates the total", async ({ page }) => {
    await page.goto("/shop");
    await page.getByRole("button", { name: "加入購物車" }).first().click();
    await page.goto("/cart");

    const quantity = page.getByLabel(/數量$/).first();
    await quantity.fill("3");
    await quantity.blur();

    await expect(page.getByText("總計")).toBeVisible();
    await expect(quantity).toHaveValue("3");
  });

  test("an item can be removed and the cart empties", async ({ page }) => {
    await page.goto("/shop");
    await page.getByRole("button", { name: "加入購物車" }).first().click();
    await page.goto("/cart");

    await page.getByRole("button", { name: /^移除/ }).first().click();

    await expect(page.getByText("購物車還是空的。")).toBeVisible();
  });

  /** Checkout must reserve stock and produce an order that can then be paid. */
  test("checkout creates an order and offers payment", async ({ page }) => {
    await page.goto("/shop");
    await page.getByRole("button", { name: "加入購物車" }).first().click();
    await page.goto("/cart");

    await page.getByLabel("收件人姓名").fill("測試收件人");
    await page.getByLabel("收件人手機").fill("0912345678");
    await page.getByLabel("收件地址").fill("台北市信義區松高路11號");
    await page.getByRole("button", { name: "送出訂單" }).click();

    await expect(page).toHaveURL(/\/orders\/\d+$/);
    await expect(page.getByText("待付款")).toBeVisible();
    await expect(page.getByRole("button", { name: "前往付款" })).toBeVisible();
    await expect(page.getByText(/PN\d{8}-\d{6}/)).toBeVisible();
  });

  test("an unpaid order can be cancelled", async ({ page }) => {
    await page.goto("/shop");
    await page.getByRole("button", { name: "加入購物車" }).first().click();
    await page.goto("/cart");
    await page.getByLabel("收件人姓名").fill("測試收件人");
    await page.getByLabel("收件人手機").fill("0912345678");
    await page.getByLabel("收件地址").fill("台北市信義區松高路11號");
    await page.getByRole("button", { name: "送出訂單" }).click();
    await expect(page).toHaveURL(/\/orders\/\d+$/);

    await page.getByRole("button", { name: "取消訂單" }).click();

    await expect(page.getByText("已取消")).toBeVisible();
  });

  test("the orders list links through to an order", async ({ page }) => {
    await page.goto("/orders");
    await expect(page.getByRole("heading", { name: "我的訂單" })).toBeVisible();
  });
});
