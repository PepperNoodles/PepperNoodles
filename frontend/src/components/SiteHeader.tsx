"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, useRef, useState } from "react";
import { useAuth } from "./AuthProvider";
import { Logo } from "./Logo";
import { api } from "@/lib/api";
import type { Role } from "@/lib/types";
import { IconCart, IconClose, IconMenu, IconUser } from "./icons";

/**
 * The 2021 header: a circular PepperNoodle mark on the left and white nav links,
 * laid transparently over the hero photograph. Pages without a hero get an
 * opaque bar instead, otherwise white-on-white links would be invisible.
 *
 * <p>The first rebuild put all ten links in one unwrapped flex row, which
 * overflowed the viewport below roughly 1100px and scrolled the whole document
 * sideways on a phone. Above `lg` the links stay inline; below it they move
 * into a drawer.
 */

interface NavItem {
  href: string;
  label: string;
  /** Which roles may see it; undefined means everyone. */
  roles?: Role[];
  authed?: boolean;
}

const NAV: NavItem[] = [
  { href: "/map", label: "地圖" },
  { href: "/restaurants", label: "餐廳" },
  { href: "/shop", label: "商城" },
  { href: "/forum", label: "專欄" },
  { href: "/friends", label: "好友", authed: true },
  { href: "/company", label: "管理", roles: ["ROLE_COMPANY", "ROLE_ADMIN"] },
  { href: "/admin", label: "後台", roles: ["ROLE_ADMIN"] },
];

