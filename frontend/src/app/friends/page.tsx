"use client";

import Link from "next/link";
import { useCallback, useEffect, useState } from "react";
import { api, query } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import {
  Badge,
  Button,
  ButtonLink,
  Card,
  Empty,
  ErrorNote,
  Gate,
  Input,
  PageHeader,
  PageShell,
  SectionHeader,
  Spinner,
} from "@/components/ui";
import { IconMessage, IconSearch, IconUser, IconUsers } from "@/components/icons";
import type { Conversation, Friend } from "@/lib/types";

interface PublicProfileRow {
  userId: number;
  nickname: string;
  avatarUrl?: string | null;
  friendshipStatus?: string | null;
}

/** How the API's friendship states read to a person. */
const FRIENDSHIP_LABELS: Record<string, string> = {
  PENDING: "邀請已送出",
  ACCEPTED: "已是好友",
  DECLINED: "已婉拒",
  BLOCKED: "已封鎖",
};

export default function FriendsPage() {
  const { user, loading: authLoading } = useAuth();
  const [friends, setFriends] = useState<Friend[]>([]);
  const [incoming, setIncoming] = useState<Friend[]>([]);
  const [conversations, setConversations] = useState<Conversation[]>([]);
  const [results, setResults] = useState<PublicProfileRow[] | null>(null);
  const [nickname, setNickname] = useState("");
  const [error, setError] = useState<unknown>(null);
  const [loading, setLoading] = useState(true);
  const [searching, setSearching] = useState(false);

  const load = useCallback(() => {
    if (!user) return;
    Promise.all([
      api.get<Friend[]>("/friends"),
      api.get<Friend[]>("/friends/requests/incoming"),
      api.get<Conversation[]>("/chat/conversations"),
    ])
      .then(([f, i, c]) => {
        setFriends(f);
        setIncoming(i);
        setConversations(c);
      })
      .catch(setError)
      .finally(() => setLoading(false));
  }, [user]);

  useEffect(load, [load]);

  async function search(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setSearching(true);
    try {
      setResults(await api.get<PublicProfileRow[]>(`/users/search${query({ nickname })}`));
    } catch (e) {
      setError(e);
    } finally {
      setSearching(false);
    }
  }

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
  if (!user) {
    return (
      <Gate title="請先登入" action={<ButtonLink href="/login?next=/friends">前往登入</ButtonLink>}>
        登入後即可加好友、傳訊息。
      </Gate>
    );
  }

  const visibleResults = results?.filter((r) => r.userId !== user.id) ?? null;

  return (
    <PageShell>
      <PageHeader kicker="Your people" title="好友" />

      <div className="space-y-12">
        <ErrorNote error={error} />

        {/* ---------- Incoming requests ---------- */}
        {incoming.length > 0 && (
          <section>
            <SectionHeader title="收到的邀請" count={incoming.length} />
            <ul className="space-y-3">
              {incoming.map((request) => (
                <Card key={request.friendshipId} as="li" className="flex flex-wrap items-center gap-4 p-4">
                  <PersonAvatar name={request.nickname} />
                  <span className="min-w-0 flex-1 font-semibold text-ink">{request.nickname}</span>
                  <div className="flex gap-2.5">
                    <Button
                      size="sm"
                      onClick={() => act(() => api.post(`/friends/requests/${request.friendshipId}/accept`))}
                    >
                      接受
                    </Button>
                    <Button
                      size="sm"
                      variant="ghost"
                      onClick={() => act(() => api.post(`/friends/requests/${request.friendshipId}/decline`))}
                    >
                      拒絕
                    </Button>
                  </div>
                </Card>
              ))}
            </ul>
          </section>
        )}

        {/* ---------- Find people ---------- */}
        <section>
          <SectionHeader title="尋找朋友" description="以暱稱搜尋其他會員。" />
          <form onSubmit={search} className="flex flex-col gap-2.5 sm:flex-row" role="search">
            <div className="relative flex-1">
              <IconSearch
                aria-hidden
                className="pointer-events-none absolute left-4 top-1/2 -translate-y-1/2 text-lg text-subtle"
              />
              <Input
                type="search"
                placeholder="以暱稱搜尋…"
                aria-label="以暱稱搜尋"
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
                className="pl-11"
              />
            </div>
            <Button type="submit" loading={searching}>
              搜尋
            </Button>
          </form>

          {visibleResults !== null && (
            <div className="mt-4">
              {visibleResults.length === 0 ? (
                <p className="text-sm text-subtle">找不到這個暱稱的會員。</p>
              ) : (
                <ul className="space-y-3">
                  {visibleResults.map((result) => (
                    <Card key={result.userId} as="li" className="flex flex-wrap items-center gap-4 p-4">
                      <PersonAvatar name={result.nickname} url={result.avatarUrl} />
                      <Link
                        href={`/members/${result.userId}`}
                        className="min-w-0 flex-1 -mx-2 inline-flex min-h-11 items-center rounded-lg px-2 font-semibold text-ink transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-2"
                      >
                        {result.nickname}
                      </Link>
                      {result.friendshipStatus === "NONE" ? (
                        <Button size="sm" onClick={() => act(() => api.post(`/friends/requests/${result.userId}`))}>
                          加好友
                        </Button>
                      ) : (
                        <Badge>{FRIENDSHIP_LABELS[result.friendshipStatus ?? ""] ?? result.friendshipStatus}</Badge>
                      )}
                    </Card>
                  ))}
                </ul>
              )}
            </div>
          )}
        </section>

        {/* ---------- Friends ---------- */}
        <section>
          <SectionHeader title="我的好友" count={friends.length} />
          {friends.length === 0 ? (
            <Empty icon={<IconUsers />}>還沒有好友，用上面的搜尋加一個吧。</Empty>
          ) : (
            <ul className="space-y-3">
              {friends.map((friend) => {
                const conversation = conversations.find((c) => c.partnerId === friend.userId);
                const unread = conversation?.unreadCount ?? 0;
                return (
                  <Card key={friend.friendshipId} as="li" className="flex flex-wrap items-center gap-4 p-4">
                    <PersonAvatar name={friend.nickname} />
                    <div className="min-w-0 flex-1">
                      <Link
                        href={`/members/${friend.userId}`}
                        className="-mx-2 inline-flex min-h-11 items-center rounded-lg px-2 font-semibold text-ink transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-2"
                      >
                        {friend.nickname}
                      </Link>
                      {conversation && (
                        <p className="truncate text-[13px] text-subtle">{conversation.lastMessage}</p>
                      )}
                    </div>

                    {unread > 0 && (
                      <span className="flex h-6 min-w-6 items-center justify-center rounded-full bg-pepper-fill px-1.5 text-xs font-bold tabular text-white">
                        {unread}
                        <span className="sr-only">則未讀訊息</span>
                      </span>
                    )}

                    <div className="flex gap-2.5">
                      <ButtonLink
                        href={`/friends/${friend.userId}`}
                        variant="ghost"
                        size="sm"
                        icon={<IconMessage />}
                      >
                        聊天
                      </ButtonLink>
                      <Button
                        variant="quiet"
                        size="sm"
                        onClick={() => act(() => api.delete(`/friends/${friend.friendshipId}`))}
                      >
                        移除
                      </Button>
                    </div>
                  </Card>
                );
              })}
            </ul>
          )}
        </section>
      </div>
    </PageShell>
  );
}

function PersonAvatar({ name, url }: { name: string; url?: string | null }) {
  if (url) {
    // eslint-disable-next-line @next/next/no-img-element
    return <img src={url} alt="" className="h-10 w-10 shrink-0 rounded-full object-cover ring-1 ring-line" />;
  }
  return (
    <span
      aria-hidden
      title={name}
      className="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-mist text-lg text-subtle ring-1 ring-line"
    >
      <IconUser />
    </span>
  );
}
