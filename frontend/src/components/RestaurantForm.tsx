"use client";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { api, ApiError } from "@/lib/api";
import { Button, Card, ErrorNote, Field, Input, TagPicker } from "./ui";
import { IconClose, IconCrosshair, IconPlus } from "./icons";
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

  function field(name: keyof FormState, label: string, required = false, hint?: string, type = "text") {
    return (
      <Field id={name} label={label} required={required} hint={hint} error={fieldErrors[name]}>
        {(props) => (
          <Input
            {...props}
            type={type}
            value={form[name]}
            onChange={(e) => setForm({ ...form, [name]: e.target.value })}
          />
        )}
      </Field>
    );
  }

  return (
    <form onSubmit={onSubmit} className="space-y-5">
      <Card className="p-6 sm:p-7">
        <h2 className="font-display text-base font-bold text-ink">基本資料</h2>
        <div className="mt-5 space-y-5">
          {field("name", "餐廳名稱", true)}
          {field("address", "地址", true, "需與其他餐廳不同；系統以地址作為唯一識別。")}

          <div>
            <div className="flex flex-wrap items-end gap-3">
              <div className="min-w-32 flex-1">{field("latitude", "緯度", true)}</div>
              <div className="min-w-32 flex-1">{field("longitude", "經度", true)}</div>
              <Button
                type="button"
                variant="ghost"
                onClick={geocode}
                loading={geocoding}
                disabled={!form.address.trim()}
                icon={<IconCrosshair />}
              >
                從地址查座標
              </Button>
            </div>
            <p className="mt-2 text-[13px] text-subtle">
              座標決定餐廳在地圖上的位置。填好地址後按「從地址查座標」自動帶入。
            </p>
          </div>

          {field("contact", "聯絡電話", false, undefined, "tel")}
          {field("website", "官方網站", false, "含 https://", "url")}
        </div>
      </Card>

      <Card className="p-6 sm:p-7">
        <h2 className="mb-5 font-display text-base font-bold text-ink">食物標籤</h2>
        <TagPicker
          legend="選擇這間店的料理類型"
          tags={tags}
          selected={selectedTags}
          onChange={setSelectedTags}
        />
      </Card>

      <Card className="p-6 sm:p-7">
        <h2 className="font-display text-base font-bold text-ink">營業時間</h2>
        <p className="mt-1.5 text-[13px] text-subtle">
          同一天可以有多個時段（例如午晚餐分開）。沒有時段的日子視為公休。
        </p>

        <ul className="mt-5 divide-y divide-line">
          {DAYS.map((label, day) => {
            const dayHours = hours.map((h, i) => ({ ...h, index: i })).filter((h) => h.dayOfWeek === day);
            return (
              <li key={day} className="flex flex-wrap items-center gap-3 py-3.5">
                <span className="w-12 shrink-0 text-sm font-semibold text-ink">{label}</span>

                {dayHours.length === 0 && <span className="text-sm text-subtle">公休</span>}

                {dayHours.map((hour) => (
                  <span
                    key={hour.index}
                    className="flex items-center gap-1.5 rounded-xl border border-line bg-mist py-1 pl-2 pr-1"
                  >
                    <input
                      type="time"
                      value={hour.opensAt.slice(0, 5)}
                      onChange={(e) => updateHour(hour.index, { opensAt: e.target.value })}
                      aria-label={`${label} 開始時間`}
                      className="min-h-9 rounded-lg border border-line-strong bg-white px-2 text-sm tabular text-ink focus:border-pepper focus:outline-none focus:ring-2 focus:ring-pepper/20"
                    />
                    <span aria-hidden className="text-subtle">
                      –
                    </span>
                    <input
                      type="time"
                      value={hour.closesAt.slice(0, 5)}
                      onChange={(e) => updateHour(hour.index, { closesAt: e.target.value })}
                      aria-label={`${label} 結束時間`}
                      className="min-h-9 rounded-lg border border-line-strong bg-white px-2 text-sm tabular text-ink focus:border-pepper focus:outline-none focus:ring-2 focus:ring-pepper/20"
                    />
                    <button
                      type="button"
                      onClick={() => setHours(hours.filter((_, i) => i !== hour.index))}
                      className="flex h-9 w-9 cursor-pointer items-center justify-center rounded-lg text-subtle transition hover:bg-danger-tint hover:text-danger"
                      aria-label={`移除 ${label} 這個時段`}
                    >
                      <IconClose />
                    </button>
                  </span>
                ))}

                <button
                  type="button"
                  onClick={() => addHour(day)}
                  className="ml-auto inline-flex min-h-11 cursor-pointer items-center gap-1 rounded-full px-3 text-[13px] font-semibold text-pepper-ink transition hover:bg-pepper-tint sm:min-h-9"
                >
                  <IconPlus aria-hidden className="text-base" />
                  新增時段
                  <span className="sr-only">到{label}</span>
                </button>
              </li>
            );
          })}
        </ul>
      </Card>

      <ErrorNote error={error} />

      <div className="flex gap-3">
        <Button type="submit" loading={submitting} size="lg">
          {existing ? "儲存變更" : "建立餐廳"}
        </Button>
        <Button type="button" variant="ghost" size="lg" onClick={() => router.back()}>
          取消
        </Button>
      </div>
    </form>
  );
}
