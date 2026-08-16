"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api, query } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, Empty, ErrorNote, Input, Spinner, TagPill, money } from "@/components/ui";
import type { Category, Page, Product } from "@/lib/types";

export default function ShopPage() {
  const { user } = useAuth();
  const [page, setPage] = useState<Page<Product> | null>(null);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);
  const [filters, setFilters] = useState({ q: "", minPrice: "", maxPrice: "", categoryId: "" });
  const [applied, setApplied] = useState(filters);
  const [pageNumber, setPageNumber] = useState(0);

  useEffect(() => {
    api.get<Category[]>("/shop/categories", { anonymous: true }).then(setCategories).catch(() => setCategories([]));
  }, []);

  useEffect(() => {
    setLoading(true);
    api
      .get<Page<Product>>(`/shop/products${query({ ...applied, page: pageNumber, size: 12 })}`, { anonymous: true })
      .then(setPage)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [applied, pageNumber]);

  async function addToCart(productId: number) {
    setError(null);
    try {
      await api.put("/cart/items", { productId, quantity: 1 });
      // Nudge the header badge without a full reload.
      window.dispatchEvent(new Event("focus"));
    } catch (e) {
      setError(e);
    }
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">商城</h1>

      <form
        onSubmit={(e) => {
          e.preventDefault();
          setPageNumber(0);
          setApplied(filters);
        }}
        className="grid gap-3 sm:grid-cols-5"
      >
        <Input
          className="sm:col-span-2"
          placeholder="搜尋商品…"
          value={filters.q}
          onChange={(e) => setFilters({ ...filters, q: e.target.value })}
          aria-label="搜尋商品"
        />
        <Input
          type="number"
          placeholder="最低價"
          value={filters.minPrice}
          onChange={(e) => setFilters({ ...filters, minPrice: e.target.value })}
          aria-label="最低價"
        />
        <Input
          type="number"
          placeholder="最高價"
          value={filters.maxPrice}
          onChange={(e) => setFilters({ ...filters, maxPrice: e.target.value })}
          aria-label="最高價"
        />
        <div className="flex gap-2">
          <select
            value={filters.categoryId}
            onChange={(e) => setFilters({ ...filters, categoryId: e.target.value })}
            aria-label="分類"
            className="w-full rounded-lg border border-stone-300 bg-white px-2 text-sm dark:border-stone-700 dark:bg-stone-900"
          >
            <option value="">全部分類</option>
            {categories.map((c) => (
              <option key={c.id} value={c.id}>
                {c.name}
              </option>
            ))}
          </select>
          <Button type="submit">篩選</Button>
        </div>
      </form>

      <ErrorNote error={error} />

      {loading ? (
        <Spinner />
      ) : !page || page.content.length === 0 ? (
        <Empty>找不到符合條件的商品。</Empty>
      ) : (
        <>
          <p className="text-sm text-stone-500">共 {page.totalElements} 件商品</p>
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
            {page.content.map((product) => (
              <Card key={product.id} className="flex h-full flex-col overflow-hidden">
                <Link href={`/shop/${product.id}`} className="block flex-1 p-4">
                  <h2 className="font-semibold">{product.name}</h2>
                  <p className="mt-1 text-xs text-stone-500">{product.restaurantName}</p>
                  <p className="mt-2 text-lg font-bold text-red-600">{money(product.price)}</p>
                  <p className="mt-1 text-xs text-stone-400">
                    {product.quantity > 0 ? `庫存 ${product.quantity}` : "已售完"}
                  </p>
                  <div className="mt-2 flex flex-wrap gap-1.5">
                    {product.tags.slice(0, 2).map((tag) => (
                      <TagPill key={tag.id}>{tag.name}</TagPill>
                    ))}
                  </div>
                </Link>
                {user && (
                  <div className="border-t border-stone-100 p-3 dark:border-stone-800">
                    <Button
                      className="w-full"
                      disabled={product.quantity === 0}
                      onClick={() => addToCart(product.id)}
                    >
                      加入購物車
                    </Button>
                  </div>
                )}
              </Card>
            ))}
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
