import { expect, test } from "@playwright/test";

/**
 * The mobile navigation drawer.
 *
 * <p>These run against more than one page on purpose. The drawer was first
 * checked only on the home page and looked fine there, while being broken
 * everywhere else: the home and shop headers lie over a hero photograph with no
 * backdrop filter, but every other page's header carries `backdrop-blur`, and a
 * backdrop filter establishes a containing block for `position: fixed`. The
 * drawer therefore resolved against the 76px-tall header instead of the
 * viewport and opened as a sliver.
 *
 * <p>The map page is included separately because Leaflet numbers its panes from
 * z-index 400 and its container establishes no stacking context, so the map
 * painted straight over the drawer even once the drawer was the right size.
 */

const PAGES = [
  { path: "/", note: "hero header, no backdrop filter" },
  { path: "/map", note: "plain header + a Leaflet map underneath" },
  { path: "/restaurants", note: "plain header" },
  { path: "/forum", note: "plain header" },
];

test.use({ viewport: { width: 440, height: 956 } });

for (const { path, note } of PAGES) {
  test(`the drawer opens fully on ${path} (${note})`, async ({ page }) => {
    await page.goto(path);
    await page.getByRole("button", { name: "開啟選單" }).click();

    const drawer = page.getByRole("dialog", { name: "主選單" });
    await expect(drawer).toBeVisible();

    // It must fill the viewport height, not the header's.
    const box = await drawer.boundingBox();
    expect(box, "drawer has no box").not.toBeNull();
    expect(box!.height, "drawer is not full height").toBeGreaterThan(900);

    // Every destination has to be reachable, not merely present: an element
    // painted over by the map is "visible" to the DOM but not to the user.
    for (const label of ["地圖", "餐廳", "商城", "專欄"]) {
      const link = drawer.getByRole("link", { name: label, exact: true });
      await expect(link).toBeVisible();
      const b = (await link.boundingBox())!;
      const onTop = await page.evaluate(
        ([x, y]) => {
          const el = document.elementFromPoint(x, y);
          return !!el?.closest("#mobile-nav");
        },
        [b.x + b.width / 2, b.y + b.height / 2],
      );
      expect(onTop, `「${label}」is covered by something above the drawer`).toBe(true);
    }

    // And it actually navigates.
    await drawer.getByRole("link", { name: "餐廳", exact: true }).click();
    await expect(page).toHaveURL("/restaurants");
  });
}

test("escape closes the drawer and returns focus to the button", async ({ page }) => {
  await page.goto("/map");
  const opener = page.getByRole("button", { name: "開啟選單" });
  await opener.click();
  await expect(page.getByRole("dialog", { name: "主選單" })).toBeVisible();

  await page.keyboard.press("Escape");
  await expect(page.getByRole("dialog", { name: "主選單" })).toBeHidden();
  await expect(opener).toBeFocused();
});
