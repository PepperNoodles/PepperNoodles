"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { useAuth } from "./AuthProvider";
import { Logo } from "./Logo";
import { api } from "@/lib/api";

/**
 * The 2021 header: a circular PepperNoodle mark on the left and white nav links,
 * laid transparently over the hero photograph. Pages without a hero get an
 * opaque bar instead, otherwise white-on-white links would be invisible.
 */
export function SiteHeader() {
  const { user, loading, logout, hasRole } = useAuth();
  const router = useRouter();
  const pathname = usePathname();
  const [cartCount, setCartCount] = useState(0);

  const overHero = pathname === "/" || pathname === "/shop";

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

  const linkClass = overHero
    ? "px-4 py-2 text-[15px] font-medium text-white/90 transition hover:text-mint"
    : "px-4 py-2 text-[15px] font-medium text-stone-700 transition hover:text-pepper dark:text-stone-200";

  return (
    <header
      className={
        overHero
          ? "absolute inset-x-0 top-0 z-30"
          : "sticky top-0 z-30 border-b border-stone-200 bg-white/95 backdrop-blur dark:border-stone-800 dark:bg-stone-950/95"
      }
    >
      <nav className="mx-auto flex max-w-7xl items-center gap-1 px-6 py-4">
        <Link href="/" className="mr-8 shrink-0" aria-label="胡椒MAP 首頁">
          <Logo onDark={overHero} />
        </Link>

        <Link href="/map" className={linkClass}>
          地圖
        </Link>
        <Link href="/restaurants" className={linkClass}>
          餐廳
        </Link>
        <Link href="/shop" className={linkClass}>
          商城
        </Link>
        <Link href="/forum" className={linkClass}>
          專欄
        </Link>
        {user && (
          <Link href="/friends" className={linkClass}>
            好友
          </Link>
        )}
        {hasRole("ROLE_COMPANY", "ROLE_ADMIN") && (
          <Link href="/company" className={linkClass}>
            管理
          </Link>
        )}
        {hasRole("ROLE_ADMIN") && (
          <Link href="/admin" className={linkClass}>
            後台
          </Link>
        )}

        <div className="ml-auto flex items-center gap-2">
          {user && (
            <Link href="/cart" className={`relative ${linkClass}`}>
              購物車
              {cartCount > 0 && (
                <span className="absolute right-0 top-1 flex h-5 min-w-5 items-center justify-center rounded-full bg-pepper px-1 text-[11px] font-bold text-white">
                  {cartCount}
                </span>
              )}
            </Link>
          )}

          {loading ? (
            <div className="h-9 w-24 animate-pulse rounded-full bg-white/20" />
          ) : user ? (
            <>
              <Link href="/profile" className={linkClass}>
                {user.displayName}
              </Link>
              <button
                onClick={async () => {
                  await logout();
                  router.push("/");
                }}
                className={`rounded-full px-4 py-1.5 font-display text-sm font-bold uppercase transition ${
                  overHero
                    ? "border border-white/50 text-white hover:bg-white/10"
                    : "border border-stone-300 text-stone-700 hover:bg-stone-100 dark:border-stone-700 dark:text-stone-200"
                }`}
              >
                登出
              </button>
            </>
          ) : (
            <>
              <Link href="/login" className={linkClass}>
                登入
              </Link>
              <Link
                href="/register"
                className="rounded-full bg-pepper px-5 py-2 font-display text-sm font-bold uppercase tracking-wide text-white transition hover:bg-pepper-dark"
              >
                註冊
              </Link>
            </>
          )}
        </div>
      </nav>
    </header>
  );
}
