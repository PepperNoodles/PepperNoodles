"use client";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { api, ApiError } from "@/lib/api";
import { Button, Card, ErrorNote, Input } from "./ui";
import type { Category, Page, ProductDetail, RestaurantSummary, Tag } from "@/lib/types";

export function ProductForm({ existing }: { existing?: ProductDetail }) {
  const router = useRouter();
  const [restaurants, setRestaurants] = useState<RestaurantSummary[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [tags, setTags] = useState<Tag[]>([]);
  const [selectedTags, setSelectedTags] = useState<number[]>(existing?.tags.map((t) => t.id) ?? []);
  const [error, setError] = useState<unknown>(null);
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({
    restaurantId: existing ? String(existing.restaurantId) : "",
    subcategoryId: "",
    name: existing?.name ?? "",
    description: existing?.description ?? "",
    price: existing?.price ?? "",
    quantity: existing ? String(existing.quantity) : "0",
    listed: existing ? existing.status === "LISTED" : true,
  });

  const fieldErrors = error instanceof ApiError ? error.fieldErrors : {};

  useEffect(() => {
    Promise.all([
      api.get<Page<RestaurantSummary>>("/users/me/restaurants?size=100").then((p) => p.content),
      api.get<Category[]>("/shop/categories", { anonymous: true }),
      api.get<Tag[]>("/food-tags", { anonymous: true }),
    ])
      .then(([r, c, t]) => {
        setRestaurants(r);
        setCategories(c);
        setTags(t);
        // Pre-select the only restaurant so a single-store owner never has to choose.
        if (!existing && r.length === 1) setForm((f) => ({ ...f, restaurantId: String(r[0].id) }));
      })
      .catch(setError);
  }, [existing]);

  async function onSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setSubmitting(true);

    const payload = {
      restaurantId: Number(form.restaurantId),
      subcategoryId: form.subcategoryId ? Number(form.subcategoryId) : null,
      name: form.name,
      description: form.description || null,
      price: form.price,
      quantity: Number(form.quantity),
      tagIds: selectedTags,
      listed: form.listed,
    };

    try {
      if (existing) {
        await api.put(`/shop/products/${existing.id}`, payload);
      } else {
        await api.post("/shop/products", payload);
      }
      router.push("/company");
      router.refresh();
    } catch (e) {
      setError(e);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form onSubmit={onSubmit} className="space-y-6">
      <Card className="space-y-4 p-6">
        <div>
          <label htmlFor="restaurantId" className="mb-1 block text-sm font-medium">
            所屬餐廳 <span className="text-pepper">*</span>
          </label>
          <select
            id="restaurantId"
            required
            value={form.restaurantId}
            onChange={(e) => setForm({ ...form, restaurantId: e.target.value })}
            className="w-full rounded-lg border border-stone-300 bg-white px-3 py-2 text-sm"
          >
            <option value="">請選擇…</option>
            {restaurants.map((r) => (
              <option key={r.id} value={r.id}>
                {r.name}
              </option>
            ))}
          </select>
          {restaurants.length === 0 && (
            <p className="mt-1 text-xs text-pepper">請先登錄一間餐廳，才能上架商品。</p>
          )}
        </div>

        <div>
          <label htmlFor="name" className="mb-1 block text-sm font-medium">
            商品名稱 <span className="text-pepper">*</span>
          </label>
          <Input
            id="name"
            required
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
          />
          {fieldErrors.name && <p className="mt-1 text-xs text-pepper">{fieldErrors.name}</p>}
        </div>

        <div>
          <label htmlFor="description" className="mb-1 block text-sm font-medium">
            商品說明
          </label>
          <textarea
            id="description"
            rows={3}
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
            className="w-full rounded-lg border border-stone-300 bg-white px-3 py-2 text-sm outline-none focus:border-pepper"
          />
        </div>

        <div className="flex gap-4">
          <div className="flex-1">
            <label htmlFor="price" className="mb-1 block text-sm font-medium">
              價格 <span className="text-pepper">*</span>
            </label>
            <Input
              id="price"
              type="number"
              min="0"
              step="1"
              required
              value={form.price}
              onChange={(e) => setForm({ ...form, price: e.target.value })}
            />
            {fieldErrors.price && <p className="mt-1 text-xs text-pepper">{fieldErrors.price}</p>}
          </div>
          <div className="flex-1">
            <label htmlFor="quantity" className="mb-1 block text-sm font-medium">
              庫存
            </label>
            <Input
              id="quantity"
              type="number"
              min="0"
              value={form.quantity}
              onChange={(e) => setForm({ ...form, quantity: e.target.value })}
            />
          </div>
        </div>

        <div>
          <label htmlFor="subcategoryId" className="mb-1 block text-sm font-medium">
            分類
          </label>
          <select
            id="subcategoryId"
            value={form.subcategoryId}
            onChange={(e) => setForm({ ...form, subcategoryId: e.target.value })}
            className="w-full rounded-lg border border-stone-300 bg-white px-3 py-2 text-sm"
          >
            <option value="">不分類</option>
            {categories.map((c) => (
              <optgroup key={c.id} label={c.name}>
                {c.subcategories.map((s) => (
                  <option key={s.id} value={s.id}>
                    {s.name}
                  </option>
                ))}
              </optgroup>
            ))}
          </select>
        </div>

        <div>
          <span className="mb-2 block text-sm font-medium">食物標籤</span>
          <div className="flex flex-wrap gap-2">
            {tags.map((tag) => {
              const active = selectedTags.includes(tag.id);
              return (
                <button
                  type="button"
                  key={tag.id}
                  aria-pressed={active}
                  onClick={() =>
                    setSelectedTags(active ? selectedTags.filter((i) => i !== tag.id) : [...selectedTags, tag.id])
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
        </div>

        <label className="flex items-center gap-2 text-sm">
          <input
            type="checkbox"
            checked={form.listed}
            onChange={(e) => setForm({ ...form, listed: e.target.checked })}
          />
          立即上架（未勾選則存為下架中）
        </label>
      </Card>

      <ErrorNote error={error} />

      <div className="flex gap-3">
        <Button type="submit" disabled={submitting || restaurants.length === 0}>
          {submitting ? "儲存中…" : existing ? "儲存變更" : "新增商品"}
        </Button>
        <Button type="button" variant="ghost" onClick={() => router.back()}>
          取消
        </Button>
      </div>
    </form>
  );
}
