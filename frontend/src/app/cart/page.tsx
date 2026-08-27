"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import {
  Alert,
  Button,
  ButtonLink,
  Card,
  Empty,
  ErrorNote,
  Field,
  Input,
  PageHeader,
  PageShell,
  Spinner,
  money,
} from "@/components/ui";
import { IconArrowRight, IconCart, IconClose } from "@/components/icons";
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

  const empty = !cart || cart.items.length === 0;

  return (
    <PageShell>
      <PageHeader
        kicker="Your basket"
        title="購物車"
        description={empty ? undefined : `${cart.items.length} 件商品`}
      />

      <ErrorNote error={error} />

      {empty ? (
        <Empty
          icon={<IconCart />}
          action={
            <ButtonLink href="/shop" icon={<IconArrowRight />}>
              去商城逛逛
            </ButtonLink>
          }
        >
          購物車還是空的。
        </Empty>
      ) : (
        <div className="mt-6 grid gap-8 lg:grid-cols-3">
          {/* ---------- Lines ---------- */}
          <ul className="space-y-4 lg:col-span-2">
            {cart.items.map((line) => (
              <Card key={line.productId} as="li" className="p-5">
                <div className="flex flex-col gap-4 sm:flex-row sm:items-center">
                  {/* basis-0 + grow lets this column take the leftover width but
                      never squeeze the controls; min-w keeps the name readable. */}
                  <div className="min-w-48 grow basis-0">
                    <Link
                      href={`/shop/${line.productId}`}
                      className="font-display text-[15px] font-bold text-ink underline-offset-2 hover:text-pepper-ink hover:underline"
                    >
                      {line.name}
                    </Link>
                    <p className="mt-1 text-sm tabular text-subtle">{money(line.unitPrice)}</p>
                  </div>

                  <div className="flex shrink-0 items-center gap-4">
                    <div>
                      <Input
                        aria-label={`${line.name} 數量`}
                        type="number"
                        inputMode="numeric"
                        min={0}
                        max={Math.max(line.availableStock, 1)}
                        value={line.quantity}
                        onChange={(e) => updateQuantity(line.productId, Number(e.target.value))}
                        className="w-20 tabular"
                      />
                    </div>
                    <span className="w-24 text-right font-semibold tabular text-ink">{money(line.lineTotal)}</span>
                    <button
                      onClick={() => updateQuantity(line.productId, 0)}
                      className="flex h-11 w-11 shrink-0 cursor-pointer items-center justify-center rounded-full text-lg text-subtle transition hover:bg-danger-tint hover:text-danger"
                      aria-label={`移除 ${line.name}`}
                    >
                      <IconClose />
                    </button>
                  </div>
                </div>

                {line.unavailable && (
                  <div className="mt-4">
                    <Alert tone="warn">
                      已下架或庫存不足（剩 {line.availableStock}），請調整數量後再結帳。
                    </Alert>
                  </div>
                )}
              </Card>
            ))}
          </ul>

          {/* ---------- Summary ---------- */}
          <Card className="h-fit p-6 lg:sticky lg:top-24">
            <h2 className="font-display text-lg font-bold text-ink">結帳</h2>

            <dl className="mt-4 space-y-2 border-b border-line pb-4 text-sm">
              <div className="flex justify-between">
                <dt className="text-subtle">小計</dt>
                <dd className="tabular text-ink">{money(cart.total)}</dd>
              </div>
            </dl>
            <p className="mt-4 flex items-baseline justify-between">
              <span className="font-semibold text-ink">總計</span>
              <span className="font-display text-2xl font-bold tabular text-pepper">{money(cart.total)}</span>
            </p>

            <form onSubmit={checkout} className="mt-6 space-y-4">
              <Field id="receiverName" label="收件人姓名" required>
                {(props) => (
                  <Input
                    {...props}
                    autoComplete="name"
                    value={receiver.receiverName}
                    onChange={(e) => setReceiver({ ...receiver, receiverName: e.target.value })}
                  />
                )}
              </Field>
              <Field id="receiverPhone" label="收件人手機" hint="例如 0912345678" required>
                {(props) => (
                  <Input
                    {...props}
                    type="tel"
                    inputMode="tel"
                    autoComplete="tel"
                    value={receiver.receiverPhone}
                    onChange={(e) => setReceiver({ ...receiver, receiverPhone: e.target.value })}
                  />
                )}
              </Field>
              <Field id="receiverAddress" label="收件地址" required>
                {(props) => (
                  <Input
                    {...props}
                    autoComplete="street-address"
                    value={receiver.receiverAddress}
                    onChange={(e) => setReceiver({ ...receiver, receiverAddress: e.target.value })}
                  />
                )}
              </Field>

              <Button
                type="submit"
                className="w-full"
                size="lg"
                loading={checkingOut}
                disabled={cart.hasUnavailableItems}
              >
                送出訂單
              </Button>

              {cart.hasUnavailableItems && (
                <Alert tone="danger">請先處理上方無法購買的商品，才能送出訂單。</Alert>
              )}
            </form>
          </Card>
        </div>
      )}
    </PageShell>
  );
}
