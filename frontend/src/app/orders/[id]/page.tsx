"use client";

import { use, useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { EcpayButton } from "@/components/EcpayButton";
import {
  Alert,
  Badge,
  Button,
  Card,
  ErrorNote,
  PageHeader,
  PageShell,
  Spinner,
  money,
} from "@/components/ui";
import { ORDER_STATUS } from "@/lib/orderStatus";
import type { Order } from "@/lib/types";

export default function OrderDetailPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const [order, setOrder] = useState<Order | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);

  const load = useCallback(() => {
    api
      .get<Order>(`/orders/${id}`)
      .then(setOrder)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [id]);

  useEffect(load, [load]);

  // ECPay sends the buyer back here after payment, but its server-to-server
  // callback may land a moment later — so a pending order is re-checked briefly
  // rather than showing a stale 待付款.
  useEffect(() => {
    if (order?.status !== "PENDING") return;
    let tries = 0;
    const timer = setInterval(() => {
      if (++tries > 5) {
        clearInterval(timer);
        return;
      }
      api
        .get<Order>(`/orders/${id}`)
        .then((fresh) => {
          if (fresh.status !== "PENDING") {
            setOrder(fresh);
            clearInterval(timer);
          }
        })
        .catch(() => clearInterval(timer));
    }, 3000);
    return () => clearInterval(timer);
  }, [order?.status, id]);

  async function cancel() {
    setError(null);
    try {
      await api.post(`/orders/${id}/cancel`);
      load();
    } catch (e) {
      setError(e);
    }
  }

  if (loading) return <Spinner />;
  if (!order) {
    return (
      <PageShell width="reading">
        <ErrorNote error={error} />
      </PageShell>
    );
  }

  const status = ORDER_STATUS[order.status];

  return (
    <PageShell width="reading">
      <PageHeader
        title={order.orderNo}
        description={`成立於 ${new Date(order.createdAt).toLocaleString("zh-TW")}`}
        back={{ href: "/orders", label: "回到訂單列表" }}
        actions={<Badge tone={status.tone}>{status.label}</Badge>}
      />

      <div className="space-y-5">
        <ErrorNote error={error} />

        {order.status === "PENDING" && (
          <Card className="border-warn/25 bg-warn-tint p-6">
            <Alert tone="warn">
              這筆訂單尚未付款，保留至{" "}
              <strong className="font-semibold">
                {new Date(order.expiresAt).toLocaleString("zh-TW")}
              </strong>
              。逾時後庫存會自動釋出。
            </Alert>
            <div className="mt-5 flex flex-wrap items-center gap-3">
              <EcpayButton orderId={order.id} />
              <Button variant="ghost" onClick={cancel}>
                取消訂單
              </Button>
            </div>
          </Card>
        )}

        {order.status === "PAID" && (
          <Alert tone="success" title="付款完成">
            {order.paidAt ? new Date(order.paidAt).toLocaleString("zh-TW") : "—"} · 感謝您的訂購！
          </Alert>
        )}

        <Card className="p-6 sm:p-7">
          <h2 className="font-display text-base font-bold text-ink">訂購商品</h2>
          <ul className="mt-5 divide-y divide-line">
            {order.items.map((item, index) => (
              <li key={index} className="flex justify-between gap-4 py-3 text-sm first:pt-0">
                <span className="min-w-0 text-body">
                  {item.productName}
                  <span className="ml-2 tabular text-subtle">× {item.quantity}</span>
                </span>
                <span className="shrink-0 font-medium tabular text-ink">{money(item.lineTotal)}</span>
              </li>
            ))}
          </ul>
          <p className="mt-5 flex items-baseline justify-between border-t-2 border-line pt-5">
            <span className="font-semibold text-ink">總計</span>
            <span className="font-display text-2xl font-bold tabular text-pepper">
              {money(order.totalCost)}
            </span>
          </p>
        </Card>

        <Card className="p-6 sm:p-7">
          <h2 className="font-display text-base font-bold text-ink">收件資訊</h2>
          <dl className="mt-5 space-y-3 text-sm">
            {[
              ["收件人", order.receiverName],
              ["電話", order.receiverPhone],
              ["地址", order.receiverAddress],
            ].map(([label, value]) => (
              <div key={label} className="flex gap-4">
                <dt className="w-20 shrink-0 text-subtle">{label}</dt>
                <dd className="text-ink">{value}</dd>
              </div>
            ))}
          </dl>
        </Card>
      </div>
    </PageShell>
  );
}
