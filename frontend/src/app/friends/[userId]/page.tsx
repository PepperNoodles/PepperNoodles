"use client";

import { use, useCallback, useEffect, useRef, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, ErrorNote, Input, Spinner } from "@/components/ui";
import type { ChatMessage, Page } from "@/lib/types";

export default function ChatPage({ params }: { params: Promise<{ userId: string }> }) {
  const { userId } = use(params);
  const { user, loading: authLoading } = useAuth();
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [draft, setDraft] = useState("");
  const [error, setError] = useState<unknown>(null);
  const [loading, setLoading] = useState(true);
  const bottom = useRef<HTMLDivElement>(null);

  const load = useCallback(() => {
    api
      .get<Page<ChatMessage>>(`/chat/conversations/${userId}?size=50`)
      .then((page) => setMessages([...page.content].reverse()))
      .catch(setError)
      .finally(() => setLoading(false));
    api.post(`/chat/conversations/${userId}/read`).catch(() => undefined);
  }, [userId]);

  useEffect(() => {
    if (authLoading || !user) return;
    load();
    // Short-poll rather than opening a socket: the STOMP endpoint exists, but
    // polling keeps this page simple and works behind any proxy.
    const timer = setInterval(load, 5000);
    return () => clearInterval(timer);
  }, [authLoading, user, load]);

  useEffect(() => {
    bottom.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages.length]);

  async function send(event: React.FormEvent) {
    event.preventDefault();
    if (!draft.trim()) return;
    setError(null);
    try {
      await api.post<ChatMessage>("/chat/messages", { recipientId: Number(userId), body: draft });
      setDraft("");
      load();
    } catch (e) {
      setError(e);
    }
  }

  if (authLoading || loading) return <Spinner />;
  if (!user) return <p className="py-12 text-center text-sm text-stone-500">請先登入。</p>;

  return (
    <div className="mx-auto max-w-2xl space-y-4">
      <h1 className="text-xl font-bold">聊天室</h1>
      <ErrorNote error={error} />

      <Card className="flex h-[60vh] flex-col p-4">
        <div className="flex-1 space-y-2 overflow-y-auto">
          {messages.map((message) => {
            const mine = message.senderId === user.id;
            return (
              <div key={message.id} className={`flex ${mine ? "justify-end" : "justify-start"}`}>
                <div
                  className={`max-w-[75%] rounded-2xl px-3 py-2 text-sm ${
                    mine ? "bg-red-600 text-white" : "bg-stone-100 dark:bg-stone-800"
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

        <form onSubmit={send} className="mt-3 flex gap-2 border-t border-stone-100 pt-3 dark:border-stone-800">
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
