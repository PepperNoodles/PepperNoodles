"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api, query } from "@/lib/api";
import { Button, Card, Empty, Input, Spinner, Stars, TagPill } from "@/components/ui";
import type { Page, RestaurantSummary, Tag } from "@/lib/types";

export default function RestaurantsPage() {
  const [page, setPage] = useState<Page<RestaurantSummary> | null>(null);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [submitted, setSubmitted] = useState("");
  const [tags, setTags] = useState<Tag[]>([]);
  const [activeTags, setActiveTags] = useState<number[]>([]);
  const [pageNumber, setPageNumber] = useState(0);

  useEffect(() => {
    api.get<Tag[]>("/food-tags", { anonymous: true }).then(setTags).catch(() => setTags([]));
  }, []);

  useEffect(() => {
    setLoading(true);
    api
      .get<Page<RestaurantSummary>>(
        `/restaurants${query({ q: submitted, tagIds: activeTags, page: pageNumber, size: 12 })}`,
        { anonymous: true },
      )
      .then(setPage)
      .catch(() => setPage(null))
      .finally(() => setLoading(false));
  }, [submitted, activeTags, pageNumber]);

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">餐廳</h1>

      <form
        onSubmit={(e) => {
          e.preventDefault();
          setPageNumber(0);
          setSubmitted(search);
        }}
        className="flex gap-2"
      >
        <Input
          placeholder="搜尋店名或地址…"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          aria-label="搜尋餐廳"
        />
        <Button type="submit">搜尋</Button>
      </form>

      <div className="flex flex-wrap gap-2">
        {tags.map((tag) => {
          const active = activeTags.includes(tag.id);
          return (
            <button
              key={tag.id}
              aria-pressed={active}
              onClick={() => {
                setPageNumber(0);
                setActiveTags(active ? activeTags.filter((id) => id !== tag.id) : [...activeTags, tag.id]);
              }}
              className={`rounded-full px-3 py-1 text-xs font-medium transition ${
                active
                  ? "bg-red-600 text-white"
                  : "bg-stone-100 text-stone-700 hover:bg-stone-200 dark:bg-stone-800 dark:text-stone-300"
              }`}
            >
              {tag.name}
            </button>
          );
        })}
      </div>

      {loading ? (
        <Spinner />
      ) : !page || page.content.length === 0 ? (
        <Empty>找不到符合條件的餐廳。</Empty>
      ) : (
        <>
          <p className="text-sm text-stone-500">共 {page.totalElements} 間</p>
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {page.content.map((restaurant) => (
              <Link key={restaurant.id} href={`/restaurants/${restaurant.id}`}>
                <Card className="h-full p-4 transition hover:shadow-md">
                  <h2 className="font-semibold">{restaurant.name}</h2>
                  <p className="mt-1 text-sm text-stone-500">{restaurant.address}</p>
                  {restaurant.contact && <p className="mt-1 text-xs text-stone-400">{restaurant.contact}</p>}
                  <div className="mt-3">
                    <Stars average={restaurant.rating.average} count={restaurant.rating.reviewCount} />
                  </div>
                  <div className="mt-3 flex flex-wrap gap-1.5">
                    {restaurant.tags.map((tag) => (
                      <TagPill key={tag.id}>{tag.name}</TagPill>
                    ))}
                  </div>
                </Card>
              </Link>
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
