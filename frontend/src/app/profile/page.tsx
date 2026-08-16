"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, ErrorNote, Input, Spinner, TagPill } from "@/components/ui";
import type { Page, RestaurantSummary, Tag, UserProfile } from "@/lib/types";

export default function ProfilePage() {
  const { user, loading: authLoading, refreshUser } = useAuth();
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [tags, setTags] = useState<Tag[]>([]);
  const [favourites, setFavourites] = useState<RestaurantSummary[]>([]);
  const [error, setError] = useState<unknown>(null);
  const [saved, setSaved] = useState(false);
  const [form, setForm] = useState({ realName: "", nickname: "", phone: "", location: "" });
  const [selectedTags, setSelectedTags] = useState<number[]>([]);
  const [passwords, setPasswords] = useState({ currentPassword: "", newPassword: "" });

  useEffect(() => {
    if (authLoading || !user) return;
    Promise.all([
      api.get<UserProfile>("/users/me"),
      api.get<Tag[]>("/food-tags", { anonymous: true }),
      api.get<Page<RestaurantSummary>>("/users/me/favourites"),
    ])
      .then(([me, allTags, favouritePage]) => {
        setProfile(me);
        setTags(allTags);
        setFavourites(favouritePage.content);
        setForm({
          realName: me.realName ?? "",
          nickname: me.nickname ?? "",
          phone: me.phone ?? "",
          location: me.location ?? "",
        });
        setSelectedTags(me.foodTags.map((t) => t.id));
      })
      .catch(setError);
  }, [authLoading, user]);

  if (authLoading) return <Spinner />;
  if (!user) {
    return (
      <p className="py-12 text-center text-sm text-stone-500">
        請先{" "}
        <Link href="/login?next=/profile" className="text-red-600 hover:underline">
          登入
        </Link>
        。
      </p>
    );
  }
  if (!profile) return <Spinner />;

  async function saveProfile(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setSaved(false);
    try {
      await api.put("/users/me", { ...form, foodTagIds: selectedTags });
      await refreshUser();
      setSaved(true);
    } catch (e) {
      setError(e);
    }
  }

  async function changePassword(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    try {
      await api.put("/users/me/password", passwords);
      setPasswords({ currentPassword: "", newPassword: "" });
      alert("密碼已更新，其他裝置的登入已全部登出。");
    } catch (e) {
      setError(e);
    }
  }

  return (
    <div className="space-y-8">
      <h1 className="text-2xl font-bold">個人資料</h1>
      <ErrorNote error={error} />
      {saved && <p className="rounded-lg bg-green-50 px-3 py-2 text-sm text-green-700">已儲存 ✓</p>}

      <div className="grid gap-6 lg:grid-cols-2">
        <Card className="p-6">
          <h2 className="font-semibold">基本資料</h2>
          <form onSubmit={saveProfile} className="mt-4 space-y-3">
            <div>
              <label className="mb-1 block text-sm">電子信箱</label>
              <Input value={profile.email} disabled />
            </div>
            {(["realName", "nickname", "phone", "location"] as const).map((key) => (
              <div key={key}>
                <label htmlFor={key} className="mb-1 block text-sm">
                  {{ realName: "姓名", nickname: "暱稱", phone: "手機", location: "居住地區" }[key]}
                </label>
                <Input id={key} value={form[key]} onChange={(e) => setForm({ ...form, [key]: e.target.value })} />
              </div>
            ))}

            <div>
              <span className="mb-2 block text-sm">興趣標籤</span>
              <div className="flex flex-wrap gap-2">
                {tags.map((tag) => {
                  const active = selectedTags.includes(tag.id);
                  return (
                    <button
                      type="button"
                      key={tag.id}
                      aria-pressed={active}
                      onClick={() =>
                        setSelectedTags(
                          active ? selectedTags.filter((id) => id !== tag.id) : [...selectedTags, tag.id],
                        )
                      }
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
            </div>

            <Button type="submit">儲存</Button>
          </form>
        </Card>

        <div className="space-y-6">
          <Card className="p-6">
            <h2 className="font-semibold">修改密碼</h2>
            <form onSubmit={changePassword} className="mt-4 space-y-3">
              <Input
                type="password"
                required
                placeholder="目前的密碼"
                aria-label="目前的密碼"
                value={passwords.currentPassword}
                onChange={(e) => setPasswords({ ...passwords, currentPassword: e.target.value })}
              />
              <Input
                type="password"
                required
                placeholder="新密碼"
                aria-label="新密碼"
                value={passwords.newPassword}
                onChange={(e) => setPasswords({ ...passwords, newPassword: e.target.value })}
              />
              <Button type="submit">更新密碼</Button>
            </form>
          </Card>

          {profile.stats && (
            <Card className="p-6">
              <h2 className="font-semibold">會員狀態</h2>
              <dl className="mt-3 grid grid-cols-2 gap-2 text-sm">
                <dt className="text-stone-500">等級</dt>
                <dd>{profile.stats.tier}</dd>
                <dt className="text-stone-500">登入次數</dt>
                <dd>{profile.stats.loginCount}</dd>
                <dt className="text-stone-500">購買次數</dt>
                <dd>{profile.stats.purchaseCount}</dd>
              </dl>
            </Card>
          )}
        </div>
      </div>

      <section>
        <h2 className="mb-3 text-xl font-semibold">我的收藏</h2>
        {favourites.length === 0 ? (
          <p className="text-sm text-stone-500">還沒有收藏任何餐廳。</p>
        ) : (
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {favourites.map((restaurant) => (
              <Link key={restaurant.id} href={`/restaurants/${restaurant.id}`}>
                <Card className="h-full p-4 transition hover:shadow-md">
                  <h3 className="font-semibold">{restaurant.name}</h3>
                  <p className="mt-1 text-sm text-stone-500">{restaurant.address}</p>
                  <div className="mt-2 flex flex-wrap gap-1.5">
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
