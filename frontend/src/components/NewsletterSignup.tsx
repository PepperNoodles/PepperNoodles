"use client";

import { useCallback, useState } from "react";
import { api } from "@/lib/api";
import { Recaptcha, recaptchaEnabled } from "@/components/Recaptcha";
import { ErrorNote } from "./ui";
import { IconCheckCircle, IconMail } from "./icons";

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
      <p className="flex items-center justify-center gap-2 text-center text-white/90" role="status">
        <IconCheckCircle aria-hidden className="text-xl text-mint" />
        確認信已寄出，請到信箱點擊連結完成訂閱。
      </p>
    );
  }

  return (
    <form onSubmit={onSubmit} className="mx-auto max-w-xl">
      {/* Stacks below `sm`: an input and a button on one 375px row leaves the
          input too narrow to show a whole address. */}
      <div className="search-ring flex flex-col gap-2 bg-white p-2 sm:flex-row sm:items-center sm:gap-0 sm:p-0">
        <label htmlFor="newsletter-email" className="sr-only">
          訂閱電子報的信箱
        </label>
        <div className="flex min-w-0 flex-1 items-center gap-2 px-4 sm:px-6">
          <IconMail aria-hidden className="shrink-0 text-xl text-subtle" />
          <input
            id="newsletter-email"
            type="email"
            inputMode="email"
            autoComplete="email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="你的電子信箱"
            className="min-w-0 flex-1 bg-transparent py-3 text-[15px] text-ink outline-none placeholder:text-subtle"
          />
        </div>
        <button
          type="submit"
          disabled={submitting || (recaptchaEnabled && !recaptchaToken)}
          className="min-h-12 cursor-pointer rounded-full bg-pepper-fill px-8 font-display text-sm font-bold uppercase tracking-wider text-white transition hover:bg-pepper-dark disabled:cursor-not-allowed disabled:opacity-50"
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
