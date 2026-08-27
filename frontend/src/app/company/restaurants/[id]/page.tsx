"use client";

import { use, useCallback, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { api } from "@/lib/api";
import { RestaurantForm } from "@/components/RestaurantForm";
import {
  Badge,
  Button,
  ButtonLink,
  Card,
  ErrorNote,
  Field,
  Gate,
  ImageUploadField,
  Input,
  PageHeader,
  PageShell,
  SectionHeader,
  Spinner,
} from "@/components/ui";
import { IconClose, IconPlus, IconTrash, IconUpload } from "@/components/icons";
import type { RestaurantDetail, RestaurantEvent } from "@/lib/types";

export default function EditRestaurantPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const router = useRouter();
  const [restaurant, setRestaurant] = useState<RestaurantDetail | null>(null);
  const [events, setEvents] = useState<RestaurantEvent[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);
  const [uploadingPhoto, setUploadingPhoto] = useState(false);
  const [uploadingMenu, setUploadingMenu] = useState(false);
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
    setUploadingPhoto(true);
    await act(() => api.post(`/restaurants/${id}/photo`, body));
    setUploadingPhoto(false);
  }

  async function uploadMenu(file: File) {
    const body = new FormData();
    body.append("file", file);
    setUploadingMenu(true);
    await act(() => api.post(`/restaurants/${id}/menu`, body));
    setUploadingMenu(false);
  }

  async function createEvent(e: React.FormEvent) {
    e.preventDefault();
    await act(async () => {
      await api.post(`/restaurants/${id}/events`, newEvent);
      setNewEvent({ name: "", content: "", startsOn: "", endsOn: "" });
    });
  }

  if (loading) return <Spinner />;
  if (!restaurant) {
    return (
      <PageShell width="reading">
        <ErrorNote error={error} />
      </PageShell>
    );
  }

  if (!restaurant.editable) {
    return (
      <Gate title="沒有管理權限" action={<ButtonLink href="/company" variant="ghost">回到店家管理</ButtonLink>}>
        您沒有權限管理這間餐廳。
      </Gate>
    );
  }

  return (
    <PageShell width="reading">
      <PageHeader
        title={`管理「${restaurant.name}」`}
        back={{ href: "/company", label: "回到店家管理" }}
        actions={<ButtonLink href={`/restaurants/${id}`} variant="ghost">查看公開頁面</ButtonLink>}
      />

      <div className="space-y-12">
        <ErrorNote error={error} />

        <RestaurantForm existing={restaurant} />

        {/* ---------- Photo ---------- */}
        <Card className="p-6 sm:p-7">
          <h2 className="font-display text-base font-bold text-ink">餐廳照片</h2>
          <div className="mt-5">
            <ImageUploadField
              label="上傳照片"
              inputLabel="上傳餐廳照片"
              imageUrl={restaurant.photoUrl}
              emptyLabel="尚無照片"
              uploading={uploadingPhoto}
              onFile={uploadPhoto}
              hint="店面或招牌菜的橫幅照片，建議 1200×800 以上。"
            />
          </div>
        </Card>

        {/* ---------- Menu ---------- */}
        <Card className="p-6 sm:p-7">
          <SectionHeader title="菜單" count={restaurant.menu.length} />

          {restaurant.menu.length > 0 && (
            <ul className="mb-6 grid grid-cols-2 gap-4 sm:grid-cols-4">
              {restaurant.menu.map((item) => (
                <li key={item.id} className="group relative">
                  {/* eslint-disable-next-line @next/next/no-img-element */}
                  <img
                    src={item.imageUrl}
                    alt={item.caption ?? "菜單"}
                    loading="lazy"
                    className="aspect-4/3 w-full rounded-xl object-cover ring-1 ring-line"
                  />
                  {/*
                    Always present, not hover-only: a hover-revealed control is
                    unreachable on touch and invisible to a keyboard user.
                  */}
                  <button
                    onClick={() => act(() => api.delete(`/restaurants/${id}/menu/${item.id}`))}
                    className="absolute right-1.5 top-1.5 flex h-9 w-9 cursor-pointer items-center justify-center rounded-full bg-ink/70 text-white opacity-80 backdrop-blur-sm transition hover:bg-danger hover:opacity-100 focus-visible:opacity-100"
                    aria-label="刪除這張菜單"
                  >
                    <IconClose />
                  </button>
                </li>
              ))}
            </ul>
          )}

          <label className="inline-flex min-h-11 cursor-pointer items-center gap-2 rounded-full border border-line-strong bg-white px-5 font-display text-sm font-bold uppercase tracking-wide text-ink transition hover:border-ink hover:bg-mist has-[:focus-visible]:outline has-[:focus-visible]:outline-2 has-[:focus-visible]:outline-offset-2 has-[:focus-visible]:outline-pepper">
            <IconUpload aria-hidden className="text-base" />
            {uploadingMenu ? "上傳中…" : "新增菜單照片"}
            <input
              type="file"
              accept="image/*"
              aria-label="上傳菜單圖片"
              disabled={uploadingMenu}
              onChange={(e) => e.target.files?.[0] && uploadMenu(e.target.files[0])}
              className="sr-only"
            />
          </label>
        </Card>

        {/* ---------- Events ---------- */}
        <Card className="p-6 sm:p-7">
          <SectionHeader title="活動" count={events.length} />

          {events.length === 0 ? (
            <p className="text-sm text-subtle">還沒有任何活動。</p>
          ) : (
            <ul className="divide-y divide-line">
              {events.map((event) => (
                <li key={event.id} className="flex items-start justify-between gap-4 py-4 first:pt-0">
                  <div className="min-w-0">
                    <p className="flex flex-wrap items-center gap-2">
                      <span className="font-semibold text-ink">{event.name}</span>
                      {event.active && <Badge tone="success">進行中</Badge>}
                    </p>
                    {event.content && (
                      <p className="mt-1 text-sm leading-relaxed text-body">{event.content}</p>
                    )}
                    <p className="mt-1 text-xs tabular text-subtle">
                      {event.startsOn} ~ {event.endsOn}
                    </p>
                  </div>
                  <button
                    onClick={() => act(() => api.delete(`/restaurants/events/${event.id}`))}
                    className="flex h-10 w-10 shrink-0 cursor-pointer items-center justify-center rounded-full text-subtle transition hover:bg-danger-tint hover:text-danger"
                    aria-label={`刪除活動 ${event.name}`}
                  >
                    <IconTrash />
                  </button>
                </li>
              ))}
            </ul>
          )}

          <form onSubmit={createEvent} className="mt-6 space-y-5 border-t border-line pt-6">
            <h3 className="font-display text-sm font-bold uppercase tracking-wider text-ink">新增活動</h3>

            <Field id="event-name" label="活動名稱" required>
              {(props) => (
                <Input
                  {...props}
                  value={newEvent.name}
                  onChange={(e) => setNewEvent({ ...newEvent, name: e.target.value })}
                />
              )}
            </Field>

            <Field id="event-content" label="活動說明">
              {(props) => (
                <Input
                  {...props}
                  value={newEvent.content}
                  onChange={(e) => setNewEvent({ ...newEvent, content: e.target.value })}
                />
              )}
            </Field>

            <div className="grid gap-5 sm:grid-cols-2">
              <Field id="event-start" label="開始日期" required>
                {(props) => (
                  <Input
                    {...props}
                    type="date"
                    value={newEvent.startsOn}
                    onChange={(e) => setNewEvent({ ...newEvent, startsOn: e.target.value })}
                    className="tabular"
                  />
                )}
              </Field>
              <Field id="event-end" label="結束日期" required>
                {(props) => (
                  <Input
                    {...props}
                    type="date"
                    value={newEvent.endsOn}
                    onChange={(e) => setNewEvent({ ...newEvent, endsOn: e.target.value })}
                    className="tabular"
                  />
                )}
              </Field>
            </div>

            <Button type="submit" icon={<IconPlus />}>
              新增活動
            </Button>
          </form>
        </Card>

        {/* ---------- Danger zone ---------- */}
        <Card className="border-danger/30 p-6 sm:p-7">
          <h2 className="font-display text-base font-bold text-danger">刪除餐廳</h2>
          <p className="measure mt-2 text-sm leading-relaxed text-body">
            會一併刪除菜單、營業時間、活動與評論，且無法復原。
          </p>
          <Button
            variant="danger"
            className="mt-5"
            icon={<IconTrash />}
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
    </PageShell>
  );
}
