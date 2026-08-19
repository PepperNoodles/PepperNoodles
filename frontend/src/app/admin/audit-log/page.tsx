"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api, query } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, Empty, ErrorNote, Spinner } from "@/components/ui";
import type { Page } from "@/lib/types";

interface AuditEntry {
  id: number;
  actorUserId?: number | null;
  actorEmail?: string | null;
  action: string;
  targetType: string;
  targetId: string;
  detail?: string | null;
  createdAt: string;
}

const ACTION_LABELS: Record<string, string> = {
  SUSPEND_USER: "停權會員",
  REINSTATE_USER: "恢復權限",
  RESOLVE_INQUIRY: "處理訊息",
};

/** Pulls the human-readable reason out of the stored JSON detail. */
function reasonOf(detail?: string | null) {
  if (!detail) return null;
  try {
    return (JSON.parse(detail) as { reason?: string }).reason ?? null;
  } catch {
    return detail;
  }
}

export default function AuditLogPage() {
  const { hasRole, loading: authLoading } = useAuth();
  const [page, setPage] = useState<Page<AuditEntry> | null>(null);
  const [pageNumber, setPageNumber] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);

  useEffect(() => {
    if (authLoading) return;
    if (!hasRole("ROLE_ADMIN")) {
      setLoading(false);
      return;
    }
    setLoading(true);
    api
      .get<Page<AuditEntry>>(`/admin/audit-log${query({ page: pageNumber, size: 50 })}`)
      .then(setPage)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [authLoading, hasRole, pageNumber]);

  if (authLoading || loading) return <Spinner />;
  if (!hasRole("ROLE_ADMIN")) {
    return (
      <p className="py-16 text-center text-sm text-stone-500">
        這個頁面只有管理員能存取。
        <Link href="/" className="ml-1 text-pepper hover:underline">
          回首頁
        </Link>
      </p>
    );
  }

  return (
    <div className="mx-auto max-w-5xl space-y-6 px-6 py-10">
      <div>
        <Link href="/admin" className="text-sm text-stone-500 hover:text-pepper">
          ← 回到後台
        </Link>
        <h1 className="mt-2 text-2xl font-bold">操作紀錄</h1>
        <p className="mt-1 text-sm text-stone-500">
          誰在什麼時候對哪個對象做了什麼。2021 年的後台沒有留下任何紀錄。
        </p>
      </div>

      <ErrorNote error={error} />

      {!page || page.content.length === 0 ? (
        <Empty>還沒有任何操作紀錄。</Empty>
      ) : (
        <>
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm">
              <thead className="border-b border-stone-200 text-xs uppercase text-stone-500">
                <tr>
                  <th className="py-2">時間</th>
                  <th className="py-2">操作者</th>
                  <th className="py-2">動作</th>
                  <th className="py-2">對象</th>
                  <th className="py-2">原因</th>
                </tr>
              </thead>
              <tbody>
                {page.content.map((entry) => (
                  <tr key={entry.id} className="border-b border-stone-100 last:border-0">
                    <td className="whitespace-nowrap py-2 text-stone-500">
                      {new Date(entry.createdAt).toLocaleString("zh-TW")}
                    </td>
                    <td className="py-2">{entry.actorEmail ?? "—"}</td>
                    <td className="py-2">
                      <span className="rounded bg-stone-100 px-2 py-0.5 text-xs font-medium">
                        {ACTION_LABELS[entry.action] ?? entry.action}
                      </span>
                    </td>
                    <td className="py-2 text-stone-500">
                      {entry.targetType} #{entry.targetId}
                    </td>
                    <td className="py-2 text-stone-600">{reasonOf(entry.detail) ?? "—"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {page.totalPages > 1 && (
            <div className="flex items-center justify-center gap-3">
              <Button variant="ghost" disabled={page.first} onClick={() => setPageNumber((n) => n - 1)}>
                上一頁
              </Button>
              <span className="text-sm text-stone-500">
                {page.page + 1} / {page.totalPages}
              </span>
              <Button variant="ghost" disabled={page.last} onClick={() => setPageNumber((n) => n + 1)}>
                下一頁
              </Button>
            </div>
          )}
        </>
      )}
    </div>
  );
}
