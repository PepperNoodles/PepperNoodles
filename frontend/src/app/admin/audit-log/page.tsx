"use client";

import { useEffect, useState } from "react";
import { api, query } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import {
  Badge,
  ButtonLink,
  Empty,
  ErrorNote,
  Gate,
  PageHeader,
  PageShell,
  DataTable,
  Pagination,
  Spinner,
} from "@/components/ui";
import { IconFile } from "@/components/icons";
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

/** Suspension is the one entry that reads as a warning rather than a note. */
const ACTION_TONES: Record<string, "neutral" | "warn" | "success"> = {
  SUSPEND_USER: "warn",
  REINSTATE_USER: "success",
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
      <Gate title="僅限管理員" action={<ButtonLink href="/" variant="ghost">回首頁</ButtonLink>}>
        這個頁面只有管理員能存取。
      </Gate>
    );
  }

  return (
    <PageShell width="full">
      <PageHeader
        title="操作紀錄"
        description="誰在什麼時候對哪個對象做了什麼。2021 年的後台沒有留下任何紀錄。"
        back={{ href: "/admin", label: "回到後台" }}
      />

      <div className="space-y-5">
        <ErrorNote error={error} />

        {!page || page.content.length === 0 ? (
          <Empty icon={<IconFile />}>還沒有任何操作紀錄。</Empty>
        ) : (
          <>
            <DataTable
              caption="後台操作紀錄"
              rows={page.content}
              rowKey={(e) => e.id}
              columns={[
                {
                  key: "action",
                  header: "動作",
                  primary: true,
                  cell: (e) => (
                    <Badge tone={ACTION_TONES[e.action] ?? "neutral"}>
                      {ACTION_LABELS[e.action] ?? e.action}
                    </Badge>
                  ),
                },
                {
                  key: "time",
                  header: "時間",
                  cell: (e) => (
                    <time dateTime={e.createdAt} className="whitespace-nowrap tabular text-subtle">
                      {new Date(e.createdAt).toLocaleString("zh-TW")}
                    </time>
                  ),
                },
                { key: "actor", header: "操作者", cell: (e) => e.actorEmail ?? "—" },
                {
                  key: "target",
                  header: "對象",
                  cell: (e) => (
                    <span className="whitespace-nowrap tabular text-subtle">
                      {e.targetType} #{e.targetId}
                    </span>
                  ),
                },
                { key: "reason", header: "原因", cell: (e) => reasonOf(e.detail) ?? "—" },
              ]}
            />

            <Pagination
              page={page.page}
              totalPages={page.totalPages}
              first={page.first}
              last={page.last}
              onChange={setPageNumber}
            />
          </>
        )}
      </div>
    </PageShell>
  );
}
