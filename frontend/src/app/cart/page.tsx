"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { Button, Card, Empty, ErrorNote, Input, Spinner, money } from "@/components/ui";
import type { Cart, Order } from "@/lib/types";

export default function CartPage() {
  const router = useRouter();
  const [cart, setCart] = useState<Cart | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);
  const [checkingOut, setCheckingOut] = useState(false);
  const [receiver, setReceiver] = useState({ receiverName: "", receiverPhone: "", receiverAddress: "" });

  const load = useCallback(() => {
    api
      .get<Cart>("/cart")
      .then(setCart)
      .catch(setError)
      .finally(() => setLoading(false));
  }, []);

  useEffect(load, [load]);

  async function updateQuantity(productId: number, quantity: number) {
    setError(null);
    try {
      if (quantity <= 0) {
        await api.delete(`/cart/items/${productId}`);
      } else {
        await api.put("/cart/items", { productId, quantity });
      }
      load();
    } catch (e) {
      setError(e);
    }
  }

  async function checkout(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setCheckingOut(true);
    try {
      const order = await api.post<Order>("/orders/checkout", receiver);
      router.push(`/orders/${order.id}`);
    } catch (e) {
      setError(e);
      load(); // stock may have changed underneath us
    } finally {
      setCheckingOut(false);
    }
  }

  if (loading) return <Spinner />;

  return (
    <div className="mx-auto max-w-7xl space-y-6 px-6 py-10">
      <h1 className="text-2xl font-bold">購物車</h1>
      <ErrorNote error={error} />

      {!cart || cart.items.length === 0 ? (
        <Empty>
          購物車是空的。{" "}
          <Link href="/shop" className="text-red-600 hover:underline">
            去逛逛
          </Link>
        </Empty>
      ) : (
        <div className="grid gap-6 lg:grid-cols-3">
          <div className="space-y-3 lg:col-span-2">
            {cart.items.map((line) => (
              <Card key={line.productId} className="p-4">
                <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
                  {/* basis-0 + grow lets this column take the leftover width but
                      never squeeze the controls; min-w keeps the name readable. */}
                  <div className="min-w-48 grow basis-0">
                    <Link href={`/shop/${line.productId}`} className="font-medium hover:underline">
                      {line.name}
                    </Link>
                    <p className="text-sm text-stone-500">{money(line.unitPrice)}</p>
                  </div>

                  <div className="flex shrink-0 items-center gap-3">
                    <Input
                      type="number"
                      min={0}
                      max={Math.max(line.availableStock, 1)}
                      value={line.quantity}
                      onChange={(e) => updateQuantity(line.productId, Number(e.target.value))}
                      className="w-20"
                      aria-label={`${line.name} 數量`}
                    />
                    <span className="w-24 text-right font-semibold">{money(line.lineTotal)}</span>
                    <button
                      onClick={() => updateQuantity(line.productId, 0)}
                      className="px-1 text-stone-400 hover:text-red-600"
                      aria-label={`移除 ${line.name}`}
                    >
                      ✕
                    </button>
                  </div>
                </div>

                {line.unavailable && (
                  <p className="mt-2 text-xs text-red-600">
                    已下架或庫存不足（剩 {line.availableStock}），請調整後再結帳。
                  </p>
                )}
              </Card>
            ))}
          </div>

          <Card className="h-fit p-5">
            <h2 className="font-semibold">結帳</h2>
            <p className="mt-2 flex justify-between text-lg">
              <span>總計</span>
              <span className="font-bold text-red-600">{money(cart.total)}</span>
            </p>

            <form onSubmit={checkout} className="mt-4 space-y-3">
              <Input
                required
                placeholder="收件人姓名"
                aria-label="收件人姓名"
                value={receiver.receiverName}
                onChange={(e) => setReceiver({ ...receiver, receiverName: e.target.value })}
              />
              <Input
                required
                placeholder="收件人手機 (09xxxxxxxx)"
                aria-label="收件人手機"
                value={receiver.receiverPhone}
                onChange={(e) => setReceiver({ ...receiver, receiverPhone: e.target.value })}
              />
              <Input
                required
                placeholder="收件地址"
                aria-label="收件地址"
                value={receiver.receiverAddress}
                onChange={(e) => setReceiver({ ...receiver, receiverAddress: e.target.value })}
              />
              <Button type="submit" className="w-full" disabled={checkingOut || cart.hasUnavailableItems}>
                {checkingOut ? "處理中…" : "送出訂單"}
              </Button>
              {cart.hasUnavailableItems && (
                <p className="text-xs text-red-600">請先處理無法購買的商品。</p>
              )}
            </form>
          </Card>
        </div>
      )}
    </div>
  );
}
