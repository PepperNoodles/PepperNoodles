"use client";

import { use, useCallback, useEffect, useRef, useState } from "react";
import { api } from "@/lib/api";
import { connectChatSocket, type ConnectionState } from "@/lib/chatSocket";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, ErrorNote, Input, Spinner } from "@/components/ui";
import type { ChatMessage, Page } from "@/lib/types";

const STATE_LABELS: Record<ConnectionState, string> = {
  connecting: "連線中…",
  connected: "已連線",
  disconnected: "已斷線，重新連線中…",
};

export default function ChatPage({ params }: { params: Promise<{ userId: string }> }) {
  const { userId } = use(params);
  const { user, loading: authLoading } = useAuth();
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [draft, setDraft] = useState("");
  const [error, setError] = useState<unknown>(null);
  const [loading, setLoading] = useState(true);
  const [connection, setConnection] = useState<ConnectionState>("connecting");
  const bottom = useRef<HTMLDivElement>(null);

  const otherId = Number(userId);

  const loadHistory = useCallback(() => {
    api
      .get<Page<ChatMessage>>(`/chat/conversations/${otherId}?size=50`)
      .then((page) => setMessages([...page.content].reverse()))
      .catch(setError)
      .finally(() => setLoading(false));
    api.post(`/chat/conversations/${otherId}/read`).catch(() => undefined);
  }, [otherId]);

  useEffect(() => {
    if (authLoading || !user) return;
    loadHistory();
  }, [authLoading, user, loadHistory]);

  // Live delivery. Replaces the five-second poll this page used to run.
  useEffect(() => {
    if (authLoading || !user) return;

    return connectChatSocket({
      onStateChange: setConnection,
      onMessage: (incoming) => {
        // The server echoes to both parties, so filter to this conversation.
        const inThisThread =
          (incoming.senderId === otherId && incoming.recipientId === user.id) ||
          (incoming.senderId === user.id && incoming.recipientId === otherId);
        if (!inThisThread) return;

        setMessages((current) =>
          current.some((m) => m.id === incoming.id) ? current : [...current, incoming],
        );

        if (incoming.senderId === otherId) {
          api.post(`/chat/conversations/${otherId}/read`).catch(() => undefined);
        }
      },
    });
  }, [authLoading, user, otherId]);

  useEffect(() => {
    bottom.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages.length]);

  async function send(event: React.FormEvent) {
    event.preventDefault();
    if (!draft.trim()) return;
    setError(null);

    // Posted over REST even when the socket is up: the endpoint persists the
    // message and then pushes it to both parties, so there is one write path
    // and the sender still sees their own message if the socket has dropped.
    try {
      const saved = await api.post<ChatMessage>("/chat/messages", {
        recipientId: otherId,
        body: draft,
      });
      setDraft("");
      setMessages((current) =>
        current.some((m) => m.id === saved.id) ? current : [...current, saved],
      );
    } catch (e) {
      setError(e);
    }
  }

  if (authLoading || loading) return <Spinner />;
  if (!user) return <p className="py-12 text-center text-sm text-stone-500">請先登入。</p>;

  return (
    <div className="mx-auto max-w-2xl space-y-4 px-6 py-10">
      <div className="flex items-baseline gap-3">
        <h1 className="text-xl font-bold">聊天室</h1>
        <span
          className={`text-xs ${connection === "connected" ? "text-green-600" : "text-stone-400"}`}
          role="status"
        >
          {STATE_LABELS[connection]}
        </span>
      </div>

      <ErrorNote error={error} />

      <Card className="flex h-[60vh] flex-col p-4">
        <div className="flex-1 space-y-2 overflow-y-auto">
          {messages.map((message) => {
            const mine = message.senderId === user.id;
            return (
              <div key={message.id} className={`flex ${mine ? "justify-end" : "justify-start"}`}>
                <div
                  className={`max-w-[75%] rounded-2xl px-3 py-2 text-sm ${
                    mine ? "bg-pepper text-white" : "bg-stone-100"
                  }`}
                >
                  <p className="whitespace-pre-wrap">{message.body}</p>
                  <p className={`mt-0.5 text-[11px] ${mine ? "text-white/70" : "text-stone-400"}`}>
                    {new Date(message.createdAt).toLocaleTimeString("zh-TW", {
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </p>
                </div>
              </div>
            );
          })}
          <div ref={bottom} />
        </div>

        <form onSubmit={send} className="mt-3 flex gap-2 border-t border-stone-100 pt-3">
          <Input
            placeholder="輸入訊息…"
            aria-label="訊息內容"
            value={draft}
            onChange={(e) => setDraft(e.target.value)}
          />
          <Button type="submit">送出</Button>
        </form>
      </Card>
    </div>
  );
}
