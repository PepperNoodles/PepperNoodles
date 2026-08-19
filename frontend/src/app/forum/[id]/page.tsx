"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { use, useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, ErrorNote, Spinner, TagPill } from "@/components/ui";
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
  if (!post) return <div className="mx-auto max-w-3xl px-6 py-10"><ErrorNote error={error} /></div>;

  return (
    <div className="mx-auto max-w-3xl space-y-6 px-6 py-10">
      <Link href="/forum" className="text-sm text-stone-500 hover:text-pepper">
        ← 回到專欄
      </Link>

      <Card className="p-6">
        <div className="flex items-start justify-between gap-4">
          <div className="flex items-center gap-3">
            <Link href={`/members/${post.author.userId}`} className="font-semibold hover:text-pepper">
              {post.author.displayName}
            </Link>
            <span className="text-xs text-stone-400">
              {new Date(post.createdAt).toLocaleString("zh-TW")}
            </span>
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
              className={`text-sm ${post.bookmarked ? "text-pepper" : "text-stone-400 hover:text-pepper"}`}
            >
              {post.bookmarked ? "★ 已收藏" : "☆ 收藏"} {post.bookmarkCount}
            </button>
          )}
        </div>

        <p className="mt-4 whitespace-pre-wrap leading-relaxed">{post.body}</p>

        {post.imageUrl && (
          /* eslint-disable-next-line @next/next/no-img-element */
          <img src={post.imageUrl} alt="" className="mt-4 max-h-96 w-full rounded object-cover" />
        )}

        <div className="mt-4 flex flex-wrap gap-1.5">
          {post.tags.map((tag) => (
            <TagPill key={tag.id}>{tag.name}</TagPill>
          ))}
        </div>

        {post.editable && (
          <div className="mt-4 flex gap-3 text-xs">
            <Link href={`/forum/${id}/edit`} className="text-stone-400 hover:text-pepper">
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
              className="text-stone-400 hover:text-pepper"
            >
              刪除文章
            </button>
          </div>
        )}
      </Card>

      <ErrorNote error={error} />

      <section>
        <h2 className="mb-3 text-lg font-semibold">留言 ({post.comments.length})</h2>

        {user ? (
          <Card className="mb-4 p-5">
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
              <textarea
                required
                rows={2}
                maxLength={2000}
                value={comment}
                onChange={(e) => setComment(e.target.value)}
                placeholder="留下你的想法…"
                aria-label="留言內容"
                className="w-full rounded-lg border border-stone-300 px-3 py-2 text-sm outline-none focus:border-pepper"
              />
              <Button type="submit">送出留言</Button>
            </form>
          </Card>
        ) : (
          <p className="mb-4 text-sm text-stone-500">
            <Link href="/login" className="text-pepper hover:underline">
              登入
            </Link>{" "}
            後即可留言。
          </p>
        )}

        <ul className="space-y-3">
          {post.comments.map((c) => (
            <Card key={c.id} className="p-5">
              <div className="flex items-start justify-between gap-3">
                <div>
                  <Link href={`/members/${c.author.userId}`} className="font-medium hover:text-pepper">
                    {c.author.displayName}
                  </Link>
                  <span className="ml-2 text-xs text-stone-400">
                    {new Date(c.createdAt).toLocaleDateString("zh-TW")}
                  </span>
                </div>
                {c.score && <span className="text-amber-500">{"★".repeat(c.score)}</span>}
              </div>
              <p className="mt-2 whitespace-pre-wrap text-sm">{c.body}</p>

              {c.replies.length > 0 && (
                <ul className="mt-3 space-y-2 border-l-2 border-stone-200 pl-4">
                  {c.replies.map((r) => (
                    <li key={r.id} className="text-sm">
                      <Link href={`/members/${r.author.userId}`} className="font-medium hover:text-pepper">
                        {r.author.displayName}
                      </Link>
                      {r.replyTo && <span className="text-xs text-stone-400"> 回覆 {r.replyTo.displayName}</span>}
                      <p className="mt-0.5 whitespace-pre-wrap text-stone-600">{r.body}</p>
                      {r.editable && (
                        <button
                          onClick={() => act(() => api.delete(`/forum/replies/${r.id}`))}
                          className="text-xs text-stone-400 hover:text-pepper"
                        >
                          刪除
                        </button>
                      )}
                    </li>
                  ))}
                </ul>
              )}

              <div className="mt-3 flex gap-3 text-xs">
                {user && (
                  <button
                    onClick={() => {
                      setReplyingTo(replyingTo === c.id ? null : c.id);
                      setReplyBody("");
                    }}
                    className="text-stone-400 hover:text-pepper"
                  >
                    回覆
                  </button>
                )}
                {c.editable && (
                  <button
                    onClick={() => act(() => api.delete(`/forum/comments/${c.id}`))}
                    className="text-stone-400 hover:text-pepper"
                  >
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
                  className="mt-3 flex gap-2"
                >
                  <input
                    required
                    value={replyBody}
                    onChange={(e) => setReplyBody(e.target.value)}
                    placeholder={`回覆 ${c.author.displayName}…`}
                    aria-label="回覆內容"
                    className="flex-1 rounded-lg border border-stone-300 px-3 py-1.5 text-sm outline-none focus:border-pepper"
                  />
                  <Button type="submit">送出</Button>
                </form>
              )}
            </Card>
          ))}
        </ul>
      </section>
    </div>
  );
}
