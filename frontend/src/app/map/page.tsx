"use client";

import dynamic from "next/dynamic";
import Link from "next/link";
import { useCallback, useEffect, useRef, useState } from "react";
import { api, query } from "@/lib/api";
import { Button, Card, ErrorNote, Input, Stars, TagPill } from "@/components/ui";
import type { Bounds } from "@/components/RestaurantMap";
import type { RestaurantSummary } from "@/lib/types";
import "leaflet/dist/leaflet.css";

// Leaflet touches `window` at import time, so it cannot be server-rendered.
const RestaurantMap = dynamic(() => import("@/components/RestaurantMap").then((m) => m.RestaurantMap), {
  ssr: false,
  loading: () => <div className="h-[70vh] w-full animate-pulse rounded-xl bg-stone-200 dark:bg-stone-800" />,
});

export default function MapPage() {
  const [restaurants, setRestaurants] = useState<RestaurantSummary[]>([]);
  const [centre, setCentre] = useState<[number, number] | null>(null);
  const [error, setError] = useState<unknown>(null);
  const [radius, setRadius] = useState(1000);
  const debounce = useRef<ReturnType<typeof setTimeout> | null>(null);

  /** Viewport search, debounced so panning does not fire a request per frame. */
  const onBoundsChange = useCallback((bounds: Bounds) => {
    if (debounce.current) clearTimeout(debounce.current);
    debounce.current = setTimeout(() => {
      api
        .get<RestaurantSummary[]>(`/map/bounds${query({ ...bounds, limit: 200 })}`, { anonymous: true })
        .then(setRestaurants)
        .catch(setError);
    }, 300);
  }, []);

  function locateMe() {
    if (!navigator.geolocation) {
      setError(new Error("這個瀏覽器不支援定位功能。"));
      return;
    }
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { latitude, longitude } = position.coords;
        setCentre([latitude, longitude]);
        api
          .get<RestaurantSummary[]>(
            `/map/nearby${query({ latitude, longitude, radiusMetres: radius, limit: 100 })}`,
            { anonymous: true },
          )
          .then(setRestaurants)
          .catch(setError);
      },
      () => setError(new Error("無法取得您的位置，請確認已允許定位權限。")),
    );
  }

  useEffect(() => () => { if (debounce.current) clearTimeout(debounce.current); }, []);

  return (
    <div className="space-y-4">
      <div className="flex flex-wrap items-end gap-3">
        <h1 className="mr-auto text-2xl font-bold">美食地圖</h1>
        <div>
          <label htmlFor="radius" className="mb-1 block text-xs text-stone-500">
            搜尋半徑（公尺）
          </label>
          <Input
            id="radius"
            type="number"
            min={100}
            max={50000}
            step={100}
            value={radius}
            onChange={(e) => setRadius(Number(e.target.value))}
            className="w-32"
          />
        </div>
        <Button onClick={locateMe}>搜尋我附近</Button>
      </div>

      <ErrorNote error={error} />

      <RestaurantMap restaurants={restaurants} centre={centre} onBoundsChange={onBoundsChange} />

      <p className="text-sm text-stone-500">畫面內共 {restaurants.length} 間餐廳</p>

      <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
        {restaurants.slice(0, 12).map((restaurant) => (
          <Link key={restaurant.id} href={`/restaurants/${restaurant.id}`}>
            <Card className="h-full p-4 transition hover:shadow-md">
              <div className="flex items-baseline justify-between gap-2">
                <h2 className="font-semibold">{restaurant.name}</h2>
                {restaurant.distanceMetres != null && restaurant.distanceMetres > 0 && (
                  <span className="shrink-0 text-xs text-stone-500">
                    {Math.round(restaurant.distanceMetres)} m
                  </span>
                )}
              </div>
              <p className="mt-1 text-sm text-stone-500">{restaurant.address}</p>
              <div className="mt-2">
                <Stars average={restaurant.rating.average} count={restaurant.rating.reviewCount} />
              </div>
              <div className="mt-2 flex flex-wrap gap-1.5">
                {restaurant.tags.slice(0, 3).map((tag) => (
                  <TagPill key={tag.id}>{tag.name}</TagPill>
                ))}
              </div>
            </Card>
          </Link>
        ))}
      </div>
    </div>
  );
}
