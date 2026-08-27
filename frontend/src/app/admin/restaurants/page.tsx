"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api, query } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import {
  Button,
  ButtonLink,
  Empty,
  ErrorNote,
  Gate,
  Input,
  PageHeader,
  PageShell,
  DataTable,
  Pagination,
  Spinner,
} from "@/components/ui";
import { IconSearch } from "@/components/icons";
import type { Page } from "@/lib/types";

interface ManagedRestaurant {
  id: number;
  name: string;
  address: string;
  contact?: string | null;
  ownerUserId: number;
  ownerEmail: string;
  ownerName: string;
  reviewCount: number;
  ratingAverage?: string | null;
  createdAt: string;
}

/** 後台餐廳查詢 — every listing with the account behind it. */
export default function AdminRestaurantsPage() {
  const { hasRole, loading: authLoading } = useAuth();
  const [page, setPage] = useState<Page<ManagedRestaurant> | null>(null);
  const [search, setSearch] = useState("");
  const [applied, setApplied] = useState("");
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
      .get<Page<ManagedRestaurant>>(`/admin/restaurants${query({ q: applied, page: pageNumber, size: 20 })}`)
      .then(setPage)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [authLoading, hasRole, applied, pageNumber]);

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
        title="餐廳管理"
        description="可依店名、地址或店主信箱搜尋。編輯與刪除沿用一般的餐廳管理畫面，管理員本來就有權限。"
        back={{ href: "/admin", label: "回到後台" }}
      />

      <div className="space-y-5">
        <form
          onSubmit={(e) => {
            e.preventDefault();
            setPageNumber(0);
            setApplied(search);
          }}
          className="flex flex-col gap-2.5 sm:flex-row sm:max-w-2xl"
          role="search"
        >
          <div className="relative flex-1">
            <IconSearch
              aria-hidden
              className="pointer-events-none absolute left-4 top-1/2 -translate-y-1/2 text-lg text-subtle"
            />
            <Input
              type="search"
              placeholder="搜尋店名、地址或店主信箱…"
              aria-label="搜尋餐廳"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="pl-11"
            />
          </div>
          <Button type="submit">搜尋</Button>
        </form>

        <ErrorNote error={error} />

        {!page || page.content.length === 0 ? (
          <Empty icon={<IconSearch />}>找不到符合條件的餐廳。</Empty>
        ) : (
          <>
            <p className="text-sm text-subtle" aria-live="polite">
              共 {page.totalElements} 間
            </p>

            <DataTable
              caption="後台餐廳列表"
              rows={page.content}
              rowKey={(r) => r.id}
              columns={[
                {
                  key: "name",
                  header: "餐廳",
                  primary: true,
                  cell: (r) => (
                    <>
                      <Link
                        href={`/restaurants/${r.id}`}
                        className="-mx-2 inline-flex min-h-11 items-center rounded-lg px-2 font-semibold text-ink transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-2"
                      >
                        {r.name}
                      </Link>
                      <p className="mt-0.5 text-xs font-normal text-subtle">{r.address}</p>
                    </>
                  ),
                },
                {
                  key: "owner",
                  header: "店主",
                  cell: (r) => (
                    <>
                      <Link
                        href={`/members/${r.ownerUserId}`}
                        className="text-body underline-offset-2 hover:text-pepper-ink hover:underline"
                      >
                        {r.ownerName}
                      </Link>
                      <p className="mt-0.5 break-words text-xs text-subtle">{r.ownerEmail}</p>
                    </>
                  ),
                },
                { key: "reviews", header: "評論", align: "right", cell: (r) => <span className="tabular">{r.reviewCount}</span> },
                {
                  key: "rating",
                  header: "評分",
                  align: "right",
                  cell: (r) => (
                    <span className="tabular">
                      {r.ratingAverage ? Number(r.ratingAverage).toFixed(1) : "—"}
                    </span>
                  ),
                },
                {
                  key: "actions",
                  header: "操作",
                  align: "right",
                  cell: (r) => (
                    <Link
                      href={`/company/restaurants/${r.id}`}
                      className="text-[13px] font-semibold text-pepper-ink underline-offset-2 hover:underline"
                    >
                      管理
                    </Link>
                  ),
                },
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
