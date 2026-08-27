"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { NewsletterSignup } from "@/components/NewsletterSignup";
import {
  ButtonLink,
  Card,
  CardGridSkeleton,
  DisplayHeading,
  Stars,
  TagPill,
} from "@/components/ui";
import {
  IconArrowRight,
  IconCheck,
  IconMapPin,
  IconSearch,
  IconStarFilled,
  IconTag,
} from "@/components/icons";
import type {
  Campaign,
  District,
  HighlightReview,
  Page,
  RestaurantSummary,
  Tag,
} from "@/lib/types";

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

  const totalRestaurants = districts.reduce((n, d) => n + d.restaurantCount, 0);

  return (
    <>
      {/* ---------- Hero ---------- */}
      <section
        className="hero-overlay relative flex min-h-[620px] items-center justify-center bg-cover bg-center lg:min-h-[780px]"
        style={{ backgroundImage: "url(/brand/hero-home.jpg)" }}
      >
        <div className="relative z-10 mx-auto w-full max-w-3xl px-5 pb-10 pt-28 text-center sm:px-6">
          <span aria-hidden className="on-photo block font-script text-5xl leading-tight text-mint sm:text-6xl">
            Explore the Food
          </span>
          <h1 className="on-photo mt-2 font-display text-[2.75rem] font-bold leading-[1.06] tracking-tight text-white sm:text-6xl lg:text-7xl">
            走吧!美食之旅!
          </h1>
          <p className="on-photo mx-auto mt-5 max-w-lg text-base leading-relaxed text-white/90">
            收錄 {totalRestaurants || "上百"} 間台北餐廳、真實評論與店家優惠。
          </p>

          {/*
            The 2021 search ring. Below `sm` the three controls stack — at 375px
            an input, a select and a button on one row leaves the input about
            80px wide, which is unusable.
          */}
          <form
            onSubmit={search}
            className="search-ring mx-auto mt-9 flex max-w-2xl flex-col gap-2 bg-white p-2 sm:flex-row sm:items-center sm:gap-0 sm:p-0"
          >
            <label htmlFor="hero-search" className="sr-only">
              搜尋餐廳或商品
            </label>
            <div className="flex min-w-0 flex-1 items-center gap-2 px-4 sm:px-6">
              <IconSearch aria-hidden className="shrink-0 text-xl text-subtle" />
              <input
                id="hero-search"
                value={term}
                onChange={(e) => setTerm(e.target.value)}
                placeholder="今晚我想來點…"
                className="min-w-0 flex-1 bg-transparent py-3 text-[15px] text-ink outline-none placeholder:text-subtle"
              />
            </div>
            <label htmlFor="hero-scope" className="sr-only">
              搜尋範圍
            </label>
            <select
              id="hero-scope"
              value={scope}
              onChange={(e) => setScope(e.target.value)}
              className="min-h-11 cursor-pointer rounded-lg border-line bg-transparent px-4 text-sm text-body outline-none sm:border-l"
            >
              <option value="restaurants">餐廳</option>
              <option value="shop">商城</option>
            </select>
            <button
              type="submit"
              className="min-h-12 cursor-pointer rounded-full bg-pepper-fill px-8 font-display text-sm font-bold uppercase tracking-wider text-white transition hover:bg-pepper-dark"
            >
              Search
            </button>
          </form>
        </div>
      </section>

      {/* ---------- 熱門地點 ---------- */}
      <section className="bg-mist py-20 lg:py-24">
        <div className="mx-auto max-w-7xl px-5 sm:px-6">
          <DisplayHeading kicker="Most visited restaurants" title="熱門地點" />

          {loading ? (
            <CardGridSkeleton count={6} columns="grid-cols-2 sm:grid-cols-3 lg:grid-cols-6" />
          ) : (
            <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 lg:grid-cols-6">
              {districts.map((district) => (
                <Link
                  key={district.district}
                  href={`/restaurants?q=${encodeURIComponent(district.district)}`}
                  className="group relative flex h-36 flex-col items-center justify-center gap-1 overflow-hidden rounded-2xl border border-line bg-white shadow-card transition duration-200 hover:-translate-y-1 hover:border-pepper hover:shadow-lift motion-reduce:hover:translate-y-0"
                >
                  <IconMapPin
                    aria-hidden
                    className="text-xl text-subtle transition group-hover:text-pepper"
                  />
                  <span className="font-display text-lg font-bold text-ink">{district.district}</span>
                  <span className="text-[13px] tabular text-subtle">
                    {district.restaurantCount} 間餐廳
                  </span>
                </Link>
              ))}
            </div>
          )}

          <div className="mt-12 text-center">
            <ButtonLink href="/map" size="lg" icon={<IconMapPin />}>
              在地圖上看全部
            </ButtonLink>
          </div>
        </div>
      </section>

      {/* ---------- 如何搜尋 ---------- */}
      <section className="py-20 lg:py-24">
        <div className="mx-auto max-w-7xl px-5 sm:px-6">
          <DisplayHeading kicker="Easy to explore" title="如何搜尋" />
          <ol className="grid gap-6 md:grid-cols-3">
            <Step index={1} title="選擇喜歡的餐廳種類" note="我全都要!!!">
              <div className="flex flex-wrap justify-center gap-2">
                {tags.slice(0, 7).map((tag) => (
                  <Link key={tag.id} href={`/restaurants?tagIds=${tag.id}`} className="inline-flex min-h-10 items-center transition hover:opacity-80">
                    <TagPill>{tag.name}</TagPill>
                  </Link>
                ))}
              </div>
            </Step>

            <Step index={2} title="選擇喜歡的地點" note="吃遍全台北!!!">
              <div className="flex flex-wrap justify-center gap-2">
                {districts.slice(0, 6).map((d) => (
                  <Link
                    key={d.district}
                    href={`/restaurants?q=${encodeURIComponent(d.district)}`}
                    className="inline-flex min-h-10 items-center transition hover:opacity-80"
                  >
                    <TagPill>{d.district}</TagPill>
                  </Link>
                ))}
              </div>
            </Step>

            <Step index={3} title="立馬ㄘㄨ花!!!享食趣" note="約會、聚會、找餐廳不採雷">
              <ButtonLink href="/map" icon={<IconSearch />}>
                開始搜尋
              </ButtonLink>
            </Step>
          </ol>
        </div>
      </section>

      {/* ---------- 美食優惠 ---------- */}
      {campaigns.length > 0 && (
        <section className="bg-mist py-20 lg:py-24">
          <div className="mx-auto max-w-7xl px-5 sm:px-6">
            <DisplayHeading kicker="We are offering for you" title="美食優惠" />
            <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
              {campaigns.map((campaign) => (
                <Card key={campaign.id} as="article" className="flex h-full flex-col overflow-hidden">
                  <div className="flex h-28 items-center justify-center bg-gradient-to-br from-pepper to-gold text-3xl text-white">
                    <IconTag aria-hidden />
                  </div>
                  <div className="flex flex-1 flex-col p-5">
                    <h3 className="font-display text-lg font-bold text-ink">{campaign.name}</h3>
                    <p className="mt-2 flex-1 text-sm leading-relaxed text-body">{campaign.content}</p>
                    <p className="mt-3 text-xs tabular text-subtle">
                      {campaign.startsOn} ~ {campaign.endsOn}
                    </p>
                    <Link
                      href={`/restaurants/${campaign.restaurantId}`}
                      className="-mx-2 mt-2 inline-flex min-h-11 items-center gap-1.5 rounded-lg px-2 text-sm font-semibold text-pepper-ink transition hover:bg-pepper-tint sm:mx-0 sm:mt-3 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-2"
                    >
                      {campaign.restaurantName}
                      <IconArrowRight aria-hidden className="text-base" />
                    </Link>
                  </div>
                </Card>
              ))}
            </div>
          </div>
        </section>
      )}

      {/* ---------- 賣點 ---------- */}
      <section className="py-20 lg:py-24">
        <div className="mx-auto grid max-w-7xl items-center gap-12 px-5 sm:px-6 lg:grid-cols-2 lg:gap-16">
          <div
            className="h-72 rounded-3xl bg-cover bg-center shadow-lift lg:h-[26rem]"
            style={{ backgroundImage: "url(/brand/hero-shop.jpg)" }}
            role="img"
            aria-label="餐桌上的料理"
          />
          <div>
            <span aria-hidden className="font-script text-4xl leading-none text-pepper">
              We are offering for you
            </span>
            <p className="mt-3 font-display text-2xl font-bold leading-snug text-ink sm:text-3xl">
              每個月，數以萬計的人潮來灑胡椒麵,
              <br />
              今天中午我想來點…
            </p>
            <ul className="mt-8 space-y-6">
              {[
                ["最真實的評論、最好吃的餐廳、最優惠的價格", "真的嗎?歐真的"],
                ["囊括全台北最好吃的餐廳", `目前收錄 ${totalRestaurants} 間`],
                ["最多分類可以選", `${tags.length} 種美食標籤任你挑`],
              ].map(([title, note]) => (
                <li key={title} className="flex gap-4">
                  <span
                    aria-hidden
                    className="mt-0.5 flex h-7 w-7 shrink-0 items-center justify-center rounded-full bg-pepper-fill text-sm text-white"
                  >
                    <IconCheck />
                  </span>
                  <div>
                    <p className="font-semibold text-ink">{title}</p>
                    <p className="mt-0.5 text-sm text-subtle">{note}</p>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </section>

      {/* ---------- 客戶評論 ---------- */}
      {reviews.length > 0 && (
        <section className="bg-mist py-20 lg:py-24">
          <div className="mx-auto max-w-7xl px-5 sm:px-6">
            <DisplayHeading kicker="What our client say" title="客戶評論" />
            <div className="grid gap-6 md:grid-cols-3">
              {reviews.map((review) => (
                <Card key={review.id} as="article" className="flex h-full flex-col p-8 text-center">
                  {review.score && (
                    <p className="mb-4 flex justify-center gap-0.5 text-base text-gold" aria-label={`${review.score} 分`}>
                      {Array.from({ length: review.score }, (_, i) => (
                        <IconStarFilled key={i} />
                      ))}
                    </p>
                  )}
                  <blockquote className="flex-1 text-[15px] leading-relaxed text-body">
                    「{review.body}」
                  </blockquote>
                  <footer className="mt-6 border-t border-line pt-4">
                    <p className="font-display text-lg font-bold text-ink">{review.authorName}</p>
                    <Link
                      href={`/restaurants/${review.restaurantId}`}
                      className="inline-flex min-h-11 items-center text-[13px] font-medium text-pepper-ink transition hover:underline underline-offset-2 sm:min-h-0"
                    >
                      {review.restaurantName}
                    </Link>
                  </footer>
                </Card>
              ))}
            </div>
          </div>
        </section>
      )}

      {/* ---------- 訂閱電子報 ---------- */}
      <section
        className="hero-overlay relative bg-cover bg-center py-20 lg:py-24"
        style={{ backgroundImage: "url(/brand/hero-shop.jpg)" }}
      >
        <div className="relative z-10 mx-auto max-w-4xl px-5 text-center sm:px-6">
          <span aria-hidden className="on-photo font-script text-4xl leading-none text-mint">
            Subscribe our newsletter
          </span>
          <h2 className="on-photo mb-8 mt-2 font-display text-3xl font-bold tracking-tight text-white sm:text-4xl">
            Subscribe For Newsletter
          </h2>
          <NewsletterSignup source="home" />
        </div>
      </section>

      {/* ---------- 最新餐廳 ---------- */}
      <section className="py-20 lg:py-24">
        <div className="mx-auto max-w-7xl px-5 sm:px-6">
          <DisplayHeading kicker="Discover" title="最新餐廳" />
          {loading ? (
            <CardGridSkeleton count={6} />
          ) : (
            <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
              {restaurants.map((restaurant) => (
                <Link key={restaurant.id} href={`/restaurants/${restaurant.id}`} className="group">
                  <Card interactive className="h-full p-6">
                    <h3 className="font-display text-lg font-bold text-ink transition group-hover:text-pepper-ink">
                      {restaurant.name}
                    </h3>
                    <p className="mt-1.5 flex items-start gap-1.5 text-sm text-subtle">
                      <IconMapPin aria-hidden className="mt-0.5 shrink-0 text-base" />
                      {restaurant.address}
                    </p>
                    <div className="mt-4">
                      <Stars average={restaurant.rating.average} count={restaurant.rating.reviewCount} />
                    </div>
                    <div className="mt-4 flex flex-wrap gap-1.5">
                      {restaurant.tags.slice(0, 3).map((tag) => (
                        <TagPill key={tag.id}>{tag.name}</TagPill>
                      ))}
                    </div>
                  </Card>
                </Link>
              ))}
            </div>
          )}
          <div className="mt-12 text-center">
            <ButtonLink href="/restaurants" size="lg">
              看全部餐廳
              <IconArrowRight />
            </ButtonLink>
          </div>
        </div>
      </section>
    </>
  );
}

/** One numbered card in the 如何搜尋 sequence. */
function Step({
  index,
  title,
  note,
  children,
}: {
  index: number;
  title: string;
  note: string;
  children: React.ReactNode;
}) {
  return (
    <li className="flex">
      <Card className="flex w-full flex-col items-center p-8 text-center">
        <span aria-hidden className="font-script text-5xl leading-none text-pepper">
          {index}
        </span>
        <h3 className="mt-3 font-display text-lg font-bold text-ink">{title}</h3>
        <div className="mt-5 flex flex-1 items-start justify-center">{children}</div>
        <p className="mt-5 text-sm text-subtle">{note}</p>
      </Card>
    </li>
  );
}
