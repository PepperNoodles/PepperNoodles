"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api, query } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import {
  Button,
  ButtonLink,
  Card,
  Empty,
  ErrorNote,
  Field,
  Gate,
  Input,
  PageHeader,
  PageShell,
  DataTable,
  Spinner,
  StatCard,
  money,
} from "@/components/ui";
import { IconChart } from "@/components/icons";

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
 *
 * <p>The bars are decorative: the same numbers are given as a real table below
 * the chart, so the figures are reachable without reading a graphic.
 */
function RevenueChart({ points }: { points: SalesReport["monthly"] }) {
  if (points.length === 0) return <Empty icon={<IconChart />}>這段期間沒有已付款的訂單。</Empty>;

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
            className="text-line"
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
                rx="0.5"
              >
                <title>{`${point.month}：${money(point.revenue)}（${point.orderCount} 筆）`}</title>
              </rect>
              <text
                x={left + i * slot + slot / 2}
                y="41"
                textAnchor="middle"
                className="fill-[var(--color-subtle)]"
                style={{ fontSize: 2.6 }}
              >
                {point.month.slice(5)}
              </text>
            </g>
          );
        })}
      </svg>
      <figcaption className="mt-2 text-center text-xs tabular text-subtle">
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
    return (
      <Gate title="僅限企業會員" action={<ButtonLink href="/register/company">註冊企業帳號</ButtonLink>}>
        只有企業會員能查看銷售報表。
      </Gate>
    );
  }

  const totalRevenue = report?.monthly.reduce((sum, m) => sum + Number(m.revenue), 0) ?? 0;
  const totalOrders = report?.monthly.reduce((sum, m) => sum + m.orderCount, 0) ?? 0;

  return (
    <PageShell width="full">
      <PageHeader
        kicker="Performance"
        title="銷售報表"
        back={{ href: "/company", label: "回到店家管理" }}
      />

      <div className="space-y-6">
        <form
          onSubmit={(e) => {
            e.preventDefault();
            setApplied(range);
          }}
          className="flex flex-wrap items-end gap-4"
        >
          <Field id="from" label="起始日">
            {(props) => (
              <Input
                {...props}
                type="date"
                value={range.from}
                onChange={(e) => setRange({ ...range, from: e.target.value })}
                className="tabular"
              />
            )}
          </Field>
          <Field id="to" label="結束日">
            {(props) => (
              <Input
                {...props}
                type="date"
                value={range.to}
                onChange={(e) => setRange({ ...range, to: e.target.value })}
                className="tabular"
              />
            )}
          </Field>
          <Button type="submit">查詢</Button>
        </form>

        <ErrorNote error={error} />

        <div className="grid gap-4 sm:grid-cols-3">
          <StatCard label="總營收" value={money(totalRevenue)} tone="brand" />
          <StatCard label="已付款訂單" value={totalOrders} />
          <StatCard
            label="平均客單價"
            value={totalOrders > 0 ? money(totalRevenue / totalOrders) : money(0)}
          />
        </div>

        <Card className="p-6 sm:p-7">
          <h2 className="mb-5 font-display text-base font-bold text-ink">每月營收</h2>
          <RevenueChart points={report?.monthly ?? []} />

          {report && report.monthly.length > 0 && (
            <details className="mt-6">
              <summary className="cursor-pointer text-[13px] font-semibold text-pepper-ink underline-offset-2 hover:underline">
                以表格檢視這些數字
              </summary>
              <div className="mt-4">
                <DataTable
                  caption="每月營收明細"
                  rows={report.monthly}
                  rowKey={(m) => m.month}
                  columns={[
                    { key: "month", header: "月份", primary: true, cell: (m) => <span className="tabular">{m.month}</span> },
                    { key: "orders", header: "訂單數", align: "right", cell: (m) => <span className="tabular">{m.orderCount}</span> },
                    {
                      key: "revenue",
                      header: "營收",
                      align: "right",
                      cell: (m) => <span className="tabular font-medium text-ink">{money(m.revenue)}</span>,
                    },
                  ]}
                />
              </div>
            </details>
          )}
        </Card>

        <Card className="p-6 sm:p-7">
          <h2 className="mb-5 font-display text-base font-bold text-ink">熱銷商品</h2>
          {!report || report.topProducts.length === 0 ? (
            <Empty icon={<IconChart />}>這段期間沒有銷售紀錄。</Empty>
          ) : (
            <DataTable
              caption="熱銷商品排行"
              rows={report.topProducts}
              rowKey={(p) => p.productId}
              columns={[
                {
                  key: "product",
                  header: "商品",
                  primary: true,
                  cell: (p) => (
                    <Link
                      href={`/shop/${p.productId}`}
                      className="font-medium text-ink underline-offset-2 hover:text-pepper-ink hover:underline"
                    >
                      {p.productName}
                    </Link>
                  ),
                },
                { key: "units", header: "售出", align: "right", cell: (p) => <span className="tabular">{p.unitsSold}</span> },
                {
                  key: "revenue",
                  header: "營收",
                  align: "right",
                  cell: (p) => <span className="tabular font-medium text-ink">{money(p.revenue)}</span>,
                },
              ]}
            />
          )}
        </Card>
      </div>
    </PageShell>
  );
}
