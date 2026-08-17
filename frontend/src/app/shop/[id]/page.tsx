"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { use, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, ErrorNote, Input, Spinner, TagPill, money } from "@/components/ui";
import type { ProductDetail } from "@/lib/types";

export default function ProductPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const { user } = useAuth();
  const router = useRouter();
  const [product, setProduct] = useState<ProductDetail | null>(null);
  const [quantity, setQuantity] = useState(1);
  const [error, setError] = useState<unknown>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api
      .get<ProductDetail>(`/shop/products/${id}`)
      .then(setProduct)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [id]);

  async function addToCart() {
    setError(null);
    try {
      await api.put("/cart/items", { productId: Number(id), quantity });
      router.push("/cart");
    } catch (e) {
      setError(e);
    }
  }

  if (loading) return <Spinner />;
  if (!product) return <ErrorNote error={error ?? new Error("找不到這件商品。")} />;

  return (
    <div className="mx-auto grid max-w-7xl gap-8 px-6 py-10 lg:grid-cols-2">
      <Card className="flex items-center justify-center overflow-hidden bg-stone-100 p-8 dark:bg-stone-900">
        {product.imageUrl ? (
          // eslint-disable-next-line @next/next/no-img-element
          <img src={product.imageUrl} alt={product.name} className="max-h-96 object-contain" />
        ) : (
          <span className="text-6xl" aria-hidden>
            🍜
          </span>
        )}
      </Card>

      <div>
        <Link href={`/restaurants/${product.restaurantId}`} className="text-sm text-red-600 hover:underline">
          {product.restaurantName}
        </Link>
        <h1 className="mt-1 text-3xl font-bold">{product.name}</h1>
        <p className="mt-3 text-3xl font-bold text-red-600">{money(product.price)}</p>

        {product.description && (
          <p className="mt-4 whitespace-pre-wrap text-sm text-stone-600 dark:text-stone-400">
            {product.description}
          </p>
        )}

        <div className="mt-4 flex flex-wrap gap-1.5">
          {product.tags.map((tag) => (
            <TagPill key={tag.id}>{tag.name}</TagPill>
          ))}
        </div>

        <p className="mt-4 text-sm text-stone-500">
          {product.quantity > 0 ? `庫存 ${product.quantity} 件` : "目前已售完"}
        </p>

        <ErrorNote error={error} />

        {user ? (
          <div className="mt-6 flex items-end gap-3">
            <div>
              <label htmlFor="qty" className="mb-1 block text-sm font-medium">
                數量
              </label>
              <Input
                id="qty"
                type="number"
                min={1}
                max={Math.max(product.quantity, 1)}
                value={quantity}
                onChange={(e) => setQuantity(Number(e.target.value))}
                className="w-24"
              />
            </div>
            <Button onClick={addToCart} disabled={product.quantity === 0}>
              加入購物車
            </Button>
          </div>
        ) : (
          <p className="mt-6 text-sm text-stone-500">
            <Link href="/login" className="text-red-600 hover:underline">
              登入
            </Link>{" "}
            後即可購買。
          </p>
        )}
      </div>
    </div>
  );
}
