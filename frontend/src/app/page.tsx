"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { Card, Spinner, Stars, TagPill } from "@/components/ui";
import type {
  Campaign,
  District,
  HighlightReview,
  Page,
  RestaurantSummary,
  Tag,
} from "@/lib/types";

/** Section heading in the 2021 style: script kicker over a bold title. */
function SectionTitle({ kicker, title }: { kicker: string; title: string }) {
  return (
    <div className="mb-10 text-center">
      <span className="font-script text-4xl text-pepper">{kicker}</span>
      <h2 className="mt-1 text-3xl font-bold sm:text-4xl">{title}</h2>
    </div>
  );
}

export default function HomePage() {
  const router = useRouter();
  const [restaurants, setRestaurants] = useState<RestaurantSummary[]>([]);
  const [districts, setDistricts] = useState<District[]>([]);
  const [campaigns, setCampaigns] = useState<Campaign[]>([]);
  const [reviews, setReviews] = useState<HighlightReview[]>([]);
  const [tags, setTags] = useState<Tag[]>([]);
  const [loading, setLoading] = useState(true);
  const [term, setTerm] = useState("");
  const [scope, setScope] = useState("restaurants");

  useEffect(() => {
    const anon = { anonymous: true } as const;
    Promise.all([
      api.get<Page<RestaurantSummary>>("/restaurants?size=6", anon).then((p) => p.content),
      api.get<District[]>("/discovery/districts?limit=6", anon),
      api.get<Campaign[]>("/discovery/campaigns?limit=4", anon),
      api.get<HighlightReview[]>("/discovery/reviews?limit=3", anon),
      api.get<Tag[]>("/food-tags", anon),
    ])
      .then(([r, d, c, rv, t]) => {
        setRestaurants(r);
        setDistricts(d);
        setCampaigns(c);
        setReviews(rv);
        setTags(t);
      })
      .catch(() => undefined)
      .finally(() => setLoading(false));
  }, []);

  function search(event: React.FormEvent) {
    event.preventDefault();
    const path = scope === "shop" ? "/shop" : "/restaurants";
    router.push(term.trim() ? `${path}?q=${encodeURIComponent(term.trim())}` : path);
  }

  return (
    <>
      {/* ---------- Hero ---------- */}
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
        </div>
      </section>

      {loading ? (
        <Spinner />
      ) : (
        <>
          {/* ---------- 熱門地點 ---------- */}
          <section className="bg-mist py-20">
            <div className="mx-auto max-w-7xl px-6">
              <SectionTitle kicker="Most visited restaurants" title="熱門地點" />
              <div className="grid grid-cols-2 gap-5 sm:grid-cols-3 lg:grid-cols-6">
                {districts.map((district) => (
                  <Link
                    key={district.district}
                    href={`/restaurants?q=${encodeURIComponent(district.district)}`}
                    className="group relative flex h-40 flex-col items-center justify-center overflow-hidden rounded-xl bg-ink/85 text-white transition hover:bg-pepper"
                  >
                    <span className="text-xl font-bold">{district.district}</span>
                    <span className="mt-1 text-sm text-white/70">
                      {district.restaurantCount} 間餐廳
                    </span>
                  </Link>
                ))}
              </div>
              <div className="mt-10 text-center">
                <Link
                  href="/map"
                  className="inline-block rounded-full bg-pepper px-8 py-3 font-display text-sm font-bold uppercase tracking-wide text-white transition hover:bg-pepper-dark"
                >
                  在地圖上看全部
                </Link>
              </div>
            </div>
          </section>

          {/* ---------- 如何搜尋 ---------- */}
          <section className="py-20">
            <div className="mx-auto max-w-7xl px-6">
              <SectionTitle kicker="Easy to explore" title="如何搜尋" />
              <div className="grid gap-8 md:grid-cols-3">
                <Card className="p-8 text-center">
                  <span className="font-script text-5xl text-pepper">1</span>
                  <h3 className="mt-2 text-lg font-bold">選擇喜歡的餐廳種類</h3>
                  <div className="mt-4 flex flex-wrap justify-center gap-2">
                    {tags.slice(0, 7).map((tag) => (
                      <Link key={tag.id} href={`/restaurants?tagIds=${tag.id}`}>
                        <TagPill>{tag.name}</TagPill>
                      </Link>
                    ))}
                  </div>
                  <p className="mt-4 text-sm text-stone-500">我全都要!!!</p>
                </Card>

                <Card className="p-8 text-center">
                  <span className="font-script text-5xl text-pepper">2</span>
                  <h3 className="mt-2 text-lg font-bold">選擇喜歡的地點</h3>
                  <div className="mt-4 flex flex-wrap justify-center gap-2">
                    {districts.slice(0, 6).map((d) => (
                      <Link key={d.district} href={`/restaurants?q=${encodeURIComponent(d.district)}`}>
                        <TagPill>{d.district}</TagPill>
                      </Link>
                    ))}
                  </div>
                  <p className="mt-4 text-sm text-stone-500">吃遍全台北!!!</p>
                </Card>

                <Card className="p-8 text-center">
                  <span className="font-script text-5xl text-pepper">3</span>
                  <h3 className="mt-2 text-lg font-bold">立馬ㄘㄨ花!!!享食趣</h3>
                  <p className="mt-4 text-sm text-stone-500">約會、聚會、找餐廳不採雷</p>
                  <Link
                    href="/map"
                    className="mt-6 inline-block rounded-full bg-pepper px-6 py-2.5 font-display text-sm font-bold uppercase text-white transition hover:bg-pepper-dark"
                  >
                    開始搜尋
                  </Link>
                </Card>
              </div>
            </div>
          </section>

          {/* ---------- 美食優惠 ---------- */}
          {campaigns.length > 0 && (
            <section className="bg-mist py-20">
              <div className="mx-auto max-w-7xl px-6">
                <SectionTitle kicker="We are offering for you" title="美食優惠" />
                <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
                  {campaigns.map((campaign) => (
                    <Card key={campaign.id} className="flex h-full flex-col overflow-hidden">
                      <div className="flex h-32 items-center justify-center bg-gradient-to-br from-pepper to-gold text-4xl">
                        🎉
                      </div>
                      <div className="flex flex-1 flex-col p-5">
                        <h3 className="font-bold">{campaign.name}</h3>
                        <p className="mt-2 flex-1 text-sm text-stone-500">{campaign.content}</p>
                        <p className="mt-3 text-xs text-stone-400">
                          {campaign.startsOn} ~ {campaign.endsOn}
                        </p>
                        <Link
                          href={`/restaurants/${campaign.restaurantId}`}
                          className="mt-3 text-sm font-medium text-pepper hover:underline"
                        >
                          {campaign.restaurantName} · 查看詳情 →
                        </Link>
                      </div>
                    </Card>
                  ))}
                </div>
              </div>
            </section>
          )}

          {/* ---------- 賣點 ---------- */}
          <section className="py-20">
            <div className="mx-auto grid max-w-7xl items-center gap-12 px-6 lg:grid-cols-2">
              <div
                className="h-80 rounded-2xl bg-cover bg-center"
                style={{ backgroundImage: "url(/brand/hero-shop.jpg)" }}
              />
              <div>
                <span className="font-script text-4xl text-pepper">We are offering for you</span>
                <p className="mt-2 text-2xl font-bold leading-snug">
                  每個月，數以萬計的人潮來灑胡椒麵,
                  <br />
                  今天中午我想來點…
                </p>
                <ul className="mt-8 space-y-5">
                  {[
                    ["最真實的評論、最好吃的餐廳、最優惠的價格", "真的嗎?歐真的"],
                    ["囊括全台北最好吃的餐廳", `目前收錄 ${districts.reduce((n, d) => n + d.restaurantCount, 0)} 間`],
                    ["最多分類可以選", `${tags.length} 種美食標籤任你挑`],
                  ].map(([title, note]) => (
                    <li key={title} className="flex gap-4">
                      <span className="mt-1 flex h-6 w-6 shrink-0 items-center justify-center rounded-full bg-pepper text-xs font-bold text-white">
                        ✓
                      </span>
                      <div>
                        <p className="font-semibold">{title}</p>
                        <p className="text-sm text-stone-500">{note}</p>
                      </div>
                    </li>
                  ))}
                </ul>
              </div>
            </div>
          </section>

          {/* ---------- 客戶評論 ---------- */}
          {reviews.length > 0 && (
            <section className="bg-mist py-20">
              <div className="mx-auto max-w-7xl px-6">
                <SectionTitle kicker="What our client say" title="客戶評論" />
                <div className="grid gap-6 md:grid-cols-3">
                  {reviews.map((review) => (
                    <Card key={review.id} className="flex h-full flex-col p-7 text-center">
                      <p className="flex-1 text-sm leading-relaxed text-stone-600">
                        「{review.body}」
                      </p>
                      <div className="mt-5">
                        <p className="font-display text-lg font-bold">{review.authorName}</p>
                        <Link
                          href={`/restaurants/${review.restaurantId}`}
                          className="text-xs text-pepper hover:underline"
                        >
                          {review.restaurantName}
                        </Link>
                        {review.score && (
                          <p className="mt-1 text-amber-500">{"★".repeat(review.score)}</p>
                        )}
                      </div>
                    </Card>
                  ))}
                </div>
              </div>
            </section>
          )}

          {/* ---------- 最新餐廳 ---------- */}
          <section className="py-20">
            <div className="mx-auto max-w-7xl px-6">
              <SectionTitle kicker="Discover" title="最新餐廳" />
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
              <div className="mt-10 text-center">
                <Link
                  href="/restaurants"
                  className="inline-block rounded-full bg-pepper px-8 py-3 font-display text-sm font-bold uppercase tracking-wide text-white transition hover:bg-pepper-dark"
                >
                  看全部餐廳
                </Link>
              </div>
            </div>
          </section>
        </>
      )}
    </>
  );
}
