"use client";

import Link from "next/link";
import { use, useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { EcpayButton } from "@/components/EcpayButton";
import { Button, Card, ErrorNote, Spinner, money } from "@/components/ui";
import type { Order } from "@/lib/types";

const STATUS: Record<Order["status"], { label: string; className: string }> = {
  PENDING: { label: "待付款", className: "bg-amber-100 text-amber-800" },
  PAID: { label: "已付款", className: "bg-green-100 text-green-800" },
  CANCELLED: { label: "已取消", className: "bg-stone-200 text-stone-700" },
  EXPIRED: { label: "已逾期", className: "bg-stone-200 text-stone-700" },
};

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
  if (!order) return <div className="mx-auto max-w-3xl px-6 py-10"><ErrorNote error={error} /></div>;

  const status = STATUS[order.status];

  return (
    <div className="mx-auto max-w-3xl space-y-6 px-6 py-10">
      <Link href="/orders" className="text-sm text-stone-500 hover:text-pepper">
        ← 回到訂單列表
      </Link>

      <div className="flex flex-wrap items-start justify-between gap-3">
        <div>
          <h1 className="font-mono text-2xl font-bold">{order.orderNo}</h1>
          <p className="mt-1 text-sm text-stone-500">
            成立於 {new Date(order.createdAt).toLocaleString("zh-TW")}
          </p>
        </div>
        <span className={`rounded-full px-3 py-1 text-sm font-medium ${status.className}`}>
          {status.label}
        </span>
      </div>

      <ErrorNote error={error} />

      {order.status === "PENDING" && (
        <Card className="border-amber-300 bg-amber-50 p-5">
          <p className="text-sm text-amber-900">
            這筆訂單尚未付款，保留至{" "}
            <strong>{new Date(order.expiresAt).toLocaleString("zh-TW")}</strong>
            。逾時後庫存會自動釋出。
          </p>
          <div className="mt-4 flex items-center gap-3">
            <EcpayButton orderId={order.id} />
            <Button variant="ghost" onClick={cancel}>
              取消訂單
            </Button>
          </div>
        </Card>
      )}

      {order.status === "PAID" && (
        <Card className="border-green-300 bg-green-50 p-5">
          <p className="text-sm text-green-900">
            付款完成於 {order.paidAt ? new Date(order.paidAt).toLocaleString("zh-TW") : "—"}，感謝您的訂購！
          </p>
        </Card>
      )}

      <Card className="p-6">
        <h2 className="font-semibold">訂購商品</h2>
        <ul className="mt-4 space-y-2">
          {order.items.map((item, index) => (
            <li key={index} className="flex justify-between border-b border-stone-100 pb-2 text-sm last:border-0">
              <span>
                {item.productName}
                <span className="ml-2 text-stone-400">× {item.quantity}</span>
              </span>
              <span className="font-medium">{money(item.lineTotal)}</span>
            </li>
          ))}
        </ul>
        <p className="mt-4 flex justify-between border-t border-stone-200 pt-4 text-lg font-bold">
          <span>總計</span>
          <span className="text-pepper">{money(order.totalCost)}</span>
        </p>
      </Card>

      <Card className="p-6">
        <h2 className="font-semibold">收件資訊</h2>
        <dl className="mt-4 space-y-2 text-sm">
          <div className="flex gap-3">
            <dt className="w-20 shrink-0 text-stone-500">收件人</dt>
            <dd>{order.receiverName}</dd>
          </div>
          <div className="flex gap-3">
            <dt className="w-20 shrink-0 text-stone-500">電話</dt>
            <dd>{order.receiverPhone}</dd>
          </div>
          <div className="flex gap-3">
            <dt className="w-20 shrink-0 text-stone-500">地址</dt>
            <dd>{order.receiverAddress}</dd>
          </div>
        </dl>
      </Card>
    </div>
  );
}
