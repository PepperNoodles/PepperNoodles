"use client";

import { useCallback, useState } from "react";
import { api } from "@/lib/api";
import { Recaptcha, recaptchaEnabled } from "@/components/Recaptcha";
import { Button, ErrorNote } from "./ui";

/**
 * 電子報訂閱 — the 2021 首頁 had this form, wired to nothing.
 *
 * <p>The confirmation copy is deliberately the same whether or not the address
 * was already subscribed; the endpoint is public, so saying otherwise would let
 * anyone test whether a given address is on the list.
 */
export function NewsletterSignup({ source = "home" }: { source?: string }) {
  const [email, setEmail] = useState("");
  const [recaptchaToken, setRecaptchaToken] = useState<string | null>(null);
  const [sent, setSent] = useState(false);
  const [error, setError] = useState<unknown>(null);
  const [submitting, setSubmitting] = useState(false);

  const onToken = useCallback((token: string | null) => setRecaptchaToken(token), []);

  async function onSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await api.post("/newsletter/subscribe", { email, source, recaptchaToken }, { anonymous: true });
      setSent(true);
    } catch (e) {
      setError(e);
    } finally {
      setSubmitting(false);
    }
  }

  if (sent) {
    return (
      <p className="text-center text-white/90" role="status">
        確認信已寄出，請到信箱點擊連結完成訂閱。
      </p>
    );
  }

  return (
    <form onSubmit={onSubmit} className="mx-auto max-w-xl">
      <div className="search-ring flex bg-white">
        <input
          type="email"
          required
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="你的電子信箱"
          aria-label="訂閱電子報的信箱"
          className="min-w-0 flex-1 rounded-l-full bg-transparent px-6 py-3 text-stone-800 outline-none placeholder:text-stone-400"
        />
        <button
          type="submit"
          disabled={submitting || (recaptchaEnabled && !recaptchaToken)}
          className="rounded-full bg-pepper px-8 py-3 font-display text-sm font-bold uppercase tracking-wider text-white transition hover:bg-pepper-dark disabled:opacity-60"
        >
          {submitting ? "送出中…" : "Send Now"}
        </button>
      </div>

      {recaptchaEnabled && (
        <div className="mt-4 flex justify-center">
          <Recaptcha onToken={onToken} />
        </div>
      )}

      <div className="mt-3">
        <ErrorNote error={error} />
      </div>
    </form>
  );
}
