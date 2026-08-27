"use client";

import Link from "next/link";
import { Suspense, useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { api, query } from "@/lib/api";
import {
  Button,
  Card,
  CardGridSkeleton,
  Empty,
  FilterChip,
  PageHeader,
  PageShell,
  Pagination,
  Stars,
  TagPill,
} from "@/components/ui";
import { IconMapPin, IconPhone, IconSearch } from "@/components/icons";
import type { Page, RestaurantSummary, Tag } from "@/lib/types";

function RestaurantsBrowser() {
  // The home hero and the district tiles both link here with ?q=, so the
  // initial term has to come from the URL or those links land on a blank list.
  const initialQuery = useSearchParams().get("q") ?? "";

  const [page, setPage] = useState<Page<RestaurantSummary> | null>(null);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState(initialQuery);
  const [submitted, setSubmitted] = useState(initialQuery);
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

  const filtered = submitted !== "" || activeTags.length > 0;

  function clearFilters() {
    setSearch("");
    setSubmitted("");
    setActiveTags([]);
    setPageNumber(0);
  }

  return (
    <PageShell>
      <PageHeader
        kicker="Find your table"
        title="餐廳"
        description="以店名、地址或料理類型尋找台北的餐廳。"
      />

      <div className="space-y-5">
        <form
          onSubmit={(e) => {
            e.preventDefault();
            setPageNumber(0);
            setSubmitted(search);
          }}
          className="flex flex-col gap-2.5 sm:flex-row"
          role="search"
        >
          <div className="relative flex-1">
            <IconSearch
              aria-hidden
              className="pointer-events-none absolute left-4 top-1/2 -translate-y-1/2 text-lg text-subtle"
            />
            <input
              type="search"
              placeholder="搜尋店名或地址…"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              aria-label="搜尋餐廳"
              className="min-h-12 w-full rounded-xl border border-line-strong bg-white pl-11 pr-4 text-[15px] text-ink transition placeholder:text-subtle hover:border-ink/30 focus:border-pepper focus:outline-none focus:ring-4 focus:ring-pepper/15"
            />
          </div>
          <Button type="submit" size="lg">
            搜尋
          </Button>
        </form>

        {tags.length > 0 && (
          <fieldset>
            <legend className="mb-2.5 text-[13px] font-semibold uppercase tracking-wider text-subtle">
              料理類型
            </legend>
            <div className="flex flex-wrap gap-2">
              {tags.map((tag) => {
                const active = activeTags.includes(tag.id);
                return (
                  <FilterChip
                    key={tag.id}
                    active={active}
                    onClick={() => {
                      setPageNumber(0);
                      setActiveTags(
                        active ? activeTags.filter((id) => id !== tag.id) : [...activeTags, tag.id],
                      );
                    }}
                  >
                    {tag.name}
                  </FilterChip>
                );
              })}
            </div>
          </fieldset>
        )}

        {/* Result count is a live region: filtering swaps the list without
            moving focus, so otherwise nothing announces what changed. */}
        <div className="flex flex-wrap items-center gap-3 border-b border-line pb-4">
          <p className="text-sm text-subtle" aria-live="polite">
            {loading ? "搜尋中…" : `共 ${page?.totalElements ?? 0} 間餐廳`}
          </p>
          {filtered && (
            <Button variant="quiet" size="sm" onClick={clearFilters}>
              清除篩選
            </Button>
          )}
        </div>

        {loading ? (
          <CardGridSkeleton count={6} />
        ) : !page || page.content.length === 0 ? (
          <Empty
            icon={<IconSearch />}
            action={
              filtered ? (
                <Button variant="ghost" onClick={clearFilters}>
                  清除篩選
                </Button>
              ) : undefined
            }
          >
            找不到符合條件的餐廳。試試其他關鍵字，或放寬料理類型。
          </Empty>
        ) : (
          <>
            <ul className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
              {page.content.map((restaurant) => (
                <li key={restaurant.id} className="flex">
                  <Link href={`/restaurants/${restaurant.id}`} className="group w-full">
                    <Card interactive className="flex h-full flex-col p-6">
                      <h2 className="font-display text-lg font-bold leading-snug text-ink transition group-hover:text-pepper-ink">
                        {restaurant.name}
                      </h2>
                      <p className="mt-2 flex items-start gap-1.5 text-sm leading-relaxed text-subtle">
                        <IconMapPin aria-hidden className="mt-0.5 shrink-0 text-base" />
                        {restaurant.address}
                      </p>
                      {restaurant.contact && (
                        <p className="mt-1 flex items-center gap-1.5 text-[13px] tabular text-subtle">
                          <IconPhone aria-hidden className="shrink-0 text-sm" />
                          {restaurant.contact}
                        </p>
                      )}
                      <div className="mt-4">
                        <Stars average={restaurant.rating.average} count={restaurant.rating.reviewCount} />
                      </div>
                      {restaurant.tags.length > 0 && (
                        <div className="mt-auto flex flex-wrap gap-1.5 pt-4">
                          {restaurant.tags.map((tag) => (
                            <TagPill key={tag.id}>{tag.name}</TagPill>
                          ))}
                        </div>
                      )}
                    </Card>
                  </Link>
                </li>
              ))}
            </ul>

            <Pagination
              page={page.page}
              totalPages={page.totalPages}
              first={page.first}
              last={page.last}
              onChange={setPageNumber}
            />
          </>
        )}
      </div>
    </PageShell>
  );
}

export default function RestaurantsPage() {
  return (
    <Suspense fallback={<PageShell><CardGridSkeleton count={6} /></PageShell>}>
      <RestaurantsBrowser />
    </Suspense>
  );
}
