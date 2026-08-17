"use client";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { api, ApiError } from "@/lib/api";
import { Button, Card, ErrorNote, Input } from "./ui";
import type { BusinessHour, RestaurantDetail, Tag } from "@/lib/types";

const DAYS = ["週日", "週一", "週二", "週三", "週四", "週五", "週六"];

interface FormState {
  name: string;
  address: string;
  contact: string;
  website: string;
  latitude: string;
  longitude: string;
}

/**
 * Create/edit form for a restaurant.
 *
 * <p>Business hours travel with the restaurant payload rather than through
 * their own endpoints — the API replaces the whole set on save, which matches
 * how the week is edited here.
 */
export function RestaurantForm({ existing }: { existing?: RestaurantDetail }) {
  const router = useRouter();
  const [form, setForm] = useState<FormState>({
    name: existing?.name ?? "",
    address: existing?.address ?? "",
    contact: existing?.contact ?? "",
    website: existing?.website ?? "",
    latitude: existing?.latitude ?? "",
    longitude: existing?.longitude ?? "",
  });
  const [tags, setTags] = useState<Tag[]>([]);
  const [selectedTags, setSelectedTags] = useState<number[]>(existing?.tags.map((t) => t.id) ?? []);
  const [hours, setHours] = useState<BusinessHour[]>(existing?.businessHours ?? []);
  const [error, setError] = useState<unknown>(null);
  const [submitting, setSubmitting] = useState(false);
  const [geocoding, setGeocoding] = useState(false);

  const fieldErrors = error instanceof ApiError ? error.fieldErrors : {};

  useEffect(() => {
    api.get<Tag[]>("/food-tags", { anonymous: true }).then(setTags).catch(() => setTags([]));
  }, []);

  /** Address → coordinates via OSM Nominatim, so the owner need not know them. */
  async function geocode() {
    if (!form.address.trim()) return;
    setGeocoding(true);
    setError(null);
    try {
      const response = await fetch(
        `https://nominatim.openstreetmap.org/search?format=json&limit=1&q=${encodeURIComponent(form.address)}`,
      );
      const [hit] = (await response.json()) as { lat: string; lon: string }[];
      if (!hit) {
        setError(new Error("找不到這個地址的座標，請手動填入或換個寫法。"));
        return;
      }
      setForm((f) => ({ ...f, latitude: Number(hit.lat).toFixed(7), longitude: Number(hit.lon).toFixed(7) }));
    } catch {
      setError(new Error("座標查詢失敗，請手動填入。"));
    } finally {
      setGeocoding(false);
    }
  }

  function addHour(dayOfWeek: number) {
    setHours([...hours, { dayOfWeek, opensAt: "11:00", closesAt: "21:00" }]);
  }

  function updateHour(index: number, patch: Partial<BusinessHour>) {
    setHours(hours.map((h, i) => (i === index ? { ...h, ...patch } : h)));
  }

  async function onSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setSubmitting(true);

    const payload = {
      ...form,
      contact: form.contact || null,
      website: form.website || null,
      latitude: form.latitude,
      longitude: form.longitude,
      tagIds: selectedTags,
      // Seconds are required by the API's LocalTime parsing.
      businessHours: hours.map((h) => ({
        dayOfWeek: h.dayOfWeek,
        opensAt: h.opensAt.length === 5 ? `${h.opensAt}:00` : h.opensAt,
        closesAt: h.closesAt.length === 5 ? `${h.closesAt}:00` : h.closesAt,
      })),
    };

    try {
      if (existing) {
        await api.put(`/restaurants/${existing.id}`, payload);
        router.push(`/restaurants/${existing.id}`);
      } else {
        await api.post("/restaurants", payload);
        router.push("/company");
      }
      router.refresh();
    } catch (e) {
      setError(e);
    } finally {
      setSubmitting(false);
    }
  }

  function field(name: keyof FormState, label: string, required = false, hint?: string) {
    return (
      <div>
        <label htmlFor={name} className="mb-1 block text-sm font-medium">
          {label}
          {required && <span className="text-pepper"> *</span>}
        </label>
        <Input
          id={name}
          required={required}
          value={form[name]}
          onChange={(e) => setForm({ ...form, [name]: e.target.value })}
        />
        {hint && !fieldErrors[name] && <p className="mt-1 text-xs text-stone-500">{hint}</p>}
        {fieldErrors[name] && <p className="mt-1 text-xs text-pepper">{fieldErrors[name]}</p>}
      </div>
    );
  }

  return (
    <form onSubmit={onSubmit} className="space-y-6">
      <Card className="space-y-4 p-6">
        <h2 className="font-display text-lg font-bold">基本資料</h2>
        {field("name", "餐廳名稱", true)}
        {field("address", "地址", true, "需與其他餐廳不同；系統以地址作為唯一識別。")}

        <div className="flex items-end gap-3">
          <div className="flex-1">{field("latitude", "緯度", true)}</div>
          <div className="flex-1">{field("longitude", "經度", true)}</div>
          <Button type="button" variant="ghost" onClick={geocode} disabled={geocoding}>
            {geocoding ? "查詢中…" : "從地址查座標"}
          </Button>
        </div>

        {field("contact", "聯絡電話")}
        {field("website", "官方網站")}
      </Card>

      <Card className="p-6">
        <h2 className="font-display text-lg font-bold">食物標籤</h2>
        <div className="mt-3 flex flex-wrap gap-2">
          {tags.map((tag) => {
            const active = selectedTags.includes(tag.id);
            return (
              <button
                type="button"
                key={tag.id}
                aria-pressed={active}
                onClick={() =>
                  setSelectedTags(active ? selectedTags.filter((id) => id !== tag.id) : [...selectedTags, tag.id])
                }
                className={`rounded-full px-3 py-1 text-xs font-medium transition ${
                  active ? "bg-pepper text-white" : "bg-stone-100 text-stone-700 hover:bg-stone-200"
                }`}
              >
                {tag.name}
              </button>
            );
          })}
        </div>
      </Card>

      <Card className="p-6">
        <h2 className="font-display text-lg font-bold">營業時間</h2>
        <p className="mt-1 text-xs text-stone-500">同一天可以有多個時段（例如午晚餐分開）。沒有時段的日子視為公休。</p>
        <div className="mt-4 space-y-4">
          {DAYS.map((label, day) => {
            const dayHours = hours.map((h, i) => ({ ...h, index: i })).filter((h) => h.dayOfWeek === day);
            return (
              <div key={day} className="flex flex-wrap items-center gap-2 border-b border-stone-100 pb-3">
                <span className="w-12 shrink-0 text-sm font-medium">{label}</span>
                {dayHours.length === 0 && <span className="text-sm text-stone-400">公休</span>}
                {dayHours.map((hour) => (
                  <span key={hour.index} className="flex items-center gap-1">
                    <input
                      type="time"
                      value={hour.opensAt.slice(0, 5)}
                      onChange={(e) => updateHour(hour.index, { opensAt: e.target.value })}
                      aria-label={`${label} 開始時間`}
                      className="rounded border border-stone-300 px-2 py-1 text-sm"
                    />
                    <span className="text-stone-400">–</span>
                    <input
                      type="time"
                      value={hour.closesAt.slice(0, 5)}
                      onChange={(e) => updateHour(hour.index, { closesAt: e.target.value })}
                      aria-label={`${label} 結束時間`}
                      className="rounded border border-stone-300 px-2 py-1 text-sm"
                    />
                    <button
                      type="button"
                      onClick={() => setHours(hours.filter((_, i) => i !== hour.index))}
                      className="px-1 text-stone-400 hover:text-pepper"
                      aria-label={`移除 ${label} 這個時段`}
                    >
                      ✕
                    </button>
                  </span>
                ))}
                <button
                  type="button"
                  onClick={() => addHour(day)}
                  className="ml-auto text-xs text-pepper hover:underline"
                >
                  + 新增時段
                </button>
              </div>
            );
          })}
        </div>
      </Card>

      <ErrorNote error={error} />

      <div className="flex gap-3">
        <Button type="submit" disabled={submitting}>
          {submitting ? "儲存中…" : existing ? "儲存變更" : "建立餐廳"}
        </Button>
        <Button type="button" variant="ghost" onClick={() => router.back()}>
          取消
        </Button>
      </div>
    </form>
  );
}
