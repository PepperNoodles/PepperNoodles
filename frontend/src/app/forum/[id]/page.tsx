"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { use, useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import {
  Alert,
  Button,
  Card,
  CharCount,
  ErrorNote,
  PageShell,
  SectionHeader,
  Spinner,
  TagPill,
  Textarea,
  TextLink,
} from "@/components/ui";
import {
  IconArrowLeft,
  IconBookmark,
  IconBookmarkFilled,
  IconMessage,
  IconPencil,
  IconTrash,
} from "@/components/icons";
import type { ForumPostDetail } from "@/lib/types";

export default function ForumPostPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const { user } = useAuth();
  const router = useRouter();
  const [post, setPost] = useState<ForumPostDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);
  const [comment, setComment] = useState("");
  /** Which comment the reply box is open under. */
  const [replyingTo, setReplyingTo] = useState<number | null>(null);
  const [replyBody, setReplyBody] = useState("");

  const load = useCallback(() => {
    api
      .get<ForumPostDetail>(`/forum/posts/${id}`)
      .then(setPost)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [id]);

  useEffect(load, [load]);

  async function act(fn: () => Promise<unknown>) {
    setError(null);
    try {
      await fn();
      load();
    } catch (e) {
      setError(e);
    }
  }

  if (loading) return <Spinner />;
  if (!post) {
    return (
      <PageShell width="reading">
        <ErrorNote error={error} />
      </PageShell>
    );
  }

  return (
    <PageShell width="reading">
      <Link
        href="/forum"
        className="-ml-2 mb-4 inline-flex min-h-11 items-center gap-1.5 rounded-lg px-2 text-sm font-medium text-subtle transition hover:bg-mist hover:text-pepper-ink sm:-ml-0 sm:mb-6 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
      >
        <IconArrowLeft className="text-base" />
        回到專欄
      </Link>

      <Card as="article" className="p-7 sm:p-8">
        <header className="flex items-start justify-between gap-4 border-b border-line pb-5">
          <div className="min-w-0">
            <Link
              href={`/members/${post.author.userId}`}
              className="-mx-2 inline-flex min-h-11 items-center rounded-lg px-2 font-display text-base font-bold text-ink transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-2"
            >
              {post.author.displayName}
            </Link>
            <p className="mt-0.5 text-xs tabular text-subtle">
              <time dateTime={post.createdAt}>{new Date(post.createdAt).toLocaleString("zh-TW")}</time>
            </p>
          </div>

          {user && (
            <button
              onClick={() =>
                act(() =>
                  post.bookmarked
                    ? api.delete(`/forum/posts/${id}/bookmark`)
                    : api.put(`/forum/posts/${id}/bookmark`),
                )
              }
              aria-pressed={post.bookmarked}
              className={`inline-flex min-h-10 shrink-0 cursor-pointer items-center gap-1.5 rounded-full border px-4 text-[13px] font-semibold transition ${
                post.bookmarked
                  ? "border-pepper bg-pepper-tint text-pepper-ink"
                  : "border-line-strong text-body hover:border-ink/40 hover:bg-mist"
              }`}
            >
              {post.bookmarked ? <IconBookmarkFilled /> : <IconBookmark />}
              {post.bookmarked ? "已收藏" : "收藏"}
              <span className="tabular">{post.bookmarkCount}</span>
            </button>
          )}
        </header>

        <p className="measure mt-6 whitespace-pre-wrap text-[17px] leading-[1.8] text-body">{post.body}</p>

        {post.imageUrl && (
          /* eslint-disable-next-line @next/next/no-img-element */
          <img
            src={post.imageUrl}
            alt=""
            loading="lazy"
            className="mt-7 max-h-[30rem] w-full rounded-2xl object-cover"
          />
        )}

        {post.tags.length > 0 && (
          <div className="mt-7 flex flex-wrap gap-1.5">
            {post.tags.map((tag) => (
              <TagPill key={tag.id}>{tag.name}</TagPill>
            ))}
          </div>
        )}

        {post.editable && (
          <div className="mt-7 flex gap-4 border-t border-line pt-5 text-[13px]">
            <Link
              href={`/forum/${id}/edit`}
              className="inline-flex items-center gap-1.5 font-medium text-subtle transition hover:text-pepper-ink"
            >
              <IconPencil aria-hidden className="text-base" />
              編輯文章
            </Link>
            <button
              onClick={() => {
                if (!confirm("確定要刪除這篇文章嗎？")) return;
                act(async () => {
                  await api.delete(`/forum/posts/${id}`);
                  router.push("/forum");
                });
              }}
              className="-mx-2 inline-flex min-h-11 cursor-pointer items-center gap-1.5 rounded-lg px-2 font-medium text-subtle transition hover:bg-danger-tint hover:text-danger sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
            >
              <IconTrash aria-hidden className="text-base" />
              刪除文章
            </button>
          </div>
        )}
      </Card>

      <div className="mt-5">
        <ErrorNote error={error} />
      </div>

      <section className="mt-12">
        <SectionHeader title="留言" count={post.comments.length} />

        {user ? (
          <Card className="mb-6 p-6">
            <form
              onSubmit={(e) => {
                e.preventDefault();
                act(async () => {
                  await api.post(`/forum/posts/${id}/comments`, { body: comment });
                  setComment("");
                });
              }}
              className="space-y-3"
            >
              <label htmlFor="comment" className="block text-sm font-semibold text-ink">
                留言內容
              </label>
              <Textarea
                id="comment"
                required
                rows={3}
                maxLength={2000}
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                placeholder="留下你的想法…"
              />
              <CharCount value={comment} max={2000} />
              <Button type="submit" icon={<IconMessage />}>
                送出留言
              </Button>
            </form>
          </Card>
        ) : (
          <div className="mb-6">
            <Alert tone="info">
              <TextLink href={`/login?next=/forum/${id}`}>登入</TextLink> 後即可留言。
            </Alert>
          </div>
        )}

        <ul className="space-y-4">
          {post.comments.map((c) => (
            <Card key={c.id} as="li" className="p-6">
              <div className="flex items-start justify-between gap-3">
                <div className="min-w-0">
                  <Link
                    href={`/members/${c.author.userId}`}
                    className="-mx-2 inline-flex min-h-11 items-center rounded-lg px-2 font-semibold text-ink transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-2"
                  >
                    {c.author.displayName}
                  </Link>
                  <p className="mt-0.5 text-xs tabular text-subtle">
                    <time dateTime={c.createdAt}>{new Date(c.createdAt).toLocaleDateString("zh-TW")}</time>
                  </p>
                </div>
                {c.score && (
                  <span className="shrink-0 text-sm tabular text-gold" aria-label={`${c.score} 分`}>
                    {"★".repeat(c.score)}
                  </span>
                )}
              </div>

              <p className="mt-3 whitespace-pre-wrap text-[15px] leading-relaxed text-body">{c.body}</p>

              {c.replies.length > 0 && (
                <ul className="mt-5 space-y-4 border-l-2 border-line pl-5">
                  {c.replies.map((r) => (
                    <li key={r.id}>
                      <p className="text-sm">
                        <Link
                          href={`/members/${r.author.userId}`}
                          className="-mx-2 inline-flex min-h-11 items-center rounded-lg px-2 font-semibold text-ink transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-2"
                        >
                          {r.author.displayName}
                        </Link>
                        {r.replyTo && (
                          <span className="text-xs text-subtle"> 回覆 {r.replyTo.displayName}</span>
                        )}
                      </p>
                      <p className="mt-1 whitespace-pre-wrap text-sm leading-relaxed text-body">{r.body}</p>
                      {r.editable && (
                        <button
                          onClick={() => act(() => api.delete(`/forum/replies/${r.id}`))}
                          className="-mx-2 mt-1 inline-flex min-h-11 cursor-pointer items-center gap-1 rounded-lg px-2 text-xs text-subtle transition hover:bg-danger-tint hover:text-danger sm:mx-0 sm:mt-1.5 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
                        >
                          <IconTrash aria-hidden className="text-sm" />
                          刪除
                        </button>
                      )}
                    </li>
                  ))}
                </ul>
              )}

              <div className="mt-4 flex gap-4 border-t border-line pt-3 text-[13px]">
                {user && (
                  <button
                    onClick={() => {
                      setReplyingTo(replyingTo === c.id ? null : c.id);
                      setReplyBody("");
                    }}
                    aria-expanded={replyingTo === c.id}
                    className="-mx-2 inline-flex min-h-11 cursor-pointer items-center gap-1.5 rounded-lg px-2 font-medium text-subtle transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
                  >
                    <IconMessage aria-hidden className="text-base" />
                    回覆
                  </button>
                )}
                {c.editable && (
                  <button
                    onClick={() => act(() => api.delete(`/forum/comments/${c.id}`))}
                    className="-mx-2 inline-flex min-h-11 cursor-pointer items-center gap-1.5 rounded-lg px-2 font-medium text-subtle transition hover:bg-danger-tint hover:text-danger sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
                  >
                    <IconTrash aria-hidden className="text-base" />
                    刪除
                  </button>
                )}
              </div>

              {replyingTo === c.id && (
                <form
                  onSubmit={(e) => {
                    e.preventDefault();
                    act(async () => {
                      await api.post(`/forum/comments/${c.id}/replies`, {
                        body: replyBody,
                        replyToUserId: c.author.userId,
                      });
                      setReplyingTo(null);
                      setReplyBody("");
                    });
                  }}
                  className="mt-4 flex flex-col gap-2.5 sm:flex-row"
                >
                  <input
                    required
                    autoFocus
                    value={replyBody}
                    onChange={(e) => setReplyBody(e.target.value)}
                    placeholder={`回覆 ${c.author.displayName}…`}
                    aria-label="回覆內容"
                    className="min-h-11 flex-1 rounded-xl border border-line-strong bg-white px-3.5 text-sm text-ink transition placeholder:text-subtle focus:border-pepper focus:outline-none focus:ring-4 focus:ring-pepper/15"
                  />
                  <Button type="submit">送出</Button>
                </form>
              )}
            </Card>
          ))}
        </ul>
      </section>
    </PageShell>
  );
}
