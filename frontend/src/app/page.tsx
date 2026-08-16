"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { Card, Spinner, Stars, TagPill } from "@/components/ui";
import type { Page, RestaurantSummary } from "@/lib/types";

export default function HomePage() {
  const [restaurants, setRestaurants] = useState<RestaurantSummary[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api
      .get<Page<RestaurantSummary>>("/restaurants?size=6", { anonymous: true })
      .then((page) => setRestaurants(page.content))
      .catch(() => setRestaurants([]))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="space-y-12">
      <section className="rounded-2xl bg-gradient-to-br from-red-600 to-amber-500 px-8 py-16 text-center text-white">
        <h1 className="text-4xl font-bold tracking-tight">今天吃什麼？</h1>
        <p className="mx-auto mt-3 max-w-xl text-white/90">
          用地圖找附近的餐廳、看看別人的評論，順便把喜歡的味道帶回家。
        </p>
        <div className="mt-8 flex justify-center gap-3">
          <Link
            href="/map"
            className="rounded-lg bg-white px-5 py-2.5 text-sm font-semibold text-red-700 transition hover:bg-stone-100"
          >
            打開地圖
          </Link>
          <Link
            href="/shop"
            className="rounded-lg border border-white/70 px-5 py-2.5 text-sm font-semibold transition hover:bg-white/10"
          >
            逛商城
          </Link>
        </div>
      </section>

      <section>
        <div className="mb-4 flex items-baseline justify-between">
          <h2 className="text-xl font-semibold">最新餐廳</h2>
          <Link href="/restaurants" className="text-sm text-red-600 hover:underline">
            看全部 →
          </Link>
        </div>

        {loading ? (
          <Spinner />
        ) : (
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {restaurants.map((restaurant) => (
              <Link key={restaurant.id} href={`/restaurants/${restaurant.id}`}>
                <Card className="h-full p-4 transition hover:shadow-md">
                  <h3 className="font-semibold">{restaurant.name}</h3>
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
      </section>
    </div>
  );
}
