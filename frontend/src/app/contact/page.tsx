"use client";

import { useCallback, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Recaptcha, recaptchaEnabled } from "@/components/Recaptcha";
import { Button, ButtonLink, CharCount, ErrorNote, Field, Input, Textarea } from "@/components/ui";
import { AuthResult, AuthShell } from "@/components/AuthShell";

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
      <AuthResult title="訊息已送出" action={<ButtonLink href="/" variant="ghost">回首頁</ButtonLink>}>
        <p>感謝您的來信，我們會盡快回覆。</p>
      </AuthResult>
    );
  }

  return (
    <AuthShell
      width="lg"
      kicker="Get in touch"
      title="聯絡我們"
      description="有任何問題、建議或合作提案，都歡迎留言給我們。"
    >
      <form onSubmit={onSubmit} className="space-y-5">
        {!user && (
          <Field
            id="contactEmail"
            label="聯絡信箱"
            hint="我們需要這個信箱才能回覆您。"
            required
          >
            {(props) => (
              <Input
                {...props}
                type="email"
                inputMode="email"
                autoComplete="email"
                value={contactEmail}
                onChange={(e) => setContactEmail(e.target.value)}
              />
            )}
          </Field>
        )}

        <div>
          <Field id="body" label="訊息內容" required>
            {(props) => (
              <Textarea
                {...props}
                rows={7}
                maxLength={2000}
                value={body}
                onChange={(e) => setBody(e.target.value)}
                placeholder="想告訴我們什麼？"
              />
            )}
          </Field>
          <CharCount value={body} max={2000} />
        </div>

        <Recaptcha onToken={onToken} />

        <ErrorNote error={error} />
        <Button
          type="submit"
          loading={submitting}
          disabled={recaptchaEnabled && !recaptchaToken}
          size="lg"
          className="w-full"
        >
          送出訊息
        </Button>
      </form>
    </AuthShell>
  );
}
