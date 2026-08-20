import { Client, type IMessage } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { tokenStore } from "./api";
import type { ChatMessage } from "./types";

/** Derived from the API base so one env var configures both. */
function socketUrl() {
  const api = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080/api/v1";
  return api.replace(/\/api\/v1\/?$/, "") + "/ws";
}

export type ConnectionState = "connecting" | "connected" | "disconnected";

export interface ChatSocketHandlers {
  onMessage: (message: ChatMessage) => void;
  onStateChange?: (state: ConnectionState) => void;
}

/**
 * Live chat over STOMP.
 *
 * <p>The browser cannot set an Authorization header on a WebSocket upgrade, so
 * the access token travels on the STOMP CONNECT frame — which is exactly what
 * the backend's channel interceptor reads. Each session subscribes only to its
 * own `/user/queue/messages`, so the broker does the fan-out and one member
 * cannot listen in on another's conversation.
 *
 * <p>Returns a disposer. Callers keep polling as a fallback only if they want
 * to; reconnection is handled here.
 */
export function connectChatSocket(handlers: ChatSocketHandlers) {
  const token = tokenStore.access;
  if (!token) return () => {};

  const client = new Client({
    // SockJS rather than a raw WebSocket: it falls back to HTTP streaming
    // through proxies that block upgrades.
    webSocketFactory: () => new SockJS(socketUrl()) as unknown as WebSocket,
    connectHeaders: { Authorization: `Bearer ${token}` },
    reconnectDelay: 3000,
    heartbeatIncoming: 10_000,
    heartbeatOutgoing: 10_000,
    // The library logs every frame at debug by default; too noisy for a console.
    debug: () => {},
  });

  client.onConnect = () => {
    handlers.onStateChange?.("connected");
    client.subscribe("/user/queue/messages", (frame: IMessage) => {
      try {
        handlers.onMessage(JSON.parse(frame.body) as ChatMessage);
      } catch {
        // A frame we cannot parse is not worth tearing the socket down for.
      }
    });
  };

  client.onWebSocketClose = () => handlers.onStateChange?.("disconnected");
  client.onStompError = () => handlers.onStateChange?.("disconnected");

  handlers.onStateChange?.("connecting");
  client.activate();

  return () => {
    void client.deactivate();
  };
}

/** Sends over the socket when it is up; the caller falls back to REST otherwise. */
export function sendOverSocket(client: Client | null, recipientId: number, body: string) {
  if (!client?.connected) return false;
  client.publish({
    destination: "/app/chat.send",
    body: JSON.stringify({ recipientId, body }),
  });
  return true;
}
