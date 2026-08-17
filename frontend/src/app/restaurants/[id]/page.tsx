"use client";

import { use, useCallback, useEffect, useState } from "react";
import Link from "next/link";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, Empty, ErrorNote, Spinner, Stars, TagPill } from "@/components/ui";
import type { Page, RestaurantDetail, Review } from "@/lib/types";

const DAYS = ["週日", "週一", "週二", "週三", "週四", "週五", "週六"];

export default function RestaurantPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const { user } = useAuth();

  const [restaurant, setRestaurant] = useState<RestaurantDetail | null>(null);
  const [reviews, setReviews] = useState<Review[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);
  const [body, setBody] = useState("");
  const [score, setScore] = useState(5);

  const load = useCallback(async () => {
    setError(null);
    try {
      const [detail, reviewPage] = await Promise.all([
        api.get<RestaurantDetail>(`/restaurants/${id}`),
        api.get<Page<Review>>(`/restaurants/${id}/reviews?size=20`),
      ]);
      setRestaurant(detail);
      setReviews(reviewPage.content);
    } catch (e) {
      setError(e);
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    void load();
  }, [load]);

  async function toggleFavourite() {
    if (!restaurant) return;
    try {
      if (restaurant.favourited) {
        await api.delete(`/restaurants/${id}/favourite`);
      } else {
        await api.put(`/restaurants/${id}/favourite`);
      }
      setRestaurant({ ...restaurant, favourited: !restaurant.favourited });
    } catch (e) {
      setError(e);
    }
  }

  async function submitReview(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    try {
      await api.post(`/restaurants/${id}/reviews`, { body, score });
      setBody("");
      await load();
    } catch (e) {
      setError(e);
    }
  }

  async function deleteReview(reviewId: number) {
    try {
      await api.delete(`/restaurants/reviews/${reviewId}`);
      await load();
    } catch (e) {
      setError(e);
    }
  }

  if (loading) return <Spinner />;
  if (!restaurant) return <ErrorNote error={error ?? new Error("找不到這間餐廳。")} />;

  // Group opening hours by weekday; the API returns one row per interval.
  const hoursByDay = new Map<number, string[]>();
  for (const hour of restaurant.businessHours) {
    const list = hoursByDay.get(hour.dayOfWeek) ?? [];
    list.push(`${hour.opensAt.slice(0, 5)}–${hour.closesAt.slice(0, 5)}`);
    hoursByDay.set(hour.dayOfWeek, list);
  }

  return (
    <div className="mx-auto max-w-7xl space-y-8 px-6 py-10">
      <header>
        <div className="flex flex-wrap items-start justify-between gap-4">
          <div>
            <h1 className="text-3xl font-bold">{restaurant.name}</h1>
            <p className="mt-1 text-stone-500">{restaurant.address}</p>
            <div className="mt-2">
              <Stars average={restaurant.rating.average} count={restaurant.rating.reviewCount} />
            </div>
          </div>
          <div className="flex gap-2">
            {user && (
              <Button variant={restaurant.favourited ? "primary" : "ghost"} onClick={toggleFavourite}>
                {restaurant.favourited ? "★ 已收藏" : "☆ 收藏"}
              </Button>
            )}
            {restaurant.editable && (
              <Link href={`/company/restaurants/${restaurant.id}`}>
                <Button variant="ghost">編輯</Button>
              </Link>
            )}
          </div>
        </div>
        <div className="mt-3 flex flex-wrap gap-1.5">
          {restaurant.tags.map((tag) => (
            <TagPill key={tag.id}>{tag.name}</TagPill>
          ))}
        </div>
      </header>

      <ErrorNote error={error} />

      <div className="grid gap-6 lg:grid-cols-3">
        <Card className="p-5">
          <h2 className="font-semibold">店家資訊</h2>
          <dl className="mt-3 space-y-2 text-sm">
            {restaurant.contact && (
              <div className="flex gap-2">
                <dt className="text-stone-500">電話</dt>
                <dd>{restaurant.contact}</dd>
              </div>
            )}
            {restaurant.website && (
              <div className="flex gap-2">
                <dt className="text-stone-500">網站</dt>
                <dd>
                  <a href={restaurant.website} target="_blank" rel="noreferrer" className="text-red-600 hover:underline">
                    前往
                  </a>
                </dd>
              </div>
            )}
            <div className="flex gap-2">
              <dt className="text-stone-500">店家</dt>
              <dd>{restaurant.owner.name}</dd>
            </div>
          </dl>
        </Card>

        <Card className="p-5">
          <h2 className="font-semibold">營業時間</h2>
          <dl className="mt-3 space-y-1 text-sm">
            {DAYS.map((label, day) => (
              <div key={day} className="flex justify-between gap-2">
                <dt className="text-stone-500">{label}</dt>
                <dd className={hoursByDay.has(day) ? "" : "text-stone-400"}>
                  {hoursByDay.get(day)?.join("、") ?? "公休"}
                </dd>
              </div>
            ))}
          </dl>
        </Card>

        <Card className="p-5">
          <h2 className="font-semibold">進行中的活動</h2>
          {restaurant.activeEvents.length === 0 ? (
            <p className="mt-3 text-sm text-stone-400">目前沒有活動。</p>
          ) : (
            <ul className="mt-3 space-y-3">
              {restaurant.activeEvents.map((event) => (
                <li key={event.id}>
                  <p className="text-sm font-medium">{event.name}</p>
                  <p className="text-xs text-stone-500">{event.content}</p>
                  <p className="mt-0.5 text-xs text-stone-400">
                    {event.startsOn} ~ {event.endsOn}
                  </p>
                </li>
              ))}
            </ul>
          )}
        </Card>
      </div>

      {restaurant.menu.length > 0 && (
        <section>
          <h2 className="mb-3 text-xl font-semibold">菜單</h2>
          <div className="grid gap-4 sm:grid-cols-3">
            {restaurant.menu.map((item) => (
              <Card key={item.id} className="overflow-hidden">
                {/* eslint-disable-next-line @next/next/no-img-element */}
                <img src={item.imageUrl} alt={item.caption ?? "菜單"} className="h-48 w-full object-cover" />
                {item.caption && <p className="p-3 text-sm">{item.caption}</p>}
              </Card>
            ))}
          </div>
        </section>
      )}

      <section>
        <h2 className="mb-3 text-xl font-semibold">評論 ({reviews.length})</h2>

        {user ? (
          <Card className="mb-4 p-5">
            <form onSubmit={submitReview} className="space-y-3">
              <div className="flex items-center gap-2">
                <label htmlFor="score" className="text-sm font-medium">
                  評分
                </label>
                <select
                  id="score"
                  value={score}
                  onChange={(e) => setScore(Number(e.target.value))}
                  className="rounded-lg border border-stone-300 bg-white px-2 py-1 text-sm dark:border-stone-700 dark:bg-stone-900"
                >
                  {[5, 4, 3, 2, 1].map((n) => (
                    <option key={n} value={n}>
                      {"★".repeat(n)}
                    </option>
                  ))}
                </select>
              </div>
              <textarea
                required
                maxLength={2000}
                value={body}
                onChange={(e) => setBody(e.target.value)}
                placeholder="分享你的用餐體驗…"
                className="w-full rounded-lg border border-stone-300 bg-white px-3 py-2 text-sm outline-none focus:border-red-500 dark:border-stone-700 dark:bg-stone-900"
                rows={3}
              />
              <Button type="submit">發表評論</Button>
            </form>
          </Card>
        ) : (
          <p className="mb-4 text-sm text-stone-500">
            <Link href="/login" className="text-red-600 hover:underline">
              登入
            </Link>{" "}
            後即可留下評論。
          </p>
        )}

        {reviews.length === 0 ? (
          <Empty>還沒有人評論，成為第一個吧！</Empty>
        ) : (
          <ul className="space-y-4">
            {reviews.map((review) => (
              <Card key={review.id} className="p-5">
                <div className="flex items-start justify-between gap-3">
                  <div>
                    <p className="font-medium">{review.author.displayName}</p>
                    <p className="text-xs text-stone-400">{new Date(review.createdAt).toLocaleDateString("zh-TW")}</p>
                  </div>
                  {review.score && <span className="text-amber-500">{"★".repeat(review.score)}</span>}
                </div>
                <p className="mt-2 whitespace-pre-wrap text-sm">{review.body}</p>

                {review.replies.length > 0 && (
                  <ul className="mt-3 space-y-2 border-l-2 border-stone-200 pl-4 dark:border-stone-700">
                    {review.replies.map((reply) => (
                      <li key={reply.id} className="text-sm">
                        <span className="font-medium">{reply.author.displayName}</span>
                        {reply.fromRestaurantOwner && (
                          <span className="ml-1 rounded bg-red-100 px-1.5 py-0.5 text-[11px] text-red-700 dark:bg-red-950 dark:text-red-300">
                            店家
                          </span>
                        )}
                        <p className="mt-0.5 whitespace-pre-wrap text-stone-600 dark:text-stone-400">{reply.body}</p>
                      </li>
                    ))}
                  </ul>
                )}

                {review.editable && (
                  <button
                    onClick={() => deleteReview(review.id)}
                    className="mt-3 text-xs text-stone-400 hover:text-red-600"
                  >
                    刪除
                  </button>
                )}
              </Card>
            ))}
          </ul>
        )}
      </section>
    </div>
  );
}
