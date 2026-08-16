"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { useAuth } from "./AuthProvider";
import { api } from "@/lib/api";
import { Button, NavLink } from "./ui";

export function SiteHeader() {
  const { user, loading, logout, hasRole } = useAuth();
  const router = useRouter();
  const [cartCount, setCartCount] = useState(0);

  useEffect(() => {
    if (!user) {
      setCartCount(0);
      return;
    }
    api
      .get<{ items: unknown[] }>("/cart")
      .then((cart) => setCartCount(cart.items.length))
      .catch(() => setCartCount(0));
  }, [user]);

  return (
    <header className="sticky top-0 z-50 border-b border-stone-200 bg-white/90 backdrop-blur dark:border-stone-800 dark:bg-stone-950/90">
      <nav className="mx-auto flex max-w-6xl items-center gap-1 px-4 py-3">
        <Link href="/" className="mr-4 flex items-center gap-2 text-lg font-bold">
          <span aria-hidden>🌶️</span>
          <span>胡椒MAP</span>
        </Link>

        <NavLink href="/map">地圖</NavLink>
        <NavLink href="/restaurants">餐廳</NavLink>
        <NavLink href="/shop">商城</NavLink>
        {user && <NavLink href="/friends">好友</NavLink>}
        {hasRole("ROLE_COMPANY", "ROLE_ADMIN") && <NavLink href="/company">管理</NavLink>}
        {hasRole("ROLE_ADMIN") && <NavLink href="/admin">後台</NavLink>}

        <div className="ml-auto flex items-center gap-2">
          {user && (
            <Link
              href="/cart"
              className="relative rounded-lg px-3 py-2 text-sm font-medium text-stone-600 hover:bg-stone-100 dark:text-stone-300 dark:hover:bg-stone-800"
            >
              購物車
              {cartCount > 0 && (
                <span className="absolute -right-0.5 -top-0.5 flex h-5 min-w-5 items-center justify-center rounded-full bg-red-600 px-1 text-[11px] font-semibold text-white">
                  {cartCount}
                </span>
              )}
            </Link>
          )}

          {loading ? (
            <div className="h-8 w-20 animate-pulse rounded-lg bg-stone-200 dark:bg-stone-800" />
          ) : user ? (
            <>
              <Link
                href="/profile"
                className="rounded-lg px-3 py-2 text-sm font-medium text-stone-700 hover:bg-stone-100 dark:text-stone-200 dark:hover:bg-stone-800"
              >
                {user.displayName}
              </Link>
              <Button
                variant="ghost"
                onClick={async () => {
                  await logout();
                  router.push("/");
                }}
              >
                登出
              </Button>
            </>
          ) : (
            <>
              <NavLink href="/login">登入</NavLink>
              <Link href="/register">
                <Button>註冊</Button>
              </Link>
            </>
          )}
        </div>
      </nav>
    </header>
  );
}
