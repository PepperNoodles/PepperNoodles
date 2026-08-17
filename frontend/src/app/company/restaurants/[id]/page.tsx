"use client";

import Link from "next/link";
import { use, useCallback, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { api } from "@/lib/api";
import { RestaurantForm } from "@/components/RestaurantForm";
import { Button, Card, ErrorNote, Input, Spinner } from "@/components/ui";
import type { RestaurantDetail, RestaurantEvent } from "@/lib/types";

export default function EditRestaurantPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const router = useRouter();
  const [restaurant, setRestaurant] = useState<RestaurantDetail | null>(null);
  const [events, setEvents] = useState<RestaurantEvent[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);
  const [newEvent, setNewEvent] = useState({ name: "", content: "", startsOn: "", endsOn: "" });

  const load = useCallback(() => {
    Promise.all([
      api.get<RestaurantDetail>(`/restaurants/${id}`),
      api.get<RestaurantEvent[]>(`/restaurants/${id}/events`),
    ])
      .then(([detail, eventList]) => {
        setRestaurant(detail);
        setEvents(eventList);
      })
      .catch(setError)
      .finally(() => setLoading(false));
  }, [id]);

  useEffect(load, [load]);

  async function act(fn: () => Promise<unknown>) {
    setError(null);
    try {
      await fn();
      load();
    } catch (e) {
      setError(e);
    }
  }

  async function uploadPhoto(file: File) {
    const body = new FormData();
    body.append("file", file);
    await act(() => api.post(`/restaurants/${id}/photo`, body));
  }

  async function uploadMenu(file: File) {
    const body = new FormData();
    body.append("file", file);
    await act(() => api.post(`/restaurants/${id}/menu`, body));
  }

  async function createEvent(e: React.FormEvent) {
    e.preventDefault();
    await act(async () => {
      await api.post(`/restaurants/${id}/events`, newEvent);
      setNewEvent({ name: "", content: "", startsOn: "", endsOn: "" });
    });
  }

  if (loading) return <Spinner />;
  if (!restaurant) return <div className="mx-auto max-w-3xl px-6 py-10"><ErrorNote error={error} /></div>;

  if (!restaurant.editable) {
    return (
      <p className="py-16 text-center text-sm text-stone-500">
        您沒有權限管理這間餐廳。
        <Link href="/company" className="ml-1 text-pepper hover:underline">
          回到店家管理
        </Link>
      </p>
    );
  }

  return (
    <div className="mx-auto max-w-3xl space-y-8 px-6 py-10">
      <div>
        <Link href="/company" className="text-sm text-stone-500 hover:text-pepper">
          ← 回到店家管理
        </Link>
        <h1 className="mt-2 text-2xl font-bold">管理「{restaurant.name}」</h1>
      </div>

      <ErrorNote error={error} />

      <RestaurantForm existing={restaurant} />

      {/* --- 照片 --- */}
      <Card className="p-6">
        <h2 className="font-display text-lg font-bold">餐廳照片</h2>
        <div className="mt-4 flex items-center gap-4">
          {restaurant.photoUrl ? (
            /* eslint-disable-next-line @next/next/no-img-element */
            <img src={restaurant.photoUrl} alt="" className="h-24 w-32 rounded object-cover" />
          ) : (
            <div className="flex h-24 w-32 items-center justify-center rounded bg-stone-100 text-stone-400">
              尚無照片
            </div>
          )}
          <input
            type="file"
            accept="image/*"
            aria-label="上傳餐廳照片"
            onChange={(e) => e.target.files?.[0] && uploadPhoto(e.target.files[0])}
            className="text-sm"
          />
        </div>
      </Card>

      {/* --- 菜單 --- */}
      <Card className="p-6">
        <h2 className="font-display text-lg font-bold">菜單 ({restaurant.menu.length})</h2>
        <div className="mt-4 grid grid-cols-2 gap-3 sm:grid-cols-4">
          {restaurant.menu.map((item) => (
            <div key={item.id} className="group relative">
              {/* eslint-disable-next-line @next/next/no-img-element */}
              <img src={item.imageUrl} alt={item.caption ?? ""} className="h-28 w-full rounded object-cover" />
              <button
                onClick={() => act(() => api.delete(`/restaurants/${id}/menu/${item.id}`))}
                className="absolute right-1 top-1 rounded-full bg-black/60 px-2 py-0.5 text-xs text-white opacity-0 transition group-hover:opacity-100"
                aria-label="刪除這張菜單"
              >
                ✕
              </button>
            </div>
          ))}
        </div>
        <input
          type="file"
          accept="image/*"
          aria-label="上傳菜單圖片"
          onChange={(e) => e.target.files?.[0] && uploadMenu(e.target.files[0])}
          className="mt-4 text-sm"
        />
      </Card>

      {/* --- 活動 --- */}
      <Card className="p-6">
        <h2 className="font-display text-lg font-bold">活動 ({events.length})</h2>

        <ul className="mt-4 space-y-2">
          {events.map((event) => (
            <li
              key={event.id}
              className="flex items-start justify-between gap-3 border-b border-stone-100 pb-2 last:border-0"
            >
              <div>
                <p className="font-medium">
                  {event.name}
                  {event.active && (
                    <span className="ml-2 rounded bg-green-100 px-1.5 py-0.5 text-[11px] text-green-700">
                      進行中
                    </span>
                  )}
                </p>
                <p className="text-sm text-stone-500">{event.content}</p>
                <p className="text-xs text-stone-400">
                  {event.startsOn} ~ {event.endsOn}
                </p>
              </div>
              <button
                onClick={() => act(() => api.delete(`/restaurants/events/${event.id}`))}
                className="text-xs text-stone-400 hover:text-pepper"
              >
                刪除
              </button>
            </li>
          ))}
          {events.length === 0 && <li className="text-sm text-stone-400">還沒有任何活動。</li>}
        </ul>

        <form onSubmit={createEvent} className="mt-6 space-y-3 border-t border-stone-200 pt-5">
          <h3 className="text-sm font-semibold">新增活動</h3>
          <Input
            required
            placeholder="活動名稱"
            aria-label="活動名稱"
            value={newEvent.name}
            onChange={(e) => setNewEvent({ ...newEvent, name: e.target.value })}
          />
          <Input
            placeholder="活動說明"
            aria-label="活動說明"
            value={newEvent.content}
            onChange={(e) => setNewEvent({ ...newEvent, content: e.target.value })}
          />
          <div className="flex gap-3">
            <Input
              required
              type="date"
              aria-label="開始日期"
              value={newEvent.startsOn}
              onChange={(e) => setNewEvent({ ...newEvent, startsOn: e.target.value })}
            />
            <Input
              required
              type="date"
              aria-label="結束日期"
              value={newEvent.endsOn}
              onChange={(e) => setNewEvent({ ...newEvent, endsOn: e.target.value })}
            />
          </div>
          <Button type="submit">新增活動</Button>
        </form>
      </Card>

      {/* --- 危險操作 --- */}
      <Card className="border-red-200 p-6">
        <h2 className="font-display text-lg font-bold text-pepper">刪除餐廳</h2>
        <p className="mt-1 text-sm text-stone-500">
          會一併刪除菜單、營業時間、活動與評論，且無法復原。
        </p>
        <Button
          variant="danger"
          className="mt-4"
          onClick={async () => {
            if (!confirm(`確定要刪除「${restaurant.name}」嗎？此操作無法復原。`)) return;
            await act(async () => {
              await api.delete(`/restaurants/${id}`);
              router.push("/company");
            });
          }}
        >
          刪除這間餐廳
        </Button>
      </Card>
    </div>
  );
}
