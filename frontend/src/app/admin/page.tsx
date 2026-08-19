"use client";

import Link from "next/link";
import { useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, Empty, ErrorNote, Spinner } from "@/components/ui";
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
      <p className="py-12 text-center text-sm text-stone-500">
        這個頁面只有管理員能存取。
        <Link href="/" className="ml-1 text-red-600 hover:underline">
          回首頁
        </Link>
      </p>
    );
  }

  const stats: [string, number][] = dashboard
    ? [
        ["會員總數", dashboard.totalUsers],
        ["停權中", dashboard.suspendedUsers],
        ["餐廳", dashboard.totalRestaurants],
        ["商品", dashboard.totalProducts],
        ["待付款訂單", dashboard.pendingOrders],
        ["未處理訊息", dashboard.openInquiries],
      ]
    : [];

  return (
    <div className="mx-auto max-w-7xl space-y-8 px-6 py-10">
      <div className="flex flex-wrap items-center gap-3">
        <h1 className="mr-auto text-2xl font-bold">後台</h1>
        <Link href="/admin/restaurants">
          <Button variant="ghost">餐廳管理</Button>
        </Link>
        <Link href="/admin/audit-log">
          <Button variant="ghost">操作紀錄</Button>
        </Link>
      </div>
      <ErrorNote error={error} />

      <div className="grid grid-cols-2 gap-3 sm:grid-cols-3 lg:grid-cols-6">
        {stats.map(([label, value]) => (
          <Card key={label} className="p-4 text-center">
            <p className="text-2xl font-bold">{value}</p>
            <p className="mt-1 text-xs text-stone-500">{label}</p>
          </Card>
        ))}
      </div>

      <section>
        <h2 className="mb-3 text-lg font-semibold">聯絡我們（未處理）</h2>
        {inquiries.length === 0 ? (
          <Empty>沒有待處理的訊息。</Empty>
        ) : (
          <ul className="space-y-2">
            {inquiries.map((inquiry) => (
              <Card key={inquiry.id} className="p-4">
                <div className="flex items-start justify-between gap-3">
                  <div className="flex-1">
                    <p className="text-sm font-medium">
                      {inquiry.submitterName ?? inquiry.contactEmail ?? "訪客"}
                    </p>
                    <p className="mt-1 whitespace-pre-wrap text-sm text-stone-600 dark:text-stone-400">
                      {inquiry.body}
                    </p>
                    <p className="mt-1 text-xs text-stone-400">
                      {new Date(inquiry.createdAt).toLocaleString("zh-TW")}
                    </p>
                  </div>
                  <Button
                    variant="ghost"
                    onClick={() =>
                      act(() => api.post(`/admin/inquiries/${inquiry.id}/resolve`, { resolutionNote: "已處理" }))
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

      <section>
        <h2 className="mb-3 text-lg font-semibold">會員管理</h2>
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="border-b border-stone-200 text-xs uppercase text-stone-500 dark:border-stone-800">
              <tr>
                <th className="py-2">信箱</th>
                <th className="py-2">名稱</th>
                <th className="py-2">角色</th>
                <th className="py-2">狀態</th>
                <th className="py-2 text-right">操作</th>
              </tr>
            </thead>
            <tbody>
              {users.map((u) => (
                <tr key={u.id} className="border-b border-stone-100 dark:border-stone-900">
                  <td className="py-2">{u.email}</td>
                  <td className="py-2">{u.displayName}</td>
                  <td className="py-2 text-xs text-stone-500">
                    {u.roles.map((r) => r.replace("ROLE_", "")).join(", ")}
                  </td>
                  <td className="py-2">
                    {u.suspended ? (
                      <span className="rounded bg-red-100 px-2 py-0.5 text-xs text-red-700 dark:bg-red-950 dark:text-red-300">
                        停權
                      </span>
                    ) : u.enabled ? (
                      <span className="text-xs text-green-600">正常</span>
                    ) : (
                      <span className="text-xs text-stone-400">未驗證</span>
                    )}
                  </td>
                  <td className="py-2 text-right">
                    {u.suspended ? (
                      <Button variant="ghost" onClick={() => act(() => api.post(`/admin/users/${u.id}/reinstate`))}>
                        恢復
                      </Button>
                    ) : (
                      <Button
                        variant="ghost"
                        onClick={() => {
                          const reason = prompt("停權原因：");
                          if (reason) act(() => api.post(`/admin/users/${u.id}/suspend`, { reason }));
                        }}
                      >
                        停權
                      </Button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
}
