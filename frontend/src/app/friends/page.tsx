"use client";

import Link from "next/link";
import { useCallback, useEffect, useState } from "react";
import { api, query } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, Empty, ErrorNote, Input, Spinner } from "@/components/ui";
import type { Conversation, Friend } from "@/lib/types";

interface PublicProfileRow {
  userId: number;
  nickname: string;
  avatarUrl?: string | null;
  friendshipStatus?: string | null;
}

export default function FriendsPage() {
  const { user, loading: authLoading } = useAuth();
  const [friends, setFriends] = useState<Friend[]>([]);
  const [incoming, setIncoming] = useState<Friend[]>([]);
  const [conversations, setConversations] = useState<Conversation[]>([]);
  const [results, setResults] = useState<PublicProfileRow[]>([]);
  const [nickname, setNickname] = useState("");
  const [error, setError] = useState<unknown>(null);
  const [loading, setLoading] = useState(true);

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
    try {
      setResults(await api.get<PublicProfileRow[]>(`/users/search${query({ nickname })}`));
    } catch (e) {
      setError(e);
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
      <p className="py-12 text-center text-sm text-stone-500">
        請先{" "}
        <Link href="/login?next=/friends" className="text-red-600 hover:underline">
          登入
        </Link>
        。
      </p>
    );
  }

  return (
    <div className="mx-auto max-w-7xl space-y-8 px-6 py-10">
      <h1 className="text-2xl font-bold">好友</h1>
      <ErrorNote error={error} />

      {incoming.length > 0 && (
        <section>
          <h2 className="mb-3 text-lg font-semibold">收到的邀請 ({incoming.length})</h2>
          <ul className="space-y-2">
            {incoming.map((request) => (
              <Card key={request.friendshipId} className="flex items-center gap-3 p-4">
                <span className="flex-1 font-medium">{request.nickname}</span>
                <Button onClick={() => act(() => api.post(`/friends/requests/${request.friendshipId}/accept`))}>
                  接受
                </Button>
                <Button
                  variant="ghost"
                  onClick={() => act(() => api.post(`/friends/requests/${request.friendshipId}/decline`))}
                >
                  拒絕
                </Button>
              </Card>
            ))}
          </ul>
        </section>
      )}

      <section>
        <h2 className="mb-3 text-lg font-semibold">尋找朋友</h2>
        <form onSubmit={search} className="flex gap-2">
          <Input
            placeholder="以暱稱搜尋…"
            aria-label="以暱稱搜尋"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
          />
          <Button type="submit">搜尋</Button>
        </form>
        {results.length > 0 && (
          <ul className="mt-3 space-y-2">
            {results
              .filter((r) => r.userId !== user.id)
              .map((result) => (
                <Card key={result.userId} className="flex items-center gap-3 p-4">
                  <span className="flex-1 font-medium">{result.nickname}</span>
                  {result.friendshipStatus === "NONE" ? (
                    <Button onClick={() => act(() => api.post(`/friends/requests/${result.userId}`))}>
                      加好友
                    </Button>
                  ) : (
                    <span className="text-xs text-stone-500">{result.friendshipStatus}</span>
                  )}
                </Card>
              ))}
          </ul>
        )}
      </section>

      <section>
        <h2 className="mb-3 text-lg font-semibold">我的好友 ({friends.length})</h2>
        {friends.length === 0 ? (
          <Empty>還沒有好友，搜尋暱稱加一個吧。</Empty>
        ) : (
          <ul className="space-y-2">
            {friends.map((friend) => {
              const conversation = conversations.find((c) => c.partnerId === friend.userId);
              return (
                <Card key={friend.friendshipId} className="flex items-center gap-3 p-4">
                  <div className="flex-1">
                    <p className="font-medium">{friend.nickname}</p>
                    {conversation && (
                      <p className="truncate text-xs text-stone-500">{conversation.lastMessage}</p>
                    )}
                  </div>
                  {conversation && conversation.unreadCount > 0 && (
                    <span className="rounded-full bg-red-600 px-2 py-0.5 text-xs font-semibold text-white">
                      {conversation.unreadCount}
                    </span>
                  )}
                  <Link href={`/friends/${friend.userId}`}>
                    <Button variant="ghost">聊天</Button>
                  </Link>
                  <Button variant="ghost" onClick={() => act(() => api.delete(`/friends/${friend.friendshipId}`))}>
                    移除
                  </Button>
                </Card>
              );
            })}
          </ul>
        )}
      </section>
    </div>
  );
}
