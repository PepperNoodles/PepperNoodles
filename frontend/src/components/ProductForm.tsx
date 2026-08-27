"use client";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { api, ApiError } from "@/lib/api";
import { Alert, Button, Card, Checkbox, ErrorNote, Field, Input, Select, TagPicker, Textarea } from "./ui";
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
    <form onSubmit={onSubmit} className="space-y-5">
      {restaurants.length === 0 && (
        <Alert tone="warn" title="還沒有餐廳">
          請先登錄一間餐廳，才能上架商品。
        </Alert>
      )}

      <Card className="p-6 sm:p-7">
        <h2 className="font-display text-base font-bold text-ink">商品資料</h2>

        <div className="mt-5 space-y-5">
          <Field id="restaurantId" label="所屬餐廳" required>
            {(props) => (
              <Select
                {...props}
                value={form.restaurantId}
                onChange={(e) => setForm({ ...form, restaurantId: e.target.value })}
              >
                <option value="">請選擇…</option>
                {restaurants.map((r) => (
                  <option key={r.id} value={r.id}>
                    {r.name}
                  </option>
                ))}
              </Select>
            )}
          </Field>

          <Field id="name" label="商品名稱" required error={fieldErrors.name}>
            {(props) => (
              <Input {...props} value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
            )}
          </Field>

          <Field id="description" label="商品說明" hint="口味、份量、保存方式等。">
            {(props) => (
              <Textarea
                {...props}
                rows={4}
                value={form.description}
                onChange={(e) => setForm({ ...form, description: e.target.value })}
              />
            )}
          </Field>

          <div className="grid gap-5 sm:grid-cols-2">
            <Field id="price" label="價格" hint="新台幣，整數。" required error={fieldErrors.price}>
              {(props) => (
                <Input
                  {...props}
                  type="number"
                  inputMode="numeric"
                  min="0"
                  step="1"
                  value={form.price}
                  onChange={(e) => setForm({ ...form, price: e.target.value })}
                  className="tabular"
                />
              )}
            </Field>
            <Field id="quantity" label="庫存">
              {(props) => (
                <Input
                  {...props}
                  type="number"
                  inputMode="numeric"
                  min="0"
                  value={form.quantity}
                  onChange={(e) => setForm({ ...form, quantity: e.target.value })}
                  className="tabular"
                />
              )}
            </Field>
          </div>

          <Field id="subcategoryId" label="分類">
            {(props) => (
              <Select
                {...props}
                value={form.subcategoryId}
                onChange={(e) => setForm({ ...form, subcategoryId: e.target.value })}
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
              </Select>
            )}
          </Field>

          <TagPicker legend="食物標籤" tags={tags} selected={selectedTags} onChange={setSelectedTags} />

          <Checkbox
            id="listed"
            label="立即上架"
            hint="未勾選則存為下架中，只有你看得到。"
            checked={form.listed}
            onChange={(listed) => setForm({ ...form, listed })}
          />
        </div>
      </Card>

      <ErrorNote error={error} />

      <div className="flex gap-3">
        <Button type="submit" loading={submitting} disabled={restaurants.length === 0} size="lg">
          {existing ? "儲存變更" : "新增商品"}
        </Button>
        <Button type="button" variant="ghost" size="lg" onClick={() => router.back()}>
          取消
        </Button>
      </div>
    </form>
  );
}
