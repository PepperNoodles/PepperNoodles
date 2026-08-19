"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api, query } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, Empty, ErrorNote, Input, Spinner } from "@/components/ui";
import type { Page } from "@/lib/types";

interface ManagedRestaurant {
  id: number;
  name: string;
  address: string;
  contact?: string | null;
  ownerUserId: number;
  ownerEmail: string;
  ownerName: string;
  reviewCount: number;
  ratingAverage?: string | null;
  createdAt: string;
}

/** 後台餐廳查詢 — every listing with the account behind it. */
export default function AdminRestaurantsPage() {
  const { hasRole, loading: authLoading } = useAuth();
  const [page, setPage] = useState<Page<ManagedRestaurant> | null>(null);
  const [search, setSearch] = useState("");
  const [applied, setApplied] = useState("");
  const [pageNumber, setPageNumber] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);

  useEffect(() => {
    if (authLoading) return;
    if (!hasRole("ROLE_ADMIN")) {
      setLoading(false);
      return;
    }
    setLoading(true);
    api
      .get<Page<ManagedRestaurant>>(`/admin/restaurants${query({ q: applied, page: pageNumber, size: 20 })}`)
      .then(setPage)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [authLoading, hasRole, applied, pageNumber]);

  if (authLoading || loading) return <Spinner />;
  if (!hasRole("ROLE_ADMIN")) {
    return (
      <p className="py-16 text-center text-sm text-stone-500">
        這個頁面只有管理員能存取。
        <Link href="/" className="ml-1 text-pepper hover:underline">
          回首頁
        </Link>
      </p>
    );
  }

  return (
    <div className="mx-auto max-w-6xl space-y-6 px-6 py-10">
      <div>
        <Link href="/admin" className="text-sm text-stone-500 hover:text-pepper">
          ← 回到後台
        </Link>
        <h1 className="mt-2 text-2xl font-bold">餐廳管理</h1>
        <p className="mt-1 text-sm text-stone-500">
          可依店名、地址或店主信箱搜尋。編輯與刪除沿用一般的餐廳管理畫面，管理員本來就有權限。
        </p>
      </div>

      <form
        onSubmit={(e) => {
          e.preventDefault();
          setPageNumber(0);
          setApplied(search);
        }}
        className="flex gap-2"
      >
        <Input
          placeholder="搜尋店名、地址或店主信箱…"
          aria-label="搜尋餐廳"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <Button type="submit">搜尋</Button>
      </form>

      <ErrorNote error={error} />

      {!page || page.content.length === 0 ? (
        <Empty>找不到符合條件的餐廳。</Empty>
      ) : (
        <>
          <p className="text-sm text-stone-500">共 {page.totalElements} 間</p>
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm">
              <thead className="border-b border-stone-200 text-xs uppercase text-stone-500">
                <tr>
                  <th className="py-2">餐廳</th>
                  <th className="py-2">店主</th>
                  <th className="py-2 text-right">評論</th>
                  <th className="py-2 text-right">評分</th>
                  <th className="py-2 text-right">操作</th>
                </tr>
              </thead>
              <tbody>
                {page.content.map((restaurant) => (
                  <tr key={restaurant.id} className="border-b border-stone-100 last:border-0">
                    <td className="py-2">
                      <Link
                        href={`/restaurants/${restaurant.id}`}
                        className="font-medium hover:text-pepper hover:underline"
                      >
                        {restaurant.name}
                      </Link>
                      <p className="text-xs text-stone-500">{restaurant.address}</p>
                    </td>
                    <td className="py-2">
                      <Link
                        href={`/members/${restaurant.ownerUserId}`}
                        className="hover:text-pepper hover:underline"
                      >
                        {restaurant.ownerName}
                      </Link>
                      <p className="text-xs text-stone-400">{restaurant.ownerEmail}</p>
                    </td>
                    <td className="py-2 text-right">{restaurant.reviewCount}</td>
                    <td className="py-2 text-right">
                      {restaurant.ratingAverage ? Number(restaurant.ratingAverage).toFixed(1) : "—"}
                    </td>
                    <td className="py-2 text-right">
                      <Link
                        href={`/company/restaurants/${restaurant.id}`}
                        className="text-xs text-pepper hover:underline"
                      >
                        管理
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {page.totalPages > 1 && (
            <div className="flex items-center justify-center gap-3">
              <Button variant="ghost" disabled={page.first} onClick={() => setPageNumber((n) => n - 1)}>
                上一頁
              </Button>
              <span className="text-sm text-stone-500">
                {page.page + 1} / {page.totalPages}
              </span>
              <Button variant="ghost" disabled={page.last} onClick={() => setPageNumber((n) => n + 1)}>
                下一頁
              </Button>
            </div>
          )}
        </>
      )}
    </div>
  );
}
