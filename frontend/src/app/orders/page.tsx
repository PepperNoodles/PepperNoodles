"use client";

import { Suspense, useCallback, useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { api } from "@/lib/api";
import { Button, Card, Empty, ErrorNote, Spinner, money } from "@/components/ui";
import type { Order, Page } from "@/lib/types";

const STATUS_LABELS: Record<Order["status"], string> = {
  PENDING: "待付款",
  PAID: "已付款",
  CANCELLED: "已取消",
  EXPIRED: "已逾期",
};

const STATUS_STYLES: Record<Order["status"], string> = {
  PENDING: "bg-amber-100 text-amber-800 dark:bg-amber-950 dark:text-amber-300",
  PAID: "bg-green-100 text-green-800 dark:bg-green-950 dark:text-green-300",
  CANCELLED: "bg-stone-200 text-stone-700 dark:bg-stone-800 dark:text-stone-300",
  EXPIRED: "bg-stone-200 text-stone-700 dark:bg-stone-800 dark:text-stone-300",
};

function OrderList() {
  const highlight = useSearchParams().get("highlight");
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);

  const load = useCallback(() => {
    api
      .get<Page<Order>>("/orders?size=20")
      .then((page) => setOrders(page.content))
      .catch(setError)
      .finally(() => setLoading(false));
  }, []);

  useEffect(load, [load]);

  async function cancel(id: number) {
    setError(null);
    try {
      await api.post(`/orders/${id}/cancel`);
      load();
    } catch (e) {
      setError(e);
    }
  }

  if (loading) return <Spinner />;

  return (
    <div className="mx-auto max-w-7xl space-y-6 px-6 py-10">
      <h1 className="text-2xl font-bold">我的訂單</h1>
      <ErrorNote error={error} />

      {orders.length === 0 ? (
        <Empty>還沒有任何訂單。</Empty>
      ) : (
        <ul className="space-y-4">
          {orders.map((order) => (
            <Card
              key={order.id}
              className={`p-5 ${order.orderNo === highlight ? "ring-2 ring-red-500" : ""}`}
            >
              <div className="flex flex-wrap items-start justify-between gap-3">
                <div>
                  <p className="font-mono text-sm font-semibold">{order.orderNo}</p>
                  <p className="text-xs text-stone-500">
                    {new Date(order.createdAt).toLocaleString("zh-TW")}
                  </p>
                </div>
                <span className={`rounded-full px-2.5 py-1 text-xs font-medium ${STATUS_STYLES[order.status]}`}>
                  {STATUS_LABELS[order.status]}
                </span>
              </div>

              <ul className="mt-3 space-y-1 text-sm">
                {order.items.map((item, index) => (
                  <li key={index} className="flex justify-between text-stone-600 dark:text-stone-400">
                    <span>
                      {item.productName} × {item.quantity}
                    </span>
                    <span>{money(item.lineTotal)}</span>
                  </li>
                ))}
              </ul>

              <div className="mt-3 flex items-center justify-between border-t border-stone-100 pt-3 dark:border-stone-800">
                <span className="font-semibold">{money(order.totalCost)}</span>
                {order.status === "PENDING" && (
                  <div className="flex items-center gap-3">
                    <span className="text-xs text-stone-500">
                      保留至 {new Date(order.expiresAt).toLocaleString("zh-TW")}
                    </span>
                    <Button variant="ghost" onClick={() => cancel(order.id)}>
                      取消訂單
                    </Button>
                  </div>
                )}
              </div>
            </Card>
          ))}
        </ul>
      )}
    </div>
  );
}

export default function OrdersPage() {
  return (
    <Suspense fallback={<Spinner />}>
      <OrderList />
    </Suspense>
  );
}
