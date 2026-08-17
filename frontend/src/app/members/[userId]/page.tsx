"use client";

import Link from "next/link";
import { use, useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, Empty, ErrorNote, Spinner, TagPill } from "@/components/ui";
import type { FollowCounts, FollowUser, Page, PublicProfile, WallMessage } from "@/lib/types";

/** Another member's page: profile, 追蹤, and their 留言牆. */
export default function MemberPage({ params }: { params: Promise<{ userId: string }> }) {
  const { userId } = use(params);
  const { user } = useAuth();
  const id = Number(userId);

  const [profile, setProfile] = useState<PublicProfile | null>(null);
  const [counts, setCounts] = useState<FollowCounts | null>(null);
  const [wall, setWall] = useState<WallMessage[]>([]);
  const [followers, setFollowers] = useState<FollowUser[]>([]);
  const [showFollowers, setShowFollowers] = useState(false);
  const [body, setBody] = useState("");
  const [replyingTo, setReplyingTo] = useState<number | null>(null);
  const [replyBody, setReplyBody] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);

  const load = useCallback(() => {
    Promise.all([
      api.get<PublicProfile>(`/users/${id}`),
      api.get<FollowCounts>(`/users/${id}/follow-counts`),
      api.get<Page<WallMessage>>(`/users/${id}/wall?size=30`),
    ])
      .then(([p, c, w]) => {
        setProfile(p);
        setCounts(c);
        setWall(w.content);
      })
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
  if (!profile) return <div className="mx-auto max-w-3xl px-6 py-10"><ErrorNote error={error} /></div>;

  const isSelf = user?.id === id;

  return (
    <div className="mx-auto max-w-3xl space-y-6 px-6 py-10">
      <Card className="p-6">
        <div className="flex flex-wrap items-start gap-4">
          {profile.avatarUrl ? (
            /* eslint-disable-next-line @next/next/no-img-element */
            <img src={profile.avatarUrl} alt="" className="h-20 w-20 rounded-full object-cover" />
          ) : (
            <div className="flex h-20 w-20 items-center justify-center rounded-full bg-stone-100 text-3xl">
              🍜
            </div>
          )}

          <div className="flex-1">
            <h1 className="text-2xl font-bold">{profile.nickname}</h1>
            {profile.location && <p className="text-sm text-stone-500">{profile.location}</p>}
            <div className="mt-2 flex gap-4 text-sm">
              <button
                onClick={() => {
                  setShowFollowers(!showFollowers);
                  if (!showFollowers) {
                    api.get<FollowUser[]>(`/users/${id}/followers`).then(setFollowers).catch(setError);
                  }
                }}
                className="hover:text-pepper"
              >
                <strong>{counts?.followers ?? 0}</strong>{" "}
                <span className="text-stone-500">粉絲</span>
              </button>
              <span>
                <strong>{counts?.following ?? 0}</strong> <span className="text-stone-500">追蹤中</span>
              </span>
            </div>
            <div className="mt-3 flex flex-wrap gap-1.5">
              {profile.foodTags.map((tag) => (
                <TagPill key={tag.id}>{tag.name}</TagPill>
              ))}
            </div>
          </div>

          {user && !isSelf && (
            <div className="flex flex-col gap-2">
              <Button
                variant={counts?.followedByMe ? "ghost" : "primary"}
                onClick={() =>
                  act(() =>
                    counts?.followedByMe
                      ? api.delete(`/users/${id}/follow`)
                      : api.put(`/users/${id}/follow`),
                  )
                }
              >
                {counts?.followedByMe ? "已追蹤" : "追蹤"}
              </Button>
              {profile.friendshipStatus === "ACCEPTED" && (
                <Link href={`/friends/${id}`}>
                  <Button variant="ghost" className="w-full">
                    聊天
                  </Button>
                </Link>
              )}
              {profile.friendshipStatus === "NONE" && (
                <Button variant="ghost" onClick={() => act(() => api.post(`/friends/requests/${id}`))}>
                  加好友
                </Button>
              )}
            </div>
          )}
        </div>

        {showFollowers && (
          <ul className="mt-4 flex flex-wrap gap-2 border-t border-stone-100 pt-4">
            {followers.length === 0 && <li className="text-sm text-stone-400">還沒有粉絲。</li>}
            {followers.map((f) => (
              <li key={f.userId}>
                <Link
                  href={`/members/${f.userId}`}
                  className="rounded-full bg-stone-100 px-3 py-1 text-xs hover:bg-stone-200"
                >
                  {f.displayName}
                </Link>
              </li>
            ))}
          </ul>
        )}
      </Card>

      <ErrorNote error={error} />

      <section>
        <h2 className="mb-3 text-lg font-semibold">留言牆</h2>

        {user ? (
          <Card className="mb-4 p-5">
            <form
              onSubmit={(e) => {
                e.preventDefault();
                act(async () => {
                  await api.post(`/users/${id}/wall`, { body });
                  setBody("");
                });
              }}
              className="space-y-3"
            >
              <textarea
                required
                rows={2}
                maxLength={2000}
                value={body}
                onChange={(e) => setBody(e.target.value)}
                placeholder={isSelf ? "寫點什麼…" : `留言給 ${profile.nickname}…`}
                aria-label="留言內容"
                className="w-full rounded-lg border border-stone-300 px-3 py-2 text-sm outline-none focus:border-pepper"
              />
              <Button type="submit">留言</Button>
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

        {wall.length === 0 ? (
          <Empty>留言牆還是空的。</Empty>
        ) : (
          <ul className="space-y-3">
            {wall.map((message) => (
              <Card key={message.id} className="p-5">
                <div className="flex items-start justify-between gap-3">
                  <Link href={`/members/${message.author.userId}`} className="font-medium hover:text-pepper">
                    {message.author.displayName}
                  </Link>
                  <span className="text-xs text-stone-400">
                    {new Date(message.createdAt).toLocaleDateString("zh-TW")}
                  </span>
                </div>
                <p className="mt-2 whitespace-pre-wrap text-sm">{message.body}</p>

                <div className="mt-3 flex gap-3 text-xs">
                  {user && (
                    <button
                      onClick={() =>
                        act(() =>
                          message.likedByMe
                            ? api.delete(`/users/wall/${message.id}/like`)
                            : api.put(`/users/wall/${message.id}/like`),
                        )
                      }
                      className={message.likedByMe ? "text-pepper" : "text-stone-400 hover:text-pepper"}
                    >
                      {message.likedByMe ? "♥" : "♡"} {message.likeCount}
                    </button>
                  )}
                  {user && (
                    <button
                      onClick={() => {
                        setReplyingTo(replyingTo === message.id ? null : message.id);
                        setReplyBody("");
                      }}
                      className="text-stone-400 hover:text-pepper"
                    >
                      回覆
                    </button>
                  )}
                  {message.deletable && (
                    <button
                      onClick={() => act(() => api.delete(`/users/wall/${message.id}`))}
                      className="text-stone-400 hover:text-pepper"
                    >
                      刪除
                    </button>
                  )}
                </div>

                {message.replies.length > 0 && (
                  <ul className="mt-3 space-y-2 border-l-2 border-stone-200 pl-4">
                    {message.replies.map((reply) => (
                      <li key={reply.id} className="text-sm">
                        <Link
                          href={`/members/${reply.author.userId}`}
                          className="font-medium hover:text-pepper"
                        >
                          {reply.author.displayName}
                        </Link>
                        <p className="mt-0.5 whitespace-pre-wrap text-stone-600">{reply.body}</p>
                        {reply.deletable && (
                          <button
                            onClick={() => act(() => api.delete(`/users/wall/${reply.id}`))}
                            className="text-xs text-stone-400 hover:text-pepper"
                          >
                            刪除
                          </button>
                        )}
                      </li>
                    ))}
                  </ul>
                )}

                {replyingTo === message.id && (
                  <form
                    onSubmit={(e) => {
                      e.preventDefault();
                      act(async () => {
                        await api.post(`/users/${id}/wall`, { body: replyBody, parentId: message.id });
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
                      placeholder="回覆…"
                      aria-label="回覆內容"
                      className="flex-1 rounded-lg border border-stone-300 px-3 py-1.5 text-sm outline-none focus:border-pepper"
                    />
                    <Button type="submit">送出</Button>
                  </form>
                )}
              </Card>
            ))}
          </ul>
        )}
      </section>
    </div>
  );
}
