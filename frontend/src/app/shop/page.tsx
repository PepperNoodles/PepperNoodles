"use client";

import Link from "next/link";
import { Suspense, useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { api, query } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import {
  Button,
  Card,
  CardGridSkeleton,
  Empty,
  ErrorNote,
  Input,
  SuccessNote,
  TagPill,
  money,
} from "@/components/ui";
import { IconBowl, IconCart, IconChevronDown, IconSearch } from "@/components/icons";
import type { Category, Page, Product } from "@/lib/types";

function ShopBrowser() {
  const { user } = useAuth();
  // The home hero can search the shop, so ?q= has to seed the filter.
  const initialQuery = useSearchParams().get("q") ?? "";

  const [page, setPage] = useState<Page<Product> | null>(null);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);
  const [added, setAdded] = useState<string | null>(null);
  const [filters, setFilters] = useState({ q: initialQuery, minPrice: "", maxPrice: "", categoryId: "" });
  const [applied, setApplied] = useState(filters);
  const [filtersOpen, setFiltersOpen] = useState(false);
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

  async function addToCart(product: Product) {
    setError(null);
    setAdded(null);
    try {
      await api.put("/cart/items", { productId: product.id, quantity: 1 });
      // Nudge the header badge without a full reload.
      window.dispatchEvent(new Event("focus"));
      setAdded(product.name);
    } catch (e) {
      setError(e);
    }
  }

  /** Category picks apply immediately — an extra "filter" click would be busywork. */
  function pickCategory(categoryId: string) {
    const next = { ...filters, categoryId };
    setPageNumber(0);
    setFilters(next);
    setApplied(next);
    // Collapse the mobile rail — the results it just changed are below it.
    setFiltersOpen(false);
  }

  return (
    <>
      {/* Banner: the 2021 商城 header — food photography, script welcome, title. */}
      <section
        className="hero-overlay relative flex min-h-[320px] items-center justify-center bg-cover bg-center"
        style={{ backgroundImage: "url(/brand/hero-shop.jpg)" }}
      >
        <div className="relative z-10 px-6 pt-16 text-center">
          <span aria-hidden className="on-photo block font-script text-4xl leading-tight text-mint sm:text-5xl">
            Welcome to PepperNoodle
          </span>
          <h1 className="on-photo mt-1 font-display text-4xl font-bold tracking-tight text-white sm:text-5xl">
            New Upcoming Products
          </h1>
        </div>
      </section>

      <div className="mx-auto w-full max-w-7xl gap-12 px-5 py-12 sm:px-6 lg:flex lg:py-16">
        {/* ---------- Filters ---------- */}
        {/*
          Below `lg` the filter rail is collapsed behind a toggle. Expanded, it
          filled the whole first screen on a phone — a shopper arriving at the
          shop saw only filters and had to scroll past every one of them to
          reach a single product. Above `lg` the rail is always shown, so the
          toggle is hidden there and cannot strand the panel closed.
        */}
        <aside className="mb-10 w-full shrink-0 lg:mb-0 lg:w-60">
          <button
            type="button"
            onClick={() => setFiltersOpen(!filtersOpen)}
            aria-expanded={filtersOpen}
            aria-controls="shop-filters"
            className="flex min-h-12 w-full cursor-pointer items-center justify-between rounded-2xl border border-line bg-white px-5 font-display text-sm font-bold uppercase tracking-wider text-ink shadow-card transition hover:border-line-strong lg:hidden"
          >
            篩選商品
            <IconChevronDown
              aria-hidden
              className={`text-xl text-subtle transition ${filtersOpen ? "rotate-180" : ""}`}
            />
          </button>

          <form
            id="shop-filters"
            onSubmit={(e) => {
              e.preventDefault();
              setPageNumber(0);
              setApplied(filters);
              setFiltersOpen(false);
            }}
            className={`mt-5 space-y-8 lg:mt-0 lg:block lg:sticky lg:top-24 ${filtersOpen ? "block" : "hidden"}`}
            role="search"
          >
            <div>
              <label htmlFor="shop-q" className="mb-2 block font-display text-sm font-bold uppercase tracking-wider text-ink">
                搜尋
              </label>
              <div className="relative">
                <IconSearch
                  aria-hidden
                  className="pointer-events-none absolute left-3.5 top-1/2 -translate-y-1/2 text-lg text-subtle"
                />
                <Input
                  id="shop-q"
                  type="search"
                  placeholder="搜尋商品…"
                  value={filters.q}
                  onChange={(e) => setFilters({ ...filters, q: e.target.value })}
                  className="pl-10"
                />
              </div>
            </div>

            <div>
              <h2 className="mb-3 font-display text-sm font-bold uppercase tracking-wider text-ink">商品類別</h2>
              <ul className="space-y-0.5">
                <li>
                  <CategoryButton
                    active={!filters.categoryId}
                    onClick={() => pickCategory("")}
                    label="全部"
                  />
                </li>
                {categories.map((category, index) => (
                  <li key={category.id}>
                    <CategoryButton
                      active={filters.categoryId === String(category.id)}
                      onClick={() => pickCategory(String(category.id))}
                      label={category.name}
                      index={index + 1}
                    />
                  </li>
                ))}
              </ul>
            </div>

            <fieldset>
              <legend className="mb-3 font-display text-sm font-bold uppercase tracking-wider text-ink">價格</legend>
              <div className="flex items-center gap-2">
                <Input
                  type="number"
                  inputMode="numeric"
                  min={0}
                  placeholder="最低"
                  value={filters.minPrice}
                  onChange={(e) => setFilters({ ...filters, minPrice: e.target.value })}
                  aria-label="最低價"
                  className="tabular"
                />
                <span aria-hidden className="text-subtle">–</span>
                <Input
                  type="number"
                  inputMode="numeric"
                  min={0}
                  placeholder="最高"
                  value={filters.maxPrice}
                  onChange={(e) => setFilters({ ...filters, maxPrice: e.target.value })}
                  aria-label="最高價"
                  className="tabular"
                />
              </div>
            </fieldset>

            <Button type="submit" className="w-full">
              篩選
            </Button>
          </form>
        </aside>

        {/* ---------- Results ---------- */}
        <div className="min-w-0 flex-1 space-y-5">
          <ErrorNote error={error} />
          {added && <SuccessNote>已將「{added}」加入購物車。</SuccessNote>}

          <p className="border-b border-line pb-4 text-sm text-subtle" aria-live="polite">
            {loading ? "載入中…" : `共 ${page?.totalElements ?? 0} 件商品`}
          </p>

          {loading ? (
            <CardGridSkeleton count={8} columns="sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4" />
          ) : !page || page.content.length === 0 ? (
            <Empty icon={<IconSearch />}>找不到符合條件的商品。試著放寬價格範圍或換個類別。</Empty>
          ) : (
            <>
              <ul className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
                {page.content.map((product) => {
                  const soldOut = product.quantity === 0;
                  return (
                    <Card key={product.id} as="li" className="group flex h-full flex-col overflow-hidden">
                      {/*
                        Named explicitly: most products have no photo, and the
                        fallback mark is decorative — without this the image
                        link is an empty anchor with no accessible name at all.
                        `aria-label` rather than visually-hidden text so the
                        name is not duplicated in the card's text content.
                      */}
                      <Link
                        href={`/shop/${product.id}`}
                        aria-label={product.name}
                        className="relative block overflow-hidden"
                      >
                        <div className="relative flex aspect-square items-center justify-center bg-mist">
                          {product.imageUrl ? (
                            // eslint-disable-next-line @next/next/no-img-element
                            <img
                              src={product.imageUrl}
                              alt={product.name}
                              loading="lazy"
                              className="h-full w-full object-cover transition duration-300 group-hover:scale-105 motion-reduce:group-hover:scale-100"
                            />
                          ) : (
                            <IconBowl aria-hidden className="text-5xl text-line-strong" />
                          )}
                          {soldOut && (
                            <span className="absolute inset-x-0 bottom-0 bg-ink/80 py-1.5 text-center font-display text-xs font-bold uppercase tracking-wider text-white">
                              已售完
                            </span>
                          )}
                        </div>
                      </Link>

                      <div className="flex flex-1 flex-col p-5">
                        <Link href={`/shop/${product.id}`} className="flex flex-1 flex-col">
                          <h2 className="font-display text-[15px] font-bold leading-snug text-ink transition group-hover:text-pepper-ink">
                            {product.name}
                          </h2>
                          <p className="mt-1 text-xs text-subtle">{product.restaurantName}</p>
                          <p className="mt-3 font-display text-xl font-bold tabular text-pepper">
                            {money(product.price)}
                          </p>
                          <p className="mt-1 text-xs tabular text-subtle">
                            {soldOut ? "目前無庫存" : `庫存 ${product.quantity}`}
                          </p>
                          {product.tags.length > 0 && (
                            <div className="mt-3 flex flex-wrap gap-1.5">
                              {product.tags.slice(0, 2).map((tag) => (
                                <TagPill key={tag.id}>{tag.name}</TagPill>
                              ))}
                            </div>
                          )}
                        </Link>

                        {user && (
                          <div className="mt-5 pt-1">
                            <Button
                              className="w-full"
                              size="sm"
                              disabled={soldOut}
                              onClick={() => addToCart(product)}
                              icon={<IconCart />}
                            >
                              加入購物車
                            </Button>
                          </div>
                        )}
                      </div>
                    </Card>
                  );
                })}
              </ul>

              {page.totalPages > 1 && (
                <nav className="flex items-center justify-center gap-4 pt-4" aria-label="分頁">
                  <Button
                    variant="ghost"
                    size="sm"
                    disabled={page.first}
                    onClick={() => setPageNumber((n) => n - 1)}
                  >
                    上一頁
                  </Button>
                  <span className="text-sm tabular text-subtle" aria-live="polite">
                    第 {page.page + 1} / {page.totalPages} 頁
                  </span>
                  <Button
                    variant="ghost"
                    size="sm"
                    disabled={page.last}
                    onClick={() => setPageNumber((n) => n + 1)}
                  >
                    下一頁
                  </Button>
                </nav>
              )}
            </>
          )}
        </div>
      </div>
    </>
  );
}

/** A category row in the shop rail — numbered like the 2021 sidebar. */
function CategoryButton({
  active,
  label,
  index,
  onClick,
}: {
  active: boolean;
  label: string;
  index?: number;
  onClick: () => void;
}) {
  return (
    <button
      type="button"
      onClick={onClick}
      aria-pressed={active}
      className={`flex min-h-10 w-full cursor-pointer items-center gap-2.5 rounded-lg px-2.5 text-left text-sm transition ${
        active ? "bg-pepper-tint font-semibold text-pepper-ink" : "text-body hover:bg-mist hover:text-ink"
      }`}
    >
      {index !== undefined && (
        <span aria-hidden className={`text-xs tabular ${active ? "text-pepper" : "text-subtle"}`}>
          {String(index).padStart(2, "0")}
        </span>
      )}
      {label}
    </button>
  );
}

export default function ShopPage() {
  return (
    <Suspense fallback={null}>
      <ShopBrowser />
    </Suspense>
  );
}
