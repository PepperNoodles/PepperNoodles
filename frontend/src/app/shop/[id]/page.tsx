"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { use, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import {
  Alert,
  Badge,
  Button,
  Card,
  ErrorNote,
  Input,
  PageShell,
  Spinner,
  TagPill,
  TextLink,
  money,
} from "@/components/ui";
import { IconArrowLeft, IconBowl, IconCart, IconStore } from "@/components/icons";
import type { ProductDetail } from "@/lib/types";

export default function ProductPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const { user } = useAuth();
  const router = useRouter();
  const [product, setProduct] = useState<ProductDetail | null>(null);
  const [quantity, setQuantity] = useState(1);
  const [error, setError] = useState<unknown>(null);
  const [loading, setLoading] = useState(true);
  const [adding, setAdding] = useState(false);

  useEffect(() => {
    api
      .get<ProductDetail>(`/shop/products/${id}`)
      .then(setProduct)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [id]);

  async function addToCart() {
    setError(null);
    setAdding(true);
    try {
      await api.put("/cart/items", { productId: Number(id), quantity });
      router.push("/cart");
    } catch (e) {
      setError(e);
      setAdding(false);
    }
  }

  if (loading) return <Spinner />;
  if (!product) {
    return (
      <PageShell width="reading">
        <ErrorNote error={error ?? new Error("找不到這件商品。")} />
      </PageShell>
    );
  }

  const soldOut = product.quantity === 0;

  return (
    <PageShell>
      <Link
        href="/shop"
        className="-ml-2 mb-4 inline-flex min-h-11 items-center gap-1.5 rounded-lg px-2 text-sm font-medium text-subtle transition hover:bg-mist hover:text-pepper-ink sm:-ml-0 sm:mb-6 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
      >
        <IconArrowLeft className="text-base" />
        回到商城
      </Link>

      <div className="grid gap-10 lg:grid-cols-2 lg:gap-14">
        <Card className="flex aspect-square items-center justify-center overflow-hidden bg-mist p-0">
          {product.imageUrl ? (
            // eslint-disable-next-line @next/next/no-img-element
            <img src={product.imageUrl} alt={product.name} className="h-full w-full object-cover" />
          ) : (
            <IconBowl aria-hidden className="text-7xl text-line-strong" />
          )}
        </Card>

        <div className="flex flex-col">
          <Link
            href={`/restaurants/${product.restaurantId}`}
            className="-mx-2 inline-flex min-h-11 items-center gap-1.5 self-start rounded-lg px-2 text-sm font-medium text-pepper-ink transition hover:bg-pepper-tint sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-2"
          >
            <IconStore aria-hidden className="text-base" />
            {product.restaurantName}
          </Link>

          <h1 className="mt-3 font-display text-3xl font-bold leading-tight tracking-tight text-ink sm:text-4xl">
            {product.name}
          </h1>

          <p className="mt-4 font-display text-3xl font-bold tabular text-pepper">{money(product.price)}</p>

          <div className="mt-3">
            {soldOut ? (
              <Badge tone="danger">目前已售完</Badge>
            ) : (
              <Badge tone="success">庫存 {product.quantity} 件</Badge>
            )}
          </div>

          {product.description && (
            <p className="measure mt-6 whitespace-pre-wrap text-[15px] leading-relaxed text-body">
              {product.description}
            </p>
          )}

          {product.tags.length > 0 && (
            <div className="mt-6 flex flex-wrap gap-1.5">
              {product.tags.map((tag) => (
                <TagPill key={tag.id}>{tag.name}</TagPill>
              ))}
            </div>
          )}

          <div className="mt-8 border-t border-line pt-8">
            <ErrorNote error={error} />

            {user ? (
              <div className="mt-4 flex flex-wrap items-end gap-4">
                <div>
                  <label htmlFor="qty" className="mb-1.5 block text-sm font-semibold text-ink">
                    數量
                  </label>
                  <Input
                    id="qty"
                    type="number"
                    inputMode="numeric"
                    min={1}
                    max={Math.max(product.quantity, 1)}
                    value={quantity}
                    onChange={(e) => setQuantity(Number(e.target.value))}
                    disabled={soldOut}
                    className="w-24 tabular"
                  />
                </div>
                <Button onClick={addToCart} disabled={soldOut} loading={adding} size="lg" icon={<IconCart />}>
                  加入購物車
                </Button>
              </div>
            ) : (
              <Alert tone="info">
                <TextLink href={`/login?next=/shop/${id}`}>登入</TextLink> 後即可購買。
              </Alert>
            )}
          </div>
        </div>
      </div>
    </PageShell>
  );
}
