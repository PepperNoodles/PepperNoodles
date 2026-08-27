"use client";

import { useState } from "react";
import { api } from "@/lib/api";
import { Button, ButtonLink, ErrorNote, Field, Input } from "@/components/ui";
import { AuthFooterLink, AuthResult, AuthShell, MailpitHint } from "@/components/AuthShell";

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState("");
  const [sent, setSent] = useState(false);
  const [error, setError] = useState<unknown>(null);
  const [submitting, setSubmitting] = useState(false);

  async function onSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await api.post("/auth/forgot-password", { email }, { anonymous: true });
      setSent(true);
    } catch (e) {
      setError(e);
    } finally {
      setSubmitting(false);
    }
  }

  // The API answers identically whether or not the address exists, so the
  // confirmation must not imply that an account was found.
  if (sent) {
    return (
      <AuthResult
        title="請查看信箱"
        action={<ButtonLink href="/login" variant="ghost">回到登入</ButtonLink>}
      >
        <p>
          如果 <strong className="font-semibold text-ink">{email}</strong> 是已註冊的帳號，
          我們已寄出重設密碼的連結。連結一小時內有效。
        </p>
        <MailpitHint />
      </AuthResult>
    );
  }

  return (
    <AuthShell
      kicker="Forgot it?"
      title="忘記密碼"
      description="輸入註冊信箱，我們會寄一封重設密碼的連結給你。"
      footer={
        <>
          想起來了？ <AuthFooterLink href="/login">回到登入</AuthFooterLink>
        </>
      }
    >
      <form onSubmit={onSubmit} className="space-y-5">
        <Field id="email" label="電子信箱" required>
          {(props) => (
            <Input
              {...props}
              type="email"
              inputMode="email"
              autoComplete="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          )}
        </Field>
        <ErrorNote error={error} />
        <Button type="submit" loading={submitting} size="lg" className="w-full">
          寄送重設連結
        </Button>
      </form>
    </AuthShell>
  );
}
