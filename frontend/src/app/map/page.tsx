"use client";

import dynamic from "next/dynamic";
import Link from "next/link";
import { useCallback, useEffect, useRef, useState } from "react";
import { api, query } from "@/lib/api";
import { Button, Card, Empty, ErrorNote, Input, Stars, TagPill } from "@/components/ui";
import { IconCrosshair, IconMapPin, IconSearch } from "@/components/icons";
import type { Bounds } from "@/components/RestaurantMap";
import type { RestaurantSummary } from "@/lib/types";
import "leaflet/dist/leaflet.css";

// Leaflet touches `window` at import time, so it cannot be server-rendered.
const RestaurantMap = dynamic(() => import("@/components/RestaurantMap").then((m) => m.RestaurantMap), {
  ssr: false,
  loading: () => <div className="skeleton h-full w-full" />,
});

/**
 * 美食地圖.
 *
 * <p>Laid out as a two-pane explorer rather than a map with a grid underneath:
 * the map holds the full height of the viewport and the results scroll beside
 * it, so panning and reading the list are the same gesture. Below `lg` the two
 * stack, map first.
 */
export default function MapPage() {
  const [restaurants, setRestaurants] = useState<RestaurantSummary[]>([]);
  const [centre, setCentre] = useState<[number, number] | null>(null);
  const [error, setError] = useState<unknown>(null);
  const [radius, setRadius] = useState(1000);
  const [locating, setLocating] = useState(false);
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
    setLocating(true);
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
          .catch(setError)
          .finally(() => setLocating(false));
      },
      () => {
        setError(new Error("無法取得您的位置，請確認已允許定位權限。"));
        setLocating(false);
      },
    );
  }

  useEffect(() => () => { if (debounce.current) clearTimeout(debounce.current); }, []);

  return (
    <div className="mx-auto w-full max-w-[96rem] px-5 py-8 sm:px-6">
      <header className="mb-5 flex flex-wrap items-end justify-between gap-x-6 gap-y-4 border-b border-line pb-5">
        <div>
          <span aria-hidden className="block font-script text-2xl leading-none text-pepper">
            Around you
          </span>
          <h1 className="mt-1 font-display text-3xl font-bold tracking-tight text-ink">美食地圖</h1>
          <p className="mt-1.5 text-sm text-subtle">拖曳地圖搜尋該範圍的餐廳，或用目前位置找附近的店家。</p>
        </div>

        <div className="flex flex-wrap items-end gap-3">
          <div>
            <label htmlFor="radius" className="mb-1.5 block text-[13px] font-medium text-subtle">
              搜尋半徑（公尺）
            </label>
            <Input
              id="radius"
              type="number"
              inputMode="numeric"
              min={100}
              max={50000}
              step={100}
              value={radius}
              onChange={(e) => setRadius(Number(e.target.value))}
              className="w-32 tabular"
            />
          </div>
          <Button onClick={locateMe} loading={locating} icon={<IconCrosshair />}>
            搜尋我附近
          </Button>
        </div>
      </header>

      <ErrorNote error={error} />

      <div className="mt-4 grid gap-5 lg:grid-cols-[minmax(0,1fr)_24rem]">
        {/* Map — a fixed aspect on mobile, viewport-height beside the list. */}
        <div className="overflow-hidden rounded-2xl border border-line shadow-card">
          <RestaurantMap
            restaurants={restaurants}
            centre={centre}
            onBoundsChange={onBoundsChange}
            className="h-[55vh] w-full lg:h-[calc(100vh-15rem)] lg:min-h-[32rem]"
          />
        </div>

        {/* Results — scrolls independently so the map never leaves the screen. */}
        <aside className="flex flex-col lg:h-[calc(100vh-15rem)] lg:min-h-[32rem]">
          <p className="mb-3 text-sm font-medium text-subtle" aria-live="polite">
            畫面內共 {restaurants.length} 間餐廳
          </p>

          <div className="flex-1 space-y-3 overflow-y-auto pr-1">
            {restaurants.length === 0 ? (
              <Empty icon={<IconSearch />}>這個範圍內還沒有收錄餐廳，試著縮小或移動地圖。</Empty>
            ) : (
              restaurants.slice(0, 40).map((restaurant) => (
                <Link key={restaurant.id} href={`/restaurants/${restaurant.id}`} className="group block">
                  <Card interactive className="p-4">
                    <div className="flex items-baseline justify-between gap-3">
                      <h2 className="font-display text-[15px] font-bold text-ink transition group-hover:text-pepper-ink">
                        {restaurant.name}
                      </h2>
                      {restaurant.distanceMetres != null && restaurant.distanceMetres > 0 && (
                        <span className="shrink-0 text-xs tabular text-subtle">
                          {Math.round(restaurant.distanceMetres)} m
                        </span>
                      )}
                    </div>
                    <p className="mt-1.5 flex items-start gap-1.5 text-[13px] leading-relaxed text-subtle">
                      <IconMapPin aria-hidden className="mt-0.5 shrink-0 text-sm" />
                      {restaurant.address}
                    </p>
                    <div className="mt-2.5">
                      <Stars average={restaurant.rating.average} count={restaurant.rating.reviewCount} />
                    </div>
                    {restaurant.tags.length > 0 && (
                      <div className="mt-2.5 flex flex-wrap gap-1.5">
                        {restaurant.tags.slice(0, 3).map((tag) => (
                          <TagPill key={tag.id}>{tag.name}</TagPill>
                        ))}
                      </div>
                    )}
                  </Card>
                </Link>
              ))
            )}
          </div>
        </aside>
      </div>
    </div>
  );
}
