"use client";

import { useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import {
  Badge,
  Button,
  ButtonLink,
  Card,
  Empty,
  ErrorNote,
  Gate,
  PageHeader,
  PageShell,
  DataTable,
  SectionHeader,
  Spinner,
  StatCard,
} from "@/components/ui";
import { IconFile, IconMail, IconStore } from "@/components/icons";
import type { AdminDashboard, Inquiry, ManagedUser, Page } from "@/lib/types";

export default function AdminPage() {
  const { hasRole, loading: authLoading } = useAuth();
  const [dashboard, setDashboard] = useState<AdminDashboard | null>(null);
  const [users, setUsers] = useState<ManagedUser[]>([]);
  const [inquiries, setInquiries] = useState<Inquiry[]>([]);
  const [error, setError] = useState<unknown>(null);
  const [loading, setLoading] = useState(true);

  const load = useCallback(() => {
    Promise.all([
      api.get<AdminDashboard>("/admin/dashboard"),
      api.get<Page<ManagedUser>>("/admin/users?size=50"),
      api.get<Page<Inquiry>>("/admin/inquiries?status=OPEN&size=20"),
    ])
      .then(([d, u, i]) => {
        setDashboard(d);
        setUsers(u.content);
        setInquiries(i.content);
      })
      .catch(setError)
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    if (!authLoading && hasRole("ROLE_ADMIN")) load();
    else if (!authLoading) setLoading(false);
  }, [authLoading, hasRole, load]);

  async function act(fn: () => Promise<unknown>) {
    setError(null);
    try {
      await fn();
      load();
    } catch (e) {
      setError(e);
    }
  }

  if (authLoading || loading) return <Spinner />;
  if (!hasRole("ROLE_ADMIN")) {
    return (
      <Gate title="僅限管理員" action={<ButtonLink href="/" variant="ghost">回首頁</ButtonLink>}>
        這個頁面只有管理員能存取。
      </Gate>
    );
  }

  const stats: { label: string; value: number; tone?: "brand" }[] = dashboard
    ? [
        { label: "會員總數", value: dashboard.totalUsers },
        { label: "停權中", value: dashboard.suspendedUsers },
        { label: "餐廳", value: dashboard.totalRestaurants },
        { label: "商品", value: dashboard.totalProducts },
        { label: "待付款訂單", value: dashboard.pendingOrders },
        { label: "未處理訊息", value: dashboard.openInquiries, tone: "brand" },
      ]
    : [];

  return (
    <PageShell>
      <PageHeader
        kicker="Back office"
        title="後台"
        actions={
          <>
            <ButtonLink href="/admin/restaurants" variant="ghost" icon={<IconStore />}>
              餐廳管理
            </ButtonLink>
            <ButtonLink href="/admin/audit-log" variant="ghost" icon={<IconFile />}>
              操作紀錄
            </ButtonLink>
          </>
        }
      />

      <div className="space-y-12">
        <ErrorNote error={error} />

        <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 lg:grid-cols-6">
          {stats.map((stat) => (
            <StatCard key={stat.label} label={stat.label} value={stat.value} tone={stat.tone} />
          ))}
        </div>

        {/* ---------- Inquiries ---------- */}
        <section>
          <SectionHeader title="聯絡我們" count={inquiries.length} description="尚未處理的訪客訊息。" />
          {inquiries.length === 0 ? (
            <Empty icon={<IconMail />}>沒有待處理的訊息。</Empty>
          ) : (
            <ul className="space-y-3">
              {inquiries.map((inquiry) => (
                <Card key={inquiry.id} as="li" className="p-5">
                  <div className="flex flex-wrap items-start justify-between gap-4">
                    <div className="min-w-0 flex-1">
                      <p className="break-words text-sm font-semibold text-ink">
                        {inquiry.submitterName ?? inquiry.contactEmail ?? "訪客"}
                      </p>
                      <p className="measure mt-2 whitespace-pre-wrap text-sm leading-relaxed text-body">
                        {inquiry.body}
                      </p>
                      <p className="mt-2 text-xs tabular text-subtle">
                        <time dateTime={inquiry.createdAt}>
                          {new Date(inquiry.createdAt).toLocaleString("zh-TW")}
                        </time>
                      </p>
                    </div>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() =>
                        act(() =>
                          api.post(`/admin/inquiries/${inquiry.id}/resolve`, { resolutionNote: "已處理" }),
                        )
                      }
                    >
                      標記已處理
                    </Button>
                  </div>
                </Card>
              ))}
            </ul>
          )}
        </section>

        {/* ---------- Users ---------- */}
        <section>
          <SectionHeader title="會員管理" count={users.length} />
          <DataTable
            caption="會員列表"
            rows={users}
            rowKey={(u) => u.id}
            columns={[
              { key: "email", header: "信箱", primary: true, cell: (u) => u.email },
              { key: "name", header: "名稱", cell: (u) => u.displayName },
              {
                key: "roles",
                header: "角色",
                cell: (u) => (
                  <span className="text-xs text-subtle">
                    {u.roles.map((r) => r.replace("ROLE_", "")).join(", ")}
                  </span>
                ),
              },
              {
                key: "status",
                header: "狀態",
                cell: (u) =>
                  u.suspended ? (
                    <Badge tone="danger">停權</Badge>
                  ) : u.enabled ? (
                    <Badge tone="success">正常</Badge>
                  ) : (
                    <Badge>未驗證</Badge>
                  ),
              },
              {
                key: "actions",
                header: "操作",
                align: "right",
                cell: (u) =>
                  u.suspended ? (
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => act(() => api.post(`/admin/users/${u.id}/reinstate`))}
                    >
                      恢復
                    </Button>
                  ) : (
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => {
                        const reason = prompt(`停權「${u.displayName}」的原因：`);
                        if (reason) act(() => api.post(`/admin/users/${u.id}/suspend`, { reason }));
                      }}
                    >
                      停權
                    </Button>
                  ),
              },
            ]}
          />
        </section>
      </div>
    </PageShell>
  );
}
