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
    <>
      {/* Banner: the 2021 商城 header — food photography, script welcome, title. */}
      <section
        className="hero-overlay relative flex min-h-[340px] items-center justify-center bg-cover bg-center"
        style={{ backgroundImage: "url(/brand/hero-shop.jpg)" }}
      >
        <div className="relative z-10 px-6 text-center">
          <span className="block font-script text-4xl text-mint sm:text-5xl">Welcome to PepperNoodle</span>
          <h1 className="mt-1 text-4xl font-bold tracking-wide text-white sm:text-5xl">New Upcoming Products</h1>
        </div>
      </section>

      <div className="mx-auto max-w-7xl gap-10 px-6 py-12 lg:flex">
        <aside className="mb-8 w-full shrink-0 lg:mb-0 lg:w-56">
          <form
            onSubmit={(e) => {
              e.preventDefault();
              setPageNumber(0);
              setApplied(filters);
            }}
            className="space-y-6"
          >
            <Input
              placeholder="搜尋商品…"
              value={filters.q}
              onChange={(e) => setFilters({ ...filters, q: e.target.value })}
              aria-label="搜尋商品"
            />

            <div>
              <h2 className="mb-2 font-display text-lg font-bold">商品類別</h2>
              <ul className="space-y-1 text-sm">
                <li>
                  <button
                    type="button"
                    onClick={() => {
                      const next = { ...filters, categoryId: "" };
                      setPageNumber(0);
                      setFilters(next);
                      setApplied(next);
                    }}
                    className={`transition hover:text-pepper ${!filters.categoryId ? "font-semibold text-pepper" : "text-stone-600 dark:text-stone-400"}`}
                  >
                    全部
                  </button>
                </li>
                {categories.map((category, index) => (
                  <li key={category.id}>
                    <button
                      type="button"
                      onClick={() => {
                        const next = { ...filters, categoryId: String(category.id) };
                        setPageNumber(0);
                        setFilters(next);
                        setApplied(next);
                      }}
                      className={`transition hover:text-pepper ${
                        filters.categoryId === String(category.id)
                          ? "font-semibold text-pepper"
                          : "text-stone-600 dark:text-stone-400"
                      }`}
                    >
                      <span className="mr-2 text-stone-400">{String(index + 1).padStart(2, "0")}</span>
                      {category.name}
                    </button>
                  </li>
                ))}
              </ul>
            </div>

            <div>
              <h2 className="mb-2 font-display text-lg font-bold">價格</h2>
              <div className="flex gap-2">
                <Input
                  type="number"
                  placeholder="最低"
                  value={filters.minPrice}
                  onChange={(e) => setFilters({ ...filters, minPrice: e.target.value })}
                  aria-label="最低價"
                />
                <Input
                  type="number"
                  placeholder="最高"
                  value={filters.maxPrice}
                  onChange={(e) => setFilters({ ...filters, maxPrice: e.target.value })}
                  aria-label="最高價"
                />
              </div>
            </div>

            <Button type="submit" className="w-full">篩選</Button>
          </form>
        </aside>

        <div className="min-w-0 flex-1">
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
              <Card key={product.id} className="group flex h-full flex-col overflow-hidden">
                <Link href={`/shop/${product.id}`} className="relative block">
                  <div className="relative flex h-44 items-center justify-center overflow-hidden bg-stone-100 dark:bg-stone-800">
                    {product.imageUrl ? (
                      // eslint-disable-next-line @next/next/no-img-element
                      <img src={product.imageUrl} alt={product.name} className="h-full w-full object-cover" />
                    ) : (
                      <span className="text-5xl" aria-hidden>🍜</span>
                    )}
                    <span className="absolute bottom-2 right-2 rounded-full bg-pepper px-4 py-1 font-display text-xs font-bold uppercase text-white opacity-0 transition group-hover:opacity-100">
                      Open
                    </span>
                  </div>
                </Link>
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
      </div>
    </>
  );
}
