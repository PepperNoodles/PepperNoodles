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
  FilterChip,
  ListSkeleton,
  PageHeader,
  PageShell,
  Pagination,
  TagPill,
} from "@/components/ui";
import { IconBookmark, IconMessage, IconPencil, IconStar } from "@/components/icons";
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
    <PageShell width="reading">
      <PageHeader
        kicker="What's News"
        title="專欄文章"
        description="會員分享的食記、口袋名單與踩雷紀錄。"
        actions={
          user && (
            <>
              <Button
                variant="ghost"
                onClick={() => {
                  setOnlyBookmarked(!onlyBookmarked);
                  setPageNumber(0);
                }}
                aria-pressed={onlyBookmarked}
                icon={<IconBookmark />}
              >
                {onlyBookmarked ? "看全部" : "我的收藏"}
              </Button>
              <ButtonLink href="/forum/new" icon={<IconPencil />}>
                發表文章
              </ButtonLink>
            </>
          )
        }
      />

      <div className="space-y-6">
        {!onlyBookmarked && tags.length > 0 && (
          <fieldset>
            <legend className="mb-2.5 text-[13px] font-semibold uppercase tracking-wider text-subtle">
              依標籤篩選
            </legend>
            <div className="flex flex-wrap gap-2">
              {tags.map((tag) => {
                const active = activeTags.includes(tag.id);
                return (
                  <FilterChip
                    key={tag.id}
                    active={active}
                    onClick={() => {
                      setPageNumber(0);
                      setActiveTags(active ? activeTags.filter((i) => i !== tag.id) : [...activeTags, tag.id]);
                    }}
                  >
                    {tag.name}
                  </FilterChip>
                );
              })}
            </div>
          </fieldset>
        )}

        {loading ? (
          <ListSkeleton rows={4} />
        ) : !page || page.content.length === 0 ? (
          <Empty
            icon={<IconPencil />}
            action={user && !onlyBookmarked ? <ButtonLink href="/forum/new">發表文章</ButtonLink> : undefined}
          >
            {onlyBookmarked ? "還沒有收藏任何文章。" : "還沒有人發表文章，成為第一個吧！"}
          </Empty>
        ) : (
          <>
            <ul className="space-y-5">
              {page.content.map((post) => (
                <Card key={post.id} as="li" interactive className="p-6">
                  <Link href={`/forum/${post.id}`} className="block min-h-11 sm:min-h-0">
                    <p className="measure whitespace-pre-wrap text-[15px] leading-relaxed text-body">
                      {post.excerpt}
                    </p>
                  </Link>

                  <div className="mt-5 flex flex-wrap items-center gap-x-3 gap-y-2 border-t border-line pt-4 text-[13px] text-subtle">
                    <Link
                      href={`/members/${post.author.userId}`}
                      className="-mx-2 inline-flex min-h-11 items-center rounded-lg px-2 font-semibold text-ink transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-2"
                    >
                      {post.author.displayName}
                    </Link>
                    <span aria-hidden className="text-line-strong">
                      ·
                    </span>
                    <time dateTime={post.createdAt} className="tabular">
                      {new Date(post.createdAt).toLocaleDateString("zh-TW")}
                    </time>
                    <span className="inline-flex items-center gap-1 tabular">
                      <IconMessage aria-hidden className="text-base" />
                      {post.commentCount}
                      <span className="sr-only">則留言</span>
                    </span>
                    <span className="inline-flex items-center gap-1 tabular">
                      <IconStar aria-hidden className="text-base" />
                      {post.bookmarkCount}
                      <span className="sr-only">次收藏</span>
                    </span>
                    {post.tags.length > 0 && (
                      <span className="ml-auto flex flex-wrap gap-1.5">
                        {post.tags.map((tag) => (
                          <TagPill key={tag.id}>{tag.name}</TagPill>
                        ))}
                      </span>
                    )}
                  </div>
                </Card>
              ))}
            </ul>

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
