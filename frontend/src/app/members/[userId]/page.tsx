"use client";

import Link from "next/link";
import { use, useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import {
  Alert,
  Button,
  ButtonLink,
  Card,
  CharCount,
  Empty,
  ErrorNote,
  PageShell,
  SectionHeader,
  Spinner,
  TagPill,
  Textarea,
  TextLink,
} from "@/components/ui";
import {
  IconHeart,
  IconHeartFilled,
  IconMapPin,
  IconMessage,
  IconTrash,
  IconUser,
} from "@/components/icons";
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
  if (!profile) {
    return (
      <PageShell width="reading">
        <ErrorNote error={error} />
      </PageShell>
    );
  }

  const isSelf = user?.id === id;

  return (
    <PageShell width="reading">
      {/* ---------- Profile ---------- */}
      <Card className="p-6 sm:p-8">
        <div className="flex flex-wrap items-start gap-6">
          {profile.avatarUrl ? (
            /* eslint-disable-next-line @next/next/no-img-element */
            <img
              src={profile.avatarUrl}
              alt=""
              className="h-24 w-24 shrink-0 rounded-full object-cover ring-1 ring-line"
            />
          ) : (
            <span
              aria-hidden
              className="flex h-24 w-24 shrink-0 items-center justify-center rounded-full bg-mist text-4xl text-line-strong ring-1 ring-line"
            >
              <IconUser />
            </span>
          )}

          <div className="min-w-0 flex-1">
            <h1 className="font-display text-2xl font-bold tracking-tight text-ink sm:text-3xl">
              {profile.nickname}
            </h1>
            {profile.location && (
              <p className="mt-1.5 flex items-center gap-1.5 text-sm text-subtle">
                <IconMapPin aria-hidden className="text-base" />
                {profile.location}
              </p>
            )}

            <div className="mt-4 flex gap-6 text-sm">
              <button
                onClick={() => {
                  setShowFollowers(!showFollowers);
                  if (!showFollowers) {
                    api.get<FollowUser[]>(`/users/${id}/followers`).then(setFollowers).catch(setError);
                  }
                }}
                aria-expanded={showFollowers}
                className="-mx-2 inline-flex min-h-11 cursor-pointer items-center rounded-lg px-2 transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
              >
                <strong className="font-display text-lg font-bold tabular text-ink">
                  {counts?.followers ?? 0}
                </strong>{" "}
                <span className="text-subtle">粉絲</span>
              </button>
              <span>
                <strong className="font-display text-lg font-bold tabular text-ink">
                  {counts?.following ?? 0}
                </strong>{" "}
                <span className="text-subtle">追蹤中</span>
              </span>
            </div>

            {profile.foodTags.length > 0 && (
              <div className="mt-4 flex flex-wrap gap-1.5">
                {profile.foodTags.map((tag) => (
                  <TagPill key={tag.id}>{tag.name}</TagPill>
                ))}
              </div>
            )}
          </div>

          {user && !isSelf && (
            <div className="flex w-full flex-col gap-2.5 sm:w-auto">
              <Button
                variant={counts?.followedByMe ? "ghost" : "primary"}
                aria-pressed={counts?.followedByMe}
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
                <ButtonLink href={`/friends/${id}`} variant="ghost" icon={<IconMessage />}>
                  聊天
                </ButtonLink>
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
          <div className="mt-6 border-t border-line pt-5">
            <h2 className="mb-3 text-[13px] font-semibold uppercase tracking-wider text-subtle">粉絲</h2>
            {followers.length === 0 ? (
              <p className="text-sm text-subtle">還沒有粉絲。</p>
            ) : (
              <ul className="flex flex-wrap gap-2">
                {followers.map((f) => (
                  <li key={f.userId}>
                    <Link
                      href={`/members/${f.userId}`}
                      className="inline-flex min-h-9 items-center rounded-full border border-line bg-mist px-3.5 text-[13px] font-medium text-body transition hover:border-pepper hover:bg-pepper-tint hover:text-pepper-ink"
                    >
                      {f.displayName}
                    </Link>
                  </li>
                ))}
              </ul>
            )}
          </div>
        )}
      </Card>

      <div className="mt-5">
        <ErrorNote error={error} />
      </div>

      {/* ---------- Wall ---------- */}
      <section className="mt-12">
        <SectionHeader title="留言牆" count={wall.length} />

        {user ? (
          <Card className="mb-6 p-6">
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
              <label htmlFor="wall-body" className="block text-sm font-semibold text-ink">
                留言內容
              </label>
              <Textarea
                id="wall-body"
                required
                rows={3}
                maxLength={2000}
                value={body}
                onChange={(e) => setBody(e.target.value)}
                placeholder={isSelf ? "寫點什麼…" : `留言給 ${profile.nickname}…`}
              />
              <CharCount value={body} max={2000} />
              <Button type="submit" icon={<IconMessage />}>
                留言
              </Button>
            </form>
          </Card>
        ) : (
          <div className="mb-6">
            <Alert tone="info">
              <TextLink href={`/login?next=/members/${id}`}>登入</TextLink> 後即可留言。
            </Alert>
          </div>
        )}

        {wall.length === 0 ? (
          <Empty icon={<IconMessage />}>留言牆還是空的。</Empty>
        ) : (
          <ul className="space-y-4">
            {wall.map((message) => (
              <Card key={message.id} as="li" className="p-6">
                <div className="flex items-start justify-between gap-3">
                  <Link
                    href={`/members/${message.author.userId}`}
                    className="-mx-2 inline-flex min-h-11 items-center rounded-lg px-2 font-semibold text-ink transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-2"
                  >
                    {message.author.displayName}
                  </Link>
                  <time dateTime={message.createdAt} className="shrink-0 text-xs tabular text-subtle">
                    {new Date(message.createdAt).toLocaleDateString("zh-TW")}
                  </time>
                </div>

                <p className="mt-3 whitespace-pre-wrap text-[15px] leading-relaxed text-body">
                  {message.body}
                </p>

                <div className="mt-4 flex gap-4 border-t border-line pt-3 text-[13px]">
                  {user && (
                    <button
                      onClick={() =>
                        act(() =>
                          message.likedByMe
                            ? api.delete(`/users/wall/${message.id}/like`)
                            : api.put(`/users/wall/${message.id}/like`),
                        )
                      }
                      aria-pressed={message.likedByMe}
                      aria-label={`${message.likedByMe ? "收回讚" : "按讚"}，目前 ${message.likeCount} 個讚`}
                      className={`-mx-2 inline-flex min-h-11 cursor-pointer items-center gap-1.5 rounded-lg px-2 font-medium transition sm:mx-0 sm:min-h-0 sm:px-0 ${
                        message.likedByMe ? "text-pepper" : "text-subtle hover:bg-mist hover:text-pepper-ink sm:hover:bg-transparent"
                      }`}
                    >
                      {message.likedByMe ? (
                        <IconHeartFilled aria-hidden className="text-base" />
                      ) : (
                        <IconHeart aria-hidden className="text-base" />
                      )}
                      <span className="tabular">{message.likeCount}</span>
                    </button>
                  )}
                  {user && (
                    <button
                      onClick={() => {
                        setReplyingTo(replyingTo === message.id ? null : message.id);
                        setReplyBody("");
                      }}
                      aria-expanded={replyingTo === message.id}
                      className="-mx-2 inline-flex min-h-11 cursor-pointer items-center gap-1.5 rounded-lg px-2 font-medium text-subtle transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
                    >
                      <IconMessage aria-hidden className="text-base" />
                      回覆
                    </button>
                  )}
                  {message.deletable && (
                    <button
                      onClick={() => act(() => api.delete(`/users/wall/${message.id}`))}
                      className="-mx-2 inline-flex min-h-11 cursor-pointer items-center gap-1.5 rounded-lg px-2 font-medium text-subtle transition hover:bg-danger-tint hover:text-danger sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
                    >
                      <IconTrash aria-hidden className="text-base" />
                      刪除
                    </button>
                  )}
                </div>

                {message.replies.length > 0 && (
                  <ul className="mt-4 space-y-4 border-l-2 border-line pl-5">
                    {message.replies.map((reply) => (
                      <li key={reply.id}>
                        <Link
                          href={`/members/${reply.author.userId}`}
                          className="-mx-2 inline-flex min-h-11 items-center rounded-lg px-2 text-sm font-semibold text-ink transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-2"
                        >
                          {reply.author.displayName}
                        </Link>
                        <p className="mt-1 whitespace-pre-wrap text-sm leading-relaxed text-body">
                          {reply.body}
                        </p>
                        {reply.deletable && (
                          <button
                            onClick={() => act(() => api.delete(`/users/wall/${reply.id}`))}
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
                    className="mt-4 flex flex-col gap-2.5 sm:flex-row"
                  >
                    <input
                      required
                      autoFocus
                      value={replyBody}
                      onChange={(e) => setReplyBody(e.target.value)}
                      placeholder="回覆…"
                      aria-label="回覆內容"
                      className="min-h-11 flex-1 rounded-xl border border-line-strong bg-white px-3.5 text-sm text-ink transition placeholder:text-subtle focus:border-pepper focus:outline-none focus:ring-4 focus:ring-pepper/15"
                    />
                    <Button type="submit">送出</Button>
                  </form>
                )}
              </Card>
            ))}
          </ul>
        )}
      </section>
    </PageShell>
  );
}
