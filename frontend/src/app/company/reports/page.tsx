"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api, query } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, Empty, ErrorNote, Input, Spinner, money } from "@/components/ui";

interface SalesReport {
  daily: { date: string; orderCount: number; revenue: string }[];
  monthly: { month: string; orderCount: number; revenue: string }[];
  topProducts: { productId: number; productName: string; unitsSold: number; revenue: string }[];
}

/**
 * Monthly revenue bars.
 *
 * <p>Drawn as inline SVG rather than pulling in Chart.js (which the 2021 version
 * used) — one bar chart does not justify the dependency, and this scales with
 * the container and stays readable without JavaScript measuring anything.
 */
function RevenueChart({ points }: { points: SalesReport["monthly"] }) {
  if (points.length === 0) return <Empty>這段期間沒有已付款的訂單。</Empty>;

  const max = Math.max(...points.map((p) => Number(p.revenue)), 1);
  // Cap the slot width so a single month renders as a bar rather than a slab,
  // and centre the group when there are only a few points.
  const slot = Math.min(100 / points.length, 14);
  const left = (100 - slot * points.length) / 2;

  return (
    <figure>
      <svg viewBox="0 0 100 44" className="h-56 w-full" role="img" aria-label="每月營收長條圖">
        {[0.25, 0.5, 0.75, 1].map((f) => (
          <line
            key={f}
            x1="0"
            x2="100"
            y1={36 - 36 * f}
            y2={36 - 36 * f}
            stroke="currentColor"
            strokeWidth="0.15"
            className="text-stone-200"
          />
        ))}
        {points.map((point, i) => {
          const height = (Number(point.revenue) / max) * 36;
          return (
            <g key={point.month}>
              <rect
                x={left + i * slot + slot * 0.2}
                y={36 - height}
                width={slot * 0.6}
                height={height}
                className="fill-pepper"
                rx="0.4"
              >
                <title>{`${point.month}：${money(point.revenue)}（${point.orderCount} 筆）`}</title>
              </rect>
              <text
                x={left + i * slot + slot / 2}
                y="41"
                textAnchor="middle"
                className="fill-stone-500"
                style={{ fontSize: 2.6 }}
              >
                {point.month.slice(5)}
              </text>
            </g>
          );
        })}
      </svg>
      <figcaption className="mt-1 text-center text-xs text-stone-400">
        最高 {money(max)}／月
      </figcaption>
    </figure>
  );
}

/** Defaults the report to the last year, evaluated once per mount. */
function defaultRange() {
  const iso = (ms: number) => new Date(ms).toISOString().slice(0, 10);
  return { from: iso(Date.now() - 365 * 86_400_000), to: iso(Date.now()) };
}

export default function ReportsPage() {
  const { hasRole, loading: authLoading } = useAuth();
  // Computed once via a lazy initialiser rather than on every render — reading
  // the clock during render makes the component impure.
  const [range, setRange] = useState(defaultRange);
  const [applied, setApplied] = useState(defaultRange);
  const [report, setReport] = useState<SalesReport | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);

  useEffect(() => {
    if (authLoading || !hasRole("ROLE_COMPANY", "ROLE_ADMIN")) {
      setLoading(false);
      return;
    }
    setLoading(true);
    api
      .get<SalesReport>(`/orders/reports/sales${query({ ...applied, topN: 10 })}`)
      .then(setReport)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [authLoading, hasRole, applied]);

  if (authLoading || loading) return <Spinner />;
  if (!hasRole("ROLE_COMPANY", "ROLE_ADMIN")) {
    return <p className="py-16 text-center text-sm text-stone-500">只有企業會員能查看報表。</p>;
  }

  const totalRevenue = report?.monthly.reduce((sum, m) => sum + Number(m.revenue), 0) ?? 0;
  const totalOrders = report?.monthly.reduce((sum, m) => sum + m.orderCount, 0) ?? 0;

  return (
    <div className="mx-auto max-w-5xl space-y-6 px-6 py-10">
      <div>
        <Link href="/company" className="text-sm text-stone-500 hover:text-pepper">
          ← 回到店家管理
        </Link>
        <h1 className="mt-2 text-2xl font-bold">銷售報表</h1>
      </div>

      <form
        onSubmit={(e) => {
          e.preventDefault();
          setApplied(range);
        }}
        className="flex flex-wrap items-end gap-3"
      >
        <div>
          <label htmlFor="from" className="mb-1 block text-xs text-stone-500">
            起始日
          </label>
          <Input
            id="from"
            type="date"
            value={range.from}
            onChange={(e) => setRange({ ...range, from: e.target.value })}
          />
        </div>
        <div>
          <label htmlFor="to" className="mb-1 block text-xs text-stone-500">
            結束日
          </label>
          <Input id="to" type="date" value={range.to} onChange={(e) => setRange({ ...range, to: e.target.value })} />
        </div>
        <Button type="submit">查詢</Button>
      </form>

      <ErrorNote error={error} />

      <div className="grid gap-4 sm:grid-cols-3">
        <Card className="p-5 text-center">
          <p className="text-2xl font-bold text-pepper">{money(totalRevenue)}</p>
          <p className="mt-1 text-xs text-stone-500">總營收</p>
        </Card>
        <Card className="p-5 text-center">
          <p className="text-2xl font-bold">{totalOrders}</p>
          <p className="mt-1 text-xs text-stone-500">已付款訂單</p>
        </Card>
        <Card className="p-5 text-center">
          <p className="text-2xl font-bold">
            {totalOrders > 0 ? money(totalRevenue / totalOrders) : money(0)}
          </p>
          <p className="mt-1 text-xs text-stone-500">平均客單價</p>
        </Card>
      </div>

      <Card className="p-6">
        <h2 className="mb-4 font-display text-lg font-bold">每月營收</h2>
        <RevenueChart points={report?.monthly ?? []} />
      </Card>

      <Card className="p-6">
        <h2 className="mb-4 font-display text-lg font-bold">熱銷商品</h2>
        {!report || report.topProducts.length === 0 ? (
          <Empty>這段期間沒有銷售紀錄。</Empty>
        ) : (
          <table className="w-full text-left text-sm">
            <thead className="border-b border-stone-200 text-xs uppercase text-stone-500">
              <tr>
                <th className="py-2">商品</th>
                <th className="py-2 text-right">售出</th>
                <th className="py-2 text-right">營收</th>
              </tr>
            </thead>
            <tbody>
              {report.topProducts.map((p) => (
                <tr key={p.productId} className="border-b border-stone-100 last:border-0">
                  <td className="py-2">
                    <Link href={`/shop/${p.productId}`} className="hover:text-pepper hover:underline">
                      {p.productName}
                    </Link>
                  </td>
                  <td className="py-2 text-right">{p.unitsSold}</td>
                  <td className="py-2 text-right font-medium">{money(p.revenue)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Card>
    </div>
  );
}
