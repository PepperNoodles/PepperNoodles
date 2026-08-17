"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { Card, Spinner, Stars, TagPill } from "@/components/ui";
import type { Page, RestaurantSummary } from "@/lib/types";

export default function HomePage() {
  const router = useRouter();
  const [restaurants, setRestaurants] = useState<RestaurantSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [term, setTerm] = useState("");
  const [scope, setScope] = useState("restaurants");

  useEffect(() => {
    api
      .get<Page<RestaurantSummary>>("/restaurants?size=6", { anonymous: true })
      .then((page) => setRestaurants(page.content))
      .catch(() => setRestaurants([]))
      .finally(() => setLoading(false));
  }, []);

  function search(event: React.FormEvent) {
    event.preventDefault();
    const path = scope === "shop" ? "/shop" : "/restaurants";
    router.push(term.trim() ? `${path}?q=${encodeURIComponent(term.trim())}` : path);
  }

  return (
    <>
      {/* Hero: the 2021 slider — full-bleed food photography behind a dark scrim,
          script tagline, oversized headline, and the pill search control. */}
      <section
        className="hero-overlay relative flex min-h-[680px] items-center justify-center bg-cover bg-center lg:min-h-[820px]"
        style={{ backgroundImage: "url(/brand/hero-home.jpg)" }}
      >
        <div className="relative z-10 mx-auto w-full max-w-3xl px-6 text-center">
          <span className="block font-script text-5xl leading-tight text-mint sm:text-6xl">
            Explore the Food
          </span>
          <h1 className="mt-2 text-5xl font-bold leading-tight text-white sm:text-6xl lg:text-7xl">
            走吧!美食之旅!
          </h1>

          <form onSubmit={search} className="search-ring mx-auto mt-10 flex max-w-2xl bg-white">
            <input
              value={term}
              onChange={(e) => setTerm(e.target.value)}
              placeholder="今晚我想來點…"
              aria-label="搜尋"
              className="min-w-0 flex-1 rounded-l-full bg-transparent px-6 py-3 text-stone-800 outline-none placeholder:text-stone-400"
            />
            <select
              value={scope}
              onChange={(e) => setScope(e.target.value)}
              aria-label="搜尋範圍"
              className="border-l border-stone-200 bg-transparent px-4 text-sm text-stone-600 outline-none"
            >
              <option value="restaurants">餐廳</option>
              <option value="shop">商城</option>
            </select>
            <button
              type="submit"
              className="rounded-full bg-pepper px-8 py-3 font-display text-sm font-bold uppercase tracking-wider text-white transition hover:bg-pepper-dark"
            >
              Search
            </button>
          </form>

          <div className="mt-8 flex justify-center gap-4">
            <Link
              href="/map"
              className="rounded-full border border-white/60 px-6 py-2.5 font-display text-sm font-bold uppercase tracking-wide text-white transition hover:bg-white/10"
            >
              打開地圖
            </Link>
          </div>
        </div>
      </section>

      <div className="mx-auto max-w-7xl px-6 py-16">
        <div className="mb-8 text-center">
          <span className="font-script text-4xl text-pepper">Discover</span>
          <h2 className="mt-1 text-3xl font-bold">最新餐廳</h2>
        </div>

        {loading ? (
          <Spinner />
        ) : (
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {restaurants.map((restaurant) => (
              <Link key={restaurant.id} href={`/restaurants/${restaurant.id}`}>
                <Card className="h-full p-5 transition hover:-translate-y-1 hover:shadow-lg">
                  <h3 className="text-lg font-semibold">{restaurant.name}</h3>
                  <p className="mt-1 text-sm text-stone-500">{restaurant.address}</p>
                  <div className="mt-3">
                    <Stars average={restaurant.rating.average} count={restaurant.rating.reviewCount} />
                  </div>
                  <div className="mt-3 flex flex-wrap gap-1.5">
                    {restaurant.tags.slice(0, 3).map((tag) => (
                      <TagPill key={tag.id}>{tag.name}</TagPill>
                    ))}
                  </div>
                </Card>
              </Link>
            ))}
          </div>
        )}

        <div className="mt-10 text-center">
          <Link
            href="/restaurants"
            className="inline-block rounded-full bg-pepper px-8 py-3 font-display text-sm font-bold uppercase tracking-wide text-white transition hover:bg-pepper-dark"
          >
            看全部餐廳
          </Link>
        </div>
      </div>
    </>
  );
}