export function SiteHeader() {
  const { user, loading, logout, hasRole } = useAuth();
  const router = useRouter();
  const pathname = usePathname();
  const [cartCount, setCartCount] = useState(0);
  /*
   * The drawer is stored as "the path it was opened on" rather than a boolean.
   * A navigation then closes it by simply making this no longer match, instead
   * of needing an effect that calls setState on every route change.
   */
  const [openedOn, setOpenedOn] = useState<string | null>(null);
  const open = openedOn === pathname;
  const menuButton = useRef<HTMLButtonElement>(null);

  const overHero = pathname === "/" || pathname === "/shop";

  const items = NAV.filter(
    (item) => (!item.authed || user) && (!item.roles || hasRole(...item.roles)),
  );

  useEffect(() => {
    if (!user) {
      setCartCount(0);
      return;
    }
    api
      .get<{ items: unknown[] }>("/cart")
      .then((cart) => setCartCount(cart.items.length))
      .catch(() => setCartCount(0));
  }, [user, pathname]);

  // Escape closes it and hands focus back to the control that opened it —
  // otherwise focus is left on a node that no longer exists.
  useEffect(() => {
    if (!open) return;
    function onKey(event: KeyboardEvent) {
      if (event.key === "Escape") {
        setOpenedOn(null);
        menuButton.current?.focus();
      }
    }
    document.addEventListener("keydown", onKey);
    // The page behind a full-screen drawer must not scroll.
    document.body.style.overflow = "hidden";
    return () => {
      document.removeEventListener("keydown", onKey);
      document.body.style.overflow = "";
    };
  }, [open]);

  const linkBase =
    "relative rounded-lg px-3 py-2 text-[15px] font-medium transition duration-200 after:absolute after:inset-x-3 after:-bottom-0.5 after:h-0.5 after:origin-left after:scale-x-0 after:rounded-full after:transition-transform after:duration-200 hover:after:scale-x-100";

  const linkClass = overHero
    ? `${linkBase} text-white/90 hover:text-white after:bg-mint`
    : `${linkBase} text-body hover:text-ink after:bg-pepper`;

  /** The current section keeps its underline drawn and is announced as current. */
  function navProps(href: string) {
    const active = pathname === href || pathname.startsWith(`${href}/`);
    return {
      className: `${linkClass} ${active ? "after:scale-x-100 " + (overHero ? "text-white" : "text-ink") : ""}`,
      "aria-current": active ? ("page" as const) : undefined,
    };
  }

  return (
    /*
      The drawer is a SIBLING of <header>, not a child.
      `backdrop-filter` (the header's `backdrop-blur-md` on non-hero pages)
      establishes a containing block for `position: fixed` descendants, so a
      `fixed inset-0` overlay nested inside the header resolved against the
      header's own 76px-tall box instead of the viewport — the drawer opened
      clipped to a sliver. Pages with the hero header have no backdrop filter,
      which is why it looked fine on / and /shop.
    */
    <>
      <header
        className={
          overHero
            ? "absolute inset-x-0 top-0 z-40"
            : "sticky top-0 z-40 border-b border-line bg-white/85 backdrop-blur-md"
        }
      >
        <nav className="mx-auto flex max-w-7xl items-center gap-1 px-5 py-4 sm:px-6" aria-label="主選單">
          <Link href="/" className="mr-6 shrink-0 xl:mr-8" aria-label="胡椒MAP 首頁">
            <Logo onDark={overHero} />
          </Link>

          {/* Inline links — only where they genuinely fit. */}
          <div className="hidden items-center gap-0.5 lg:flex">
            {items.map((item) => (
              <Link key={item.href} href={item.href} {...navProps(item.href)}>
                {item.label}
              </Link>
            ))}
          </div>

          <div className="ml-auto flex items-center gap-1.5">
            {user && (
              <Link
                href="/cart"
                className={`relative flex h-11 w-11 items-center justify-center rounded-full text-xl transition ${
                  overHero ? "text-white hover:bg-white/15" : "text-body hover:bg-mist hover:text-ink"
                }`}
                aria-label={cartCount > 0 ? `購物車，${cartCount} 件商品` : "購物車"}
              >
                <IconCart />
                {cartCount > 0 && (
                  <span
                    aria-hidden
                    className="absolute right-1 top-1 flex h-4.5 min-w-4.5 items-center justify-center rounded-full bg-pepper-fill px-1 text-xs font-bold tabular text-white ring-2 ring-white"
                  >
                    {cartCount}
                  </span>
                )}
              </Link>
            )}

            {loading ? (
              <div className={`h-10 w-24 animate-pulse rounded-full ${overHero ? "bg-white/20" : "bg-mist"}`} />
            ) : user ? (
              <div className="hidden items-center gap-1.5 lg:flex">
                <Link
                  href="/profile"
                  className={`flex items-center gap-2 rounded-full py-2 pl-2 pr-4 text-sm font-medium transition ${
                    overHero ? "text-white hover:bg-white/15" : "text-body hover:bg-mist hover:text-ink"
                  }`}
                >
                  <Avatar url={user.avatarPath} name={user.displayName} />
                  <span className="max-w-28 truncate">{user.displayName}</span>
                </Link>
                <button
                  onClick={async () => {
                    await logout();
                    router.push("/");
                  }}
                  className={`cursor-pointer rounded-full border px-4 py-2 font-display text-sm font-bold uppercase transition ${
                    overHero
                      ? "border-white/50 text-white hover:bg-white/15"
                      : "border-line-strong text-ink hover:border-ink hover:bg-mist"
                  }`}
                >
                  登出
                </button>
              </div>
            ) : (
              <div className="hidden items-center gap-1.5 lg:flex">
                <Link href="/login" className={linkClass}>
                  登入
                </Link>
                <Link
                  href="/register"
                  className="rounded-full bg-pepper-fill px-5 py-2.5 font-display text-sm font-bold uppercase tracking-wide text-white shadow-card transition hover:bg-pepper-dark"
                >
                  註冊
                </Link>
              </div>
            )}

            <button
              ref={menuButton}
              type="button"
              onClick={() => setOpenedOn(pathname)}
              aria-label="開啟選單"
              aria-expanded={open}
              aria-controls="mobile-nav"
              className={`flex h-11 w-11 cursor-pointer items-center justify-center rounded-full text-2xl transition lg:hidden ${
                overHero ? "text-white hover:bg-white/15" : "text-ink hover:bg-mist"
              }`}
            >
              <IconMenu />
            </button>
          </div>
        </nav>
      </header>

      {open && (
        <div className="fixed inset-0 z-50 lg:hidden">
          <button
            type="button"
            tabIndex={-1}
            aria-hidden
            onClick={() => setOpenedOn(null)}
            className="absolute inset-0 h-full w-full cursor-default bg-ink/50 backdrop-blur-sm"
          />
          <div
            id="mobile-nav"
            role="dialog"
            aria-modal="true"
            aria-label="主選單"
            className="animate-rise absolute inset-y-0 right-0 flex w-[min(20rem,88vw)] flex-col overflow-y-auto bg-white shadow-pop"
          >
            <div className="flex items-center justify-between border-b border-line px-5 py-4">
              <span className="font-display text-lg font-bold text-ink">選單</span>
              <button
                type="button"
                autoFocus
                onClick={() => {
                  setOpenedOn(null);
                  menuButton.current?.focus();
                }}
                aria-label="關閉選單"
                className="flex h-11 w-11 cursor-pointer items-center justify-center rounded-full text-2xl text-body transition hover:bg-mist hover:text-ink"
              >
                <IconClose />
              </button>
            </div>

            <div className="flex flex-1 flex-col gap-1 p-4">
              {items.map((item) => {
                const active = pathname === item.href || pathname.startsWith(`${item.href}/`);
                return (
                  <Link
                    key={item.href}
                    href={item.href}
                    aria-current={active ? "page" : undefined}
                    className={`flex min-h-12 items-center rounded-xl px-4 text-[15px] font-medium transition ${
                      active ? "bg-pepper-tint text-pepper-ink" : "text-body hover:bg-mist hover:text-ink"
                    }`}
                  >
                    {item.label}
                  </Link>
                );
              })}
            </div>

            <div className="border-t border-line p-4">
              {user ? (
                <>
                  <Link
                    href="/profile"
                    className="mb-2 flex min-h-12 items-center gap-3 rounded-xl px-4 text-[15px] font-medium text-body transition hover:bg-mist hover:text-ink"
                  >
                    <Avatar url={user.avatarPath} name={user.displayName} />
                    <span className="truncate">{user.displayName}</span>
                  </Link>
                  <button
                    onClick={async () => {
                      await logout();
                      router.push("/");
                    }}
                    className="min-h-12 w-full cursor-pointer rounded-xl border border-line-strong px-4 font-display text-sm font-bold uppercase text-ink transition hover:bg-mist"
                  >
                    登出
                  </button>
                </>
              ) : (
                <div className="flex flex-col gap-2">
                  <Link
                    href="/login"
                    className="flex min-h-12 items-center justify-center rounded-xl border border-line-strong font-display text-sm font-bold uppercase text-ink transition hover:bg-mist"
                  >
                    登入
                  </Link>
                  <Link
                    href="/register"
                    className="flex min-h-12 items-center justify-center rounded-xl bg-pepper-fill font-display text-sm font-bold uppercase text-white transition hover:bg-pepper-dark"
                  >
                    註冊
                  </Link>
                </div>
              )}
            </div>
          </div>
        </div>
      )}
    </>
  );
}

/** Small round avatar with a drawn fallback — never a letterbox emoji. */
function Avatar({ url, name }: { url?: string | null; name: string }) {
  if (url) {
    return (
      // eslint-disable-next-line @next/next/no-img-element
      <img src={url} alt="" className="h-8 w-8 shrink-0 rounded-full object-cover ring-1 ring-line" />
    );
  }
  return (
    <span
      aria-hidden
      title={name}
      className="flex h-8 w-8 shrink-0 items-center justify-center rounded-full bg-mist text-base text-subtle ring-1 ring-line"
    >
      <IconUser />
    </span>
  );
}
