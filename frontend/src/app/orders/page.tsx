"use client";

import { Suspense, useCallback, useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import { api } from "@/lib/api";
import Link from "next/link";
import {
  Badge,
  Button,
  ButtonLink,
  Card,
  Empty,
  ErrorNote,
  ListSkeleton,
  PageHeader,
  PageShell,
  money,
} from "@/components/ui";
import { IconReceipt } from "@/components/icons";
import { EcpayButton } from "@/components/EcpayButton";
import { ORDER_STATUS } from "@/lib/orderStatus";
import type { Order, Page } from "@/lib/types";

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

  return (
    <PageShell>
      <PageHeader kicker="Your orders" title="我的訂單" />

      <div className="space-y-5">
        <ErrorNote error={error} />

        {loading ? (
          <ListSkeleton rows={3} />
        ) : orders.length === 0 ? (
          <Empty icon={<IconReceipt />} action={<ButtonLink href="/shop">去商城逛逛</ButtonLink>}>
            還沒有任何訂單。
          </Empty>
        ) : (
          <ul className="space-y-5">
            {orders.map((order) => {
              const status = ORDER_STATUS[order.status];
              return (
                <Card
                  key={order.id}
                  as="li"
                  className={`p-6 ${
                    order.orderNo === highlight ? "ring-2 ring-pepper ring-offset-2" : ""
                  }`}
                >
                  <div className="flex flex-wrap items-start justify-between gap-4">
                    <div>
                      <Link
                        href={`/orders/${order.id}`}
                        className="-mx-2 inline-flex min-h-11 items-center rounded-lg px-2 font-mono text-sm font-bold tabular text-ink transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-2"
                      >
                        {order.orderNo}
                      </Link>
                      <p className="mt-1 text-xs tabular text-subtle">
                        <time dateTime={order.createdAt}>
                          {new Date(order.createdAt).toLocaleString("zh-TW")}
                        </time>
                      </p>
                    </div>
                    <Badge tone={status.tone}>{status.label}</Badge>
                  </div>

                  <ul className="mt-5 space-y-1.5 text-sm">
                    {order.items.map((item, index) => (
                      <li key={index} className="flex justify-between gap-4 text-body">
                        <span className="min-w-0">
                          {item.productName}
                          <span className="ml-2 tabular text-subtle">× {item.quantity}</span>
                        </span>
                        <span className="shrink-0 tabular">{money(item.lineTotal)}</span>
                      </li>
                    ))}
                  </ul>

                  <div className="mt-5 flex flex-wrap items-center justify-between gap-4 border-t border-line pt-4">
                    <span className="font-display text-lg font-bold tabular text-ink">
                      {money(order.totalCost)}
                    </span>
                    {order.status === "PENDING" && (
                      <div className="flex flex-wrap items-center gap-3">
                        <span className="text-xs tabular text-subtle">
                          保留至 {new Date(order.expiresAt).toLocaleString("zh-TW")}
                        </span>
                        <EcpayButton orderId={order.id} />
                        <Button variant="ghost" size="sm" onClick={() => cancel(order.id)}>
                          取消訂單
                        </Button>
                      </div>
                    )}
                  </div>
                </Card>
              );
            })}
          </ul>
        )}
      </div>
    </PageShell>
  );
}

export default function OrdersPage() {
  return (
    <Suspense fallback={<PageShell><ListSkeleton rows={3} /></PageShell>}>
      <OrderList />
    </Suspense>
  );
}
