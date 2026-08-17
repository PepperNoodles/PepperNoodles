"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api, query } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, Empty, Spinner, TagPill } from "@/components/ui";
import type { ForumPostSummary, Page, Tag } from "@/lib/types";

export default function ForumPage() {
  const { user } = useAuth();
  const [page, setPage] = useState<Page<ForumPostSummary> | null>(null);
  const [tags, setTags] = useState<Tag[]>([]);
  const [activeTags, setActiveTags] = useState<number[]>([]);
  const [onlyBookmarked, setOnlyBookmarked] = useState(false);
  const [pageNumber, setPageNumber] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get<Tag[]>("/food-tags", { anonymous: true }).then(setTags).catch(() => setTags([]));
  }, []);

  useEffect(() => {
    setLoading(true);
    const path = onlyBookmarked
      ? `/forum/bookmarks${query({ page: pageNumber, size: 10 })}`
      : `/forum/posts${query({ tagIds: activeTags, page: pageNumber, size: 10 })}`;
    api
      .get<Page<ForumPostSummary>>(path)
      .then(setPage)
      .catch(() => setPage(null))
      .finally(() => setLoading(false));
  }, [activeTags, pageNumber, onlyBookmarked]);

  return (
    <div className="mx-auto max-w-4xl space-y-6 px-6 py-10">
      <div className="flex flex-wrap items-center gap-3">
        <div className="mr-auto">
          <span className="font-script text-3xl text-pepper">What&apos;s News</span>
          <h1 className="text-2xl font-bold">專欄文章</h1>
        </div>
        {user && (
          <>
            <Button variant="ghost" onClick={() => { setOnlyBookmarked(!onlyBookmarked); setPageNumber(0); }}>
              {onlyBookmarked ? "看全部" : "我的收藏"}
            </Button>
            <Link href="/forum/new">
              <Button>發表文章</Button>
            </Link>
          </>
        )}
      </div>

      {!onlyBookmarked && (
        <div className="flex flex-wrap gap-2">
          {tags.map((tag) => {
            const active = activeTags.includes(tag.id);
            return (
              <button
                key={tag.id}
                aria-pressed={active}
                onClick={() => {
                  setPageNumber(0);
                  setActiveTags(active ? activeTags.filter((i) => i !== tag.id) : [...activeTags, tag.id]);
                }}
                className={`rounded-full px-3 py-1 text-xs font-medium transition ${
                  active ? "bg-pepper text-white" : "bg-stone-100 text-stone-700 hover:bg-stone-200"
                }`}
              >
                {tag.name}
              </button>
            );
          })}
        </div>
      )}

      {loading ? (
        <Spinner />
      ) : !page || page.content.length === 0 ? (
        <Empty>{onlyBookmarked ? "還沒有收藏任何文章。" : "還沒有人發表文章，成為第一個吧！"}</Empty>
      ) : (
        <>
          <ul className="space-y-4">
            {page.content.map((post) => (
              <Card key={post.id} className="p-5 transition hover:shadow-md">
                <Link href={`/forum/${post.id}`} className="block">
                  <p className="whitespace-pre-wrap text-sm leading-relaxed">{post.excerpt}</p>
                </Link>
                <div className="mt-3 flex flex-wrap items-center gap-2 text-xs text-stone-500">
                  <Link href={`/members/${post.author.userId}`} className="font-medium hover:text-pepper">
                    {post.author.displayName}
                  </Link>
                  <span>·</span>
                  <span>{new Date(post.createdAt).toLocaleDateString("zh-TW")}</span>
                  <span>·</span>
                  <span>💬 {post.commentCount}</span>
                  <span>★ {post.bookmarkCount}</span>
                  <span className="ml-auto flex gap-1.5">
                    {post.tags.map((tag) => (
                      <TagPill key={tag.id}>{tag.name}</TagPill>
                    ))}
                  </span>
                </div>
              </Card>
            ))}
          </ul>

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
