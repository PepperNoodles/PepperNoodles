"use client";

import { useState } from "react";
import { api } from "@/lib/api";
import { ErrorNote } from "./ui";

/**
 * 重寄驗證信.
 *
 * <p>The API always answers 202 whether or not the address is registered, so
 * the confirmation here must not imply that an account was found.
 */
export function ResendVerification({ email: initialEmail }: { email?: string }) {
  const [email, setEmail] = useState(initialEmail ?? "");
  const [sent, setSent] = useState(false);
  const [error, setError] = useState<unknown>(null);
  const [sending, setSending] = useState(false);

  async function resend() {
    setError(null);
    setSending(true);
    try {
      await api.post("/auth/resend-verification", { email }, { anonymous: true });
      setSent(true);
    } catch (e) {
      setError(e);
    } finally {
      setSending(false);
    }
  }

  if (sent) {
    return (
      <p className="text-sm text-subtle" role="status">
        若這個信箱尚未驗證，我們已再寄一次驗證信。
      </p>
    );
  }

  return (
    <div className="space-y-2">
      {initialEmail === undefined && (
        <input
          type="email"
          required
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="註冊時使用的信箱"
          aria-label="重寄驗證信的信箱"
          className="min-h-11 w-full rounded-xl border border-line-strong bg-white px-3.5 text-sm text-ink transition placeholder:text-subtle focus:border-pepper focus:outline-none focus:ring-4 focus:ring-pepper/15"
        />
      )}
      <button
        type="button"
        onClick={resend}
        disabled={sending || !email}
        className="cursor-pointer text-sm font-medium text-pepper-ink underline-offset-2 hover:underline disabled:cursor-not-allowed disabled:opacity-50"
      >
        {sending ? "寄送中…" : "沒收到信？重寄驗證信"}
      </button>
      <ErrorNote error={error} />
    </div>
  );
}
