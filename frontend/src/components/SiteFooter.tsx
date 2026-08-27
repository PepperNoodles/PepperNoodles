import Link from "next/link";
import { IconMail, IconMapPin } from "./icons";

/**
 * Site footer.
 *
 * <p>The first rebuild had three links on one centred line. A footer is where
 * people look once the page above has failed them, so it carries the full map
 * of the site instead — grouped, labelled, and reachable from every page.
 */

const COLUMNS: { heading: string; links: { href: string; label: string }[] }[] = [
  {
    heading: "探索",
    links: [
      { href: "/map", label: "美食地圖" },
      { href: "/restaurants", label: "餐廳列表" },
      { href: "/forum", label: "專欄文章" },
    ],
  },
  {
    heading: "購物",
    links: [
      { href: "/shop", label: "商城" },
      { href: "/cart", label: "購物車" },
      { href: "/orders", label: "我的訂單" },
    ],
  },
  {
    heading: "帳號",
    links: [
      { href: "/profile", label: "個人資料" },
      { href: "/friends", label: "好友與訊息" },
      { href: "/register/company", label: "企業註冊" },
    ],
  },
];

export function SiteFooter() {
  return (
    <footer className="mt-20 border-t border-line bg-mist">
      <div className="mx-auto max-w-7xl px-5 py-14 sm:px-6">
        <div className="grid gap-10 sm:grid-cols-2 lg:grid-cols-4">
          <div>
            {/* Deliberately not the hero's "Explore the Food" — one page
                should not carry the same script flourish twice. */}
            <span aria-hidden className="font-script text-3xl leading-none text-pepper">
              Since 2021
            </span>
            <p className="mt-2 font-display text-xl font-bold text-ink">胡椒MAP</p>
            <p className="measure mt-3 text-sm leading-relaxed text-body">
              尋找附近的美食、閱讀真實評論、購買店家商品。
            </p>
            <p className="mt-4 flex items-center gap-2 text-sm text-subtle">
              <IconMapPin className="text-base" />
              台北市
            </p>
          </div>

          {COLUMNS.map((column) => (
            <nav key={column.heading} aria-label={column.heading}>
              <h2 className="font-display text-sm font-bold uppercase tracking-wider text-ink">{column.heading}</h2>
              <ul className="mt-3 sm:mt-4 sm:space-y-2.5">
                {column.links.map((link) => (
                  <li key={link.href}>
                    <Link
                      href={link.href}
                      className="-mx-2 inline-flex min-h-11 items-center rounded-lg px-2 text-sm text-body transition hover:bg-white hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-4"
                    >
                      {link.label}
                    </Link>
                  </li>
                ))}
              </ul>
            </nav>
          ))}
        </div>

        <div className="mt-12 flex flex-col gap-4 border-t border-line pt-6 sm:flex-row sm:items-center sm:justify-between">
          <p className="text-xs text-subtle">胡椒MAP · 重構自 2021 年 EEIT23 專案</p>
          <Link
            href="/contact"
            className="-mx-2 inline-flex min-h-11 items-center gap-2 rounded-lg px-2 text-sm font-medium text-body transition hover:bg-white hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
          >
            <IconMail className="text-base" />
            聯絡我們
          </Link>
        </div>
      </div>
    </footer>
  );
}
