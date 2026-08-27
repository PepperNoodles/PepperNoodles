"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import {
  Button,
  ButtonLink,
  Card,
  Empty,
  ErrorNote,
  Field,
  FilterChip,
  Gate,
  Input,
  PageHeader,
  PageShell,
  SectionHeader,
  Spinner,
  Stars,
  SuccessNote,
  TagPill,
} from "@/components/ui";
import { IconHeart, IconMapPin, IconUpload, IconUser } from "@/components/icons";
import type { Page, RestaurantSummary, Tag, UserProfile } from "@/lib/types";

const LABELS = { realName: "姓名", nickname: "暱稱", phone: "手機", location: "居住地區" } as const;

export default function ProfilePage() {
  const { user, loading: authLoading, refreshUser } = useAuth();
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [tags, setTags] = useState<Tag[]>([]);
  const [favourites, setFavourites] = useState<RestaurantSummary[]>([]);
  const [error, setError] = useState<unknown>(null);
  const [saved, setSaved] = useState(false);
  const [passwordChanged, setPasswordChanged] = useState(false);
  const [form, setForm] = useState({ realName: "", nickname: "", phone: "", location: "" });
  const [selectedTags, setSelectedTags] = useState<number[]>([]);
  const [passwords, setPasswords] = useState({ currentPassword: "", newPassword: "" });
  const [uploading, setUploading] = useState(false);
  const [savingProfile, setSavingProfile] = useState(false);
  const [savingPassword, setSavingPassword] = useState(false);

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
      <Gate title="請先登入" action={<ButtonLink href="/login?next=/profile">前往登入</ButtonLink>}>
        登入後才能查看與編輯個人資料。
      </Gate>
    );
  }
  if (!profile) return <Spinner />;

  async function saveProfile(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setSaved(false);
    setSavingProfile(true);
    try {
      await api.put("/users/me", { ...form, foodTagIds: selectedTags });
      await refreshUser();
      setSaved(true);
    } catch (e) {
      setError(e);
    } finally {
      setSavingProfile(false);
    }
  }

  async function uploadAvatar(file: File) {
    setError(null);
    setUploading(true);
    try {
      const body = new FormData();
      body.append("file", file);
      const { avatarUrl } = await api.post<{ avatarUrl: string }>("/users/me/avatar", body);
      setProfile((current) => (current ? { ...current, avatarUrl } : current));
      // The header shows the avatar too, so refresh the session copy.
      await refreshUser();
    } catch (e) {
      setError(e);
    } finally {
      setUploading(false);
    }
  }

  async function changePassword(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setPasswordChanged(false);
    setSavingPassword(true);
    try {
      await api.put("/users/me/password", passwords);
      setPasswords({ currentPassword: "", newPassword: "" });
      // Was a window.alert(), which blocks the page and cannot be styled.
      setPasswordChanged(true);
    } catch (e) {
      setError(e);
    } finally {
      setSavingPassword(false);
    }
  }

  return (
    <PageShell>
      <PageHeader kicker="Your account" title="個人資料" />

      <div className="space-y-5">
        <ErrorNote error={error} />
        {saved && <SuccessNote>個人資料已儲存。</SuccessNote>}
        {passwordChanged && <SuccessNote>密碼已更新，其他裝置的登入已全部登出。</SuccessNote>}
      </div>

      {/* ---------- Avatar ---------- */}
      <Card className="mt-6 p-6">
        <h2 className="font-display text-base font-bold text-ink">大頭貼</h2>
        <div className="mt-5 flex flex-wrap items-center gap-6">
          {profile.avatarUrl ? (
            /* eslint-disable-next-line @next/next/no-img-element */
            <img
              src={profile.avatarUrl}
              alt="目前的大頭貼"
              className="h-24 w-24 shrink-0 rounded-full object-cover ring-1 ring-line"
            />
          ) : (
            <span
              aria-hidden
              className="flex h-24 w-24 shrink-0 items-center justify-center rounded-full bg-mist text-4xl text-line-strong ring-1 ring-line"
            >
              <IconUser />
            </span>
          )}
          <div>
            {/*
              A bare <input type=file> renders as an unstyled OS button that
              matches nothing else on the page. The input stays (it is the real
              control, and keyboard-focusable through the label) but is visually
              replaced by the label.
            */}
            <label className="inline-flex min-h-11 cursor-pointer items-center gap-2 rounded-full border border-line-strong bg-white px-5 font-display text-sm font-bold uppercase tracking-wide text-ink transition hover:border-ink hover:bg-mist has-[:focus-visible]:outline has-[:focus-visible]:outline-2 has-[:focus-visible]:outline-offset-2 has-[:focus-visible]:outline-pepper">
              <IconUpload aria-hidden className="text-base" />
              {uploading ? "上傳中…" : "選擇圖片"}
              <input
                type="file"
                accept="image/*"
                aria-label="上傳大頭貼"
                disabled={uploading}
                onChange={(e) => e.target.files?.[0] && uploadAvatar(e.target.files[0])}
                className="sr-only"
              />
            </label>
            <p className="mt-2.5 text-[13px] text-subtle">JPG 或 PNG，建議 400×400 以上。</p>
          </div>
        </div>
      </Card>

      <div className="mt-5 grid gap-5 lg:grid-cols-3">
        {/* ---------- Details ---------- */}
        <Card className="p-6 lg:col-span-2">
          <h2 className="font-display text-base font-bold text-ink">基本資料</h2>
          <form onSubmit={saveProfile} className="mt-5 space-y-5">
            <Field id="email" label="電子信箱" hint="信箱不能修改。">
              {(props) => <Input {...props} value={profile.email} disabled />}
            </Field>

            <div className="grid gap-5 sm:grid-cols-2">
              {(["realName", "nickname", "phone", "location"] as const).map((key) => (
                <Field key={key} id={key} label={LABELS[key]}>
                  {(props) => (
                    <Input
                      {...props}
                      type={key === "phone" ? "tel" : "text"}
                      value={form[key]}
                      onChange={(e) => setForm({ ...form, [key]: e.target.value })}
                    />
                  )}
                </Field>
              ))}
            </div>

            <fieldset>
              <legend className="mb-2.5 text-sm font-semibold text-ink">興趣標籤</legend>
              <div className="flex flex-wrap gap-2">
                {tags.map((tag) => {
                  const active = selectedTags.includes(tag.id);
                  return (
                    <FilterChip
                      key={tag.id}
                      active={active}
                      onClick={() =>
                        setSelectedTags(
                          active ? selectedTags.filter((id) => id !== tag.id) : [...selectedTags, tag.id],
                        )
                      }
                    >
                      {tag.name}
                    </FilterChip>
                  );
                })}
              </div>
            </fieldset>

            <Button type="submit" loading={savingProfile}>
              儲存
            </Button>
          </form>
        </Card>

        {/* ---------- Password + membership ---------- */}
        <div className="space-y-5">
          <Card className="p-6">
            <h2 className="font-display text-base font-bold text-ink">修改密碼</h2>
            <form onSubmit={changePassword} className="mt-5 space-y-4">
              <Field id="currentPassword" label="目前的密碼" required>
                {(props) => (
                  <Input
                    {...props}
                    type="password"
                    autoComplete="current-password"
                    value={passwords.currentPassword}
                    onChange={(e) => setPasswords({ ...passwords, currentPassword: e.target.value })}
                  />
                )}
              </Field>
              <Field
                id="newPassword"
                label="新密碼"
                hint="至少 8 碼，需包含英文字母與數字。"
                required
              >
                {(props) => (
                  <Input
                    {...props}
                    type="password"
                    autoComplete="new-password"
                    value={passwords.newPassword}
                    onChange={(e) => setPasswords({ ...passwords, newPassword: e.target.value })}
                  />
                )}
              </Field>
              <Button type="submit" loading={savingPassword} className="w-full">
                更新密碼
              </Button>
            </form>
          </Card>

          {profile.stats && (
            <Card className="p-6">
              <h2 className="font-display text-base font-bold text-ink">會員狀態</h2>
              <dl className="mt-4 space-y-2.5 text-sm">
                {[
                  ["等級", profile.stats.tier],
                  ["登入次數", profile.stats.loginCount],
                  ["購買次數", profile.stats.purchaseCount],
                ].map(([label, value]) => (
                  <div key={label} className="flex justify-between gap-3">
                    <dt className="text-subtle">{label}</dt>
                    <dd className="font-semibold tabular text-ink">{value}</dd>
                  </div>
                ))}
              </dl>
            </Card>
          )}
        </div>
      </div>

      {/* ---------- Favourites ---------- */}
      <section className="mt-14">
        <SectionHeader title="我的收藏" count={favourites.length} />
        {favourites.length === 0 ? (
          <Empty
            icon={<IconHeart />}
            action={<ButtonLink href="/restaurants" variant="ghost">去找餐廳</ButtonLink>}
          >
            還沒有收藏任何餐廳。看到喜歡的店，按下收藏就會出現在這裡。
          </Empty>
        ) : (
          <ul className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
            {favourites.map((restaurant) => (
              <li key={restaurant.id} className="flex">
                <Link href={`/restaurants/${restaurant.id}`} className="group w-full">
                  <Card interactive className="h-full p-5">
                    <h3 className="font-display text-base font-bold text-ink transition group-hover:text-pepper-ink">
                      {restaurant.name}
                    </h3>
                    <p className="mt-1.5 flex items-start gap-1.5 text-sm text-subtle">
                      <IconMapPin aria-hidden className="mt-0.5 shrink-0 text-base" />
                      {restaurant.address}
                    </p>
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
              </li>
            ))}
          </ul>
        )}
      </section>
    </PageShell>
  );
}
