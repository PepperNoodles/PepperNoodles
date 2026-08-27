"use client";

import { use, useCallback, useEffect, useRef, useState } from "react";
import { api } from "@/lib/api";
import { connectChatSocket, type ConnectionState } from "@/lib/chatSocket";
import { useAuth } from "@/components/AuthProvider";
import {
  Button,
  ButtonLink,
  Card,
  ErrorNote,
  Gate,
  Input,
  PageShell,
  Spinner,
} from "@/components/ui";
import { IconArrowLeft, IconMessage, IconSend } from "@/components/icons";
import Link from "next/link";
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
  if (!user) {
    return (
      <Gate title="請先登入" action={<ButtonLink href={`/login?next=/friends/${userId}`}>前往登入</ButtonLink>}>
        登入後即可與好友聊天。
      </Gate>
    );
  }

  return (
    <PageShell width="narrow">
      <Link
        href="/friends"
        className="-ml-2 mb-3 inline-flex min-h-11 items-center gap-1.5 rounded-lg px-2 text-sm font-medium text-subtle transition hover:bg-mist hover:text-pepper-ink sm:-ml-0 sm:mb-5 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
      >
        <IconArrowLeft className="text-base" />
        回到好友列表
      </Link>

      <div className="mb-4 flex flex-wrap items-baseline gap-3">
        <h1 className="font-display text-2xl font-bold tracking-tight text-ink">聊天室</h1>
        {/*
          Connection state carries a dot as well as a colour — "connected" and
          "disconnected" must not be distinguishable by hue alone.
        */}
        <span className="flex items-center gap-1.5 text-xs font-medium" role="status">
          <span
            aria-hidden
            className={`h-2 w-2 rounded-full ${
              connection === "connected"
                ? "bg-success"
                : connection === "connecting"
                  ? "bg-warn"
                  : "bg-faint"
            }`}
          />
          <span className={connection === "connected" ? "text-success" : "text-subtle"}>
            {STATE_LABELS[connection]}
          </span>
        </span>
      </div>

      <ErrorNote error={error} />

      <Card className="mt-4 flex h-[min(60vh,34rem)] flex-col overflow-hidden">
        <div className="flex-1 space-y-2.5 overflow-y-auto p-5" aria-live="polite" aria-label="訊息紀錄">
          {messages.length === 0 && (
            <p className="flex h-full flex-col items-center justify-center gap-2 text-center text-sm text-subtle">
              <IconMessage aria-hidden className="text-3xl text-line-strong" />
              還沒有訊息，說聲哈囉吧。
            </p>
          )}

          {messages.map((message) => {
            const mine = message.senderId === user.id;
            return (
              <div key={message.id} className={`flex ${mine ? "justify-end" : "justify-start"}`}>
                <div
                  className={`max-w-[78%] rounded-2xl px-4 py-2.5 text-sm ${
                    mine
                      ? "rounded-br-md bg-pepper-fill text-white"
                      : "rounded-bl-md border border-line bg-mist text-body"
                  }`}
                >
                  <p className="whitespace-pre-wrap leading-relaxed">{message.body}</p>
                  <p className={`mt-1 text-xs tabular ${mine ? "text-white/70" : "text-subtle"}`}>
                    <time dateTime={message.createdAt}>
                      {new Date(message.createdAt).toLocaleTimeString("zh-TW", {
                        hour: "2-digit",
                        minute: "2-digit",
                      })}
                    </time>
                  </p>
                </div>
              </div>
            );
          })}
          <div ref={bottom} />
        </div>

        <form onSubmit={send} className="flex gap-2.5 border-t border-line bg-white p-4">
          <Input
            placeholder="輸入訊息…"
            aria-label="訊息內容"
            value={draft}
            onChange={(e) => setDraft(e.target.value)}
          />
          <Button type="submit" disabled={!draft.trim()} icon={<IconSend />}>
            送出
          </Button>
        </form>
      </Card>
    </PageShell>
  );
}
