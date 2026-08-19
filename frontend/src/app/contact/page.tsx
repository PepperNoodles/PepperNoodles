"use client";

import { useCallback, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Recaptcha, recaptchaEnabled } from "@/components/Recaptcha";
import { Button, Card, ErrorNote, Input } from "@/components/ui";

/** 聯絡我們 — open to logged-out visitors, so it is reCAPTCHA-gated. */
export default function ContactPage() {
  const { user } = useAuth();
  const [contactEmail, setContactEmail] = useState("");
  const [body, setBody] = useState("");
  const [recaptchaToken, setRecaptchaToken] = useState<string | null>(null);
  const [error, setError] = useState<unknown>(null);
  const [sent, setSent] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  // Stable identity, otherwise the widget re-renders on every keystroke.
  const onToken = useCallback((token: string | null) => setRecaptchaToken(token), []);

  async function onSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await api.post(
        "/inquiries",
        { contactEmail: user ? null : contactEmail, body, recaptchaToken },
        { anonymous: !user },
      );
      setSent(true);
    } catch (e) {
      setError(e);
    } finally {
      setSubmitting(false);
    }
  }

  if (sent) {
    return (
      <Card className="mx-auto my-16 max-w-md p-8 text-center">
        <h1 className="text-2xl font-bold">訊息已送出 ✓</h1>
        <p className="mt-3 text-sm text-stone-600">
          感謝您的來信，我們會盡快回覆。
        </p>
      </Card>
    );
  }

  return (
    <Card className="mx-auto my-16 max-w-lg p-8">
      <span className="font-script text-3xl text-pepper">Get in touch</span>
      <h1 className="text-2xl font-bold">聯絡我們</h1>
      <p className="mt-2 text-sm text-stone-500">
        有任何問題、建議或合作提案，都歡迎留言給我們。
      </p>

      <form onSubmit={onSubmit} className="mt-6 space-y-4">
        {!user && (
          <div>
            <label htmlFor="contactEmail" className="mb-1 block text-sm font-medium">
              聯絡信箱 <span className="text-pepper">*</span>
            </label>
            <Input
              id="contactEmail"
              type="email"
              required
              value={contactEmail}
              onChange={(e) => setContactEmail(e.target.value)}
            />
            <p className="mt-1 text-xs text-stone-500">我們需要這個信箱才能回覆您。</p>
          </div>
        )}

        <div>
          <label htmlFor="body" className="mb-1 block text-sm font-medium">
            訊息內容 <span className="text-pepper">*</span>
          </label>
          <textarea
            id="body"
            required
            rows={6}
            maxLength={2000}
            value={body}
            onChange={(e) => setBody(e.target.value)}
            className="w-full rounded-lg border border-stone-300 bg-white px-3 py-2 text-sm outline-none focus:border-pepper"
          />
          <p className="mt-1 text-right text-xs text-stone-400">{body.length} / 2000</p>
        </div>

        <Recaptcha onToken={onToken} />

        <ErrorNote error={error} />
        <Button type="submit" disabled={submitting || (recaptchaEnabled && !recaptchaToken)}>
          {submitting ? "送出中…" : "送出訊息"}
        </Button>
      </form>
    </Card>
  );
}
