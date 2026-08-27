"use client";

import { use, useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import {
  Alert,
  Button,
  ButtonLink,
  Card,
  CharCount,
  Empty,
  ErrorNote,
  PageShell,
  SectionHeader,
  Spinner,
  Stars,
  TagPill,
  Textarea,
  TextLink,
} from "@/components/ui";
import {
  IconClock,
  IconExternal,
  IconGlobe,
  IconHeart,
  IconHeartFilled,
  IconMapPin,
  IconMessage,
  IconPencil,
  IconPhone,
  IconStar,
  IconStarFilled,
  IconStore,
  IconTag,
  IconTrash,
  IconUser,
} from "@/components/icons";
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
  const [submitting, setSubmitting] = useState(false);
  /** Which review the reply box is open under. */
  const [replyingTo, setReplyingTo] = useState<number | null>(null);
  const [replyBody, setReplyBody] = useState("");

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
    setSubmitting(true);
    try {
      await api.post(`/restaurants/${id}/reviews`, { body, score });
      setBody("");
      await load();
    } catch (e) {
      setError(e);
    } finally {
      setSubmitting(false);
    }
  }

  async function submitReply(reviewId: number) {
    setError(null);
    try {
      await api.post(`/restaurants/reviews/${reviewId}/replies`, { body: replyBody });
      setReplyingTo(null);
      setReplyBody("");
      await load();
    } catch (e) {
      setError(e);
    }
  }

  async function deleteReply(replyId: number) {
    setError(null);
    try {
      await api.delete(`/restaurants/reviews/replies/${replyId}`);
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
  if (!restaurant) {
    return (
      <PageShell width="reading">
        <ErrorNote error={error ?? new Error("找不到這間餐廳。")} />
      </PageShell>
    );
  }

  // Group opening hours by weekday; the API returns one row per interval.
  const hoursByDay = new Map<number, string[]>();
  for (const hour of restaurant.businessHours) {
    const list = hoursByDay.get(hour.dayOfWeek) ?? [];
    list.push(`${hour.opensAt.slice(0, 5)}–${hour.closesAt.slice(0, 5)}`);
    hoursByDay.set(hour.dayOfWeek, list);
  }
  const today = new Date().getDay();

  return (
    <PageShell>
      {/* ---------- Masthead ---------- */}
      <header className="mb-8 border-b border-line pb-7">
        <div className="flex flex-wrap items-start justify-between gap-x-8 gap-y-5">
          <div className="min-w-0 flex-1">
            <h1 className="font-display text-3xl font-bold leading-tight tracking-tight text-ink sm:text-4xl">
              {restaurant.name}
            </h1>
            <p className="mt-2.5 flex items-start gap-2 text-[15px] leading-relaxed text-body">
              <IconMapPin aria-hidden className="mt-0.5 shrink-0 text-lg text-subtle" />
              {restaurant.address}
            </p>
            <div className="mt-3">
              <Stars average={restaurant.rating.average} count={restaurant.rating.reviewCount} size="md" />
            </div>
            {restaurant.tags.length > 0 && (
              <div className="mt-4 flex flex-wrap gap-1.5">
                {restaurant.tags.map((tag) => (
                  <TagPill key={tag.id}>{tag.name}</TagPill>
                ))}
              </div>
            )}
          </div>

          <div className="flex shrink-0 flex-wrap gap-2.5">
            {user && (
              <Button
                variant={restaurant.favourited ? "primary" : "ghost"}
                onClick={toggleFavourite}
                aria-pressed={restaurant.favourited}
                icon={restaurant.favourited ? <IconHeartFilled /> : <IconHeart />}
              >
                {restaurant.favourited ? "已收藏" : "收藏"}
              </Button>
            )}
            {restaurant.editable && (
              <ButtonLink href={`/company/restaurants/${restaurant.id}`} variant="ghost" icon={<IconPencil />}>
                編輯
              </ButtonLink>
            )}
          </div>
        </div>
      </header>

      <ErrorNote error={error} />

      {/* ---------- Facts ---------- */}
      <div className="mt-6 grid gap-5 lg:grid-cols-3">
        <Card className="p-6">
          <h2 className="flex items-center gap-2 font-display text-base font-bold text-ink">
            <IconStore aria-hidden className="text-lg text-subtle" />
            店家資訊
          </h2>
          <dl className="mt-4 space-y-3 text-sm">
            {restaurant.contact && (
              <div className="flex gap-3">
                <dt className="flex w-16 shrink-0 items-center gap-1.5 text-subtle">
                  <IconPhone aria-hidden className="text-base" />
                  電話
                </dt>
                <dd className="tabular text-ink">
                  <a href={`tel:${restaurant.contact}`} className="-mx-2 inline-flex min-h-11 items-center rounded-lg px-2 transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline">
                    {restaurant.contact}
                  </a>
                </dd>
              </div>
            )}
            {restaurant.website && (
              <div className="flex gap-3">
                <dt className="flex w-16 shrink-0 items-center gap-1.5 text-subtle">
                  <IconGlobe aria-hidden className="text-base" />
                  網站
                </dt>
                <dd>
                  <a
                    href={restaurant.website}
                    target="_blank"
                    rel="noreferrer"
                    className="inline-flex items-center gap-1 font-medium text-pepper-ink underline-offset-2 hover:underline"
                  >
                    前往
                    <IconExternal aria-hidden className="text-sm" />
                    <span className="sr-only">（開新視窗）</span>
                  </a>
                </dd>
              </div>
            )}
            <div className="flex gap-3">
              <dt className="flex w-16 shrink-0 items-center gap-1.5 text-subtle">
                <IconUser aria-hidden className="text-base" />
                店家
              </dt>
              <dd className="text-ink">{restaurant.owner.name}</dd>
            </div>
          </dl>
        </Card>

        <Card className="p-6">
          <h2 className="flex items-center gap-2 font-display text-base font-bold text-ink">
            <IconClock aria-hidden className="text-lg text-subtle" />
            營業時間
          </h2>
          <dl className="mt-4 space-y-1.5 text-sm">
            {DAYS.map((label, day) => {
              const open = hoursByDay.has(day);
              const isToday = day === today;
              return (
                <div
                  key={day}
                  className={`flex justify-between gap-3 rounded-lg px-2 py-1 ${
                    isToday ? "bg-pepper-tint font-semibold text-ink" : ""
                  }`}
                >
                  <dt className={isToday ? "" : "text-subtle"}>
                    {label}
                    {isToday && <span className="ml-1.5 text-xs font-medium text-pepper-ink">今天</span>}
                  </dt>
                  <dd className={`tabular ${open ? "text-ink" : "text-subtle"}`}>
                    {hoursByDay.get(day)?.join("、") ?? "公休"}
                  </dd>
                </div>
              );
            })}
          </dl>
        </Card>

        <Card className="p-6">
          <h2 className="flex items-center gap-2 font-display text-base font-bold text-ink">
            <IconTag aria-hidden className="text-lg text-subtle" />
            進行中的活動
          </h2>
          {restaurant.activeEvents.length === 0 ? (
            <p className="mt-4 text-sm text-subtle">目前沒有活動。</p>
          ) : (
            <ul className="mt-4 space-y-4">
              {restaurant.activeEvents.map((event) => (
                <li key={event.id} className="border-l-2 border-pepper pl-3">
                  <p className="text-sm font-semibold text-ink">{event.name}</p>
                  <p className="mt-0.5 text-sm leading-relaxed text-body">{event.content}</p>
                  <p className="mt-1 text-xs tabular text-subtle">
                    {event.startsOn} ~ {event.endsOn}
                  </p>
                </li>
              ))}
            </ul>
          )}
        </Card>
      </div>

      {/* ---------- Menu ---------- */}
      {restaurant.menu.length > 0 && (
        <section className="mt-14">
          <SectionHeader title="菜單" count={restaurant.menu.length} />
          <ul className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
            {restaurant.menu.map((item) => (
              <li key={item.id}>
                <Card className="overflow-hidden">
                  {/* eslint-disable-next-line @next/next/no-img-element */}
                  <img
                    src={item.imageUrl}
                    alt={item.caption ?? `${restaurant.name} 的菜單`}
                    loading="lazy"
                    className="aspect-4/3 w-full object-cover"
                  />
                  {item.caption && <p className="p-4 text-sm text-body">{item.caption}</p>}
                </Card>
              </li>
            ))}
          </ul>
        </section>
      )}

      {/* ---------- Reviews ---------- */}
      <section className="mt-14">
        <SectionHeader title="評論" count={reviews.length} />

        {user ? (
          <Card className="mb-6 p-6">
            <form onSubmit={submitReview} className="space-y-4">
              <RatingInput value={score} onChange={setScore} />
              <div>
                <label htmlFor="review-body" className="mb-1.5 block text-sm font-semibold text-ink">
                  你的評論
                </label>
                <Textarea
                  id="review-body"
                  required
                  maxLength={2000}
                  value={body}
                  onChange={(e) => setBody(e.target.value)}
                  placeholder="分享你的用餐體驗…"
                  rows={4}
                />
                <CharCount value={body} max={2000} />
              </div>
              <Button type="submit" loading={submitting} icon={<IconMessage />}>
                發表評論
              </Button>
            </form>
          </Card>
        ) : (
          <div className="mb-6">
            <Alert tone="info">
              <TextLink href={`/login?next=/restaurants/${id}`}>登入</TextLink> 後即可留下評論。
            </Alert>
          </div>
        )}

        {reviews.length === 0 ? (
          <Empty icon={<IconMessage />}>還沒有人評論，成為第一個吧！</Empty>
        ) : (
          <ul className="space-y-5">
            {reviews.map((review) => (
              <Card key={review.id} as="li" className="p-6">
                <div className="flex items-start justify-between gap-4">
                  <div className="min-w-0">
                    <p className="font-semibold text-ink">{review.author.displayName}</p>
                    <p className="mt-0.5 text-xs tabular text-subtle">
                      {new Date(review.createdAt).toLocaleDateString("zh-TW")}
                    </p>
                  </div>
                  {review.score && (
                    <span className="flex shrink-0 gap-0.5 text-gold" aria-label={`${review.score} 分`}>
                      {Array.from({ length: 5 }, (_, i) =>
                        i < review.score! ? (
                          <IconStarFilled key={i} />
                        ) : (
                          <IconStar key={i} className="text-line-strong" />
                        ),
                      )}
                    </span>
                  )}
                </div>

                <p className="measure mt-3 whitespace-pre-wrap text-[15px] leading-relaxed text-body">
                  {review.body}
                </p>

                {review.replies.length > 0 && (
                  <ul className="mt-5 space-y-4 border-l-2 border-line pl-5">
                    {review.replies.map((reply) => (
                      <li key={reply.id}>
                        <p className="flex flex-wrap items-center gap-2 text-sm">
                          <span className="font-semibold text-ink">{reply.author.displayName}</span>
                          {reply.fromRestaurantOwner && (
                            <span className="rounded-full border border-pepper/25 bg-pepper-tint px-2 py-0.5 text-xs font-semibold text-pepper-ink">
                              店家回覆
                            </span>
                          )}
                        </p>
                        <p className="mt-1 whitespace-pre-wrap text-sm leading-relaxed text-body">{reply.body}</p>
                        {reply.editable && (
                          <button
                            onClick={() => deleteReply(reply.id)}
                            className="-mx-2 mt-1 inline-flex min-h-11 cursor-pointer items-center gap-1 rounded-lg px-2 text-xs text-subtle transition hover:bg-danger-tint hover:text-danger sm:mx-0 sm:mt-1.5 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
                          >
                            <IconTrash aria-hidden className="text-sm" />
                            刪除
                          </button>
                        )}
                      </li>
                    ))}
                  </ul>
                )}

                <div className="mt-4 flex gap-4 border-t border-line pt-3 text-[13px]">
                  {user && (
                    <button
                      onClick={() => {
                        setReplyingTo(replyingTo === review.id ? null : review.id);
                        setReplyBody("");
                      }}
                      aria-expanded={replyingTo === review.id}
                      className="-mx-2 inline-flex min-h-11 cursor-pointer items-center gap-1.5 rounded-lg px-2 font-medium text-subtle transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
                    >
                      <IconMessage aria-hidden className="text-base" />
                      回覆
                    </button>
                  )}
                  {review.editable && (
                    <button
                      onClick={() => deleteReview(review.id)}
                      className="-mx-2 inline-flex min-h-11 cursor-pointer items-center gap-1.5 rounded-lg px-2 font-medium text-subtle transition hover:bg-danger-tint hover:text-danger sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
                    >
                      <IconTrash aria-hidden className="text-base" />
                      刪除
                    </button>
                  )}
                </div>

                {replyingTo === review.id && (
                  <form
                    onSubmit={(e) => {
                      e.preventDefault();
                      submitReply(review.id);
                    }}
                    className="mt-4 flex flex-col gap-2.5 sm:flex-row"
                  >
                    <input
                      required
                      autoFocus
                      maxLength={1000}
                      value={replyBody}
                      onChange={(e) => setReplyBody(e.target.value)}
                      placeholder={
                        restaurant.editable ? "以店家身分回覆…" : `回覆 ${review.author.displayName}…`
                      }
                      aria-label="回覆內容"
                      className="min-h-11 flex-1 rounded-xl border border-line-strong bg-white px-3.5 text-sm text-ink transition placeholder:text-subtle focus:border-pepper focus:outline-none focus:ring-4 focus:ring-pepper/15"
                    />
                    <Button type="submit">送出</Button>
                  </form>
                )}
              </Card>
            ))}
          </ul>
        )}
      </section>
    </PageShell>
  );
}

/**
 * Star rating input.
 *
 * <p>Was a <select> of "★★★★★" strings, which a screen reader reads as five
 * identical "black star" characters. This is a radio group: each star is a real
 * radio with a text label, so it is arrow-key navigable and announces "4 分".
 */
function RatingInput({ value, onChange }: { value: number; onChange: (score: number) => void }) {
  return (
    <fieldset>
      <legend className="mb-1.5 text-sm font-semibold text-ink">評分</legend>
      <div className="flex items-center gap-1">
        {[1, 2, 3, 4, 5].map((n) => (
          <label
            key={n}
            className="cursor-pointer rounded-lg p-1 text-2xl text-gold transition hover:scale-110 has-[:focus-visible]:outline has-[:focus-visible]:outline-2 has-[:focus-visible]:outline-offset-2 has-[:focus-visible]:outline-pepper motion-reduce:hover:scale-100"
          >
            <input
              type="radio"
              name="score"
              value={n}
              checked={value === n}
              onChange={() => onChange(n)}
              className="sr-only"
            />
            {n <= value ? <IconStarFilled /> : <IconStar className="text-line-strong" />}
            <span className="sr-only">{n} 分</span>
          </label>
        ))}
        <span className="ml-2 text-sm tabular text-subtle">{value} / 5</span>
      </div>
    </fieldset>
  );
}
