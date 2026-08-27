"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { Suspense, useState } from "react";
import { api, ApiError } from "@/lib/api";
import { Button, ButtonLink, ErrorNote, Field, Input, Spinner } from "@/components/ui";
import { AuthResult, AuthShell } from "@/components/AuthShell";

function ResetForm() {
  const token = useSearchParams().get("token");
  const router = useRouter();
  const [newPassword, setNewPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [error, setError] = useState<unknown>(null);
  const [mismatch, setMismatch] = useState(false);
  const [done, setDone] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const fieldErrors = error instanceof ApiError ? error.fieldErrors : {};

  async function onSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setMismatch(false);

    // Checked here rather than server-side: the API only ever receives one
    // password. Reported on the field itself, not as a page-level error.
    if (newPassword !== confirm) {
      setMismatch(true);
      return;
    }

    setSubmitting(true);
    try {
      await api.post("/auth/reset-password", { token, newPassword }, { anonymous: true });
      setDone(true);
      setTimeout(() => router.push("/login"), 2000);
    } catch (e) {
      setError(e);
    } finally {
      setSubmitting(false);
    }
  }

  if (!token) {
    return (
      <AuthResult
        tone="failure"
        title="連結不完整"
        action={<ButtonLink href="/forgot-password" variant="ghost">重新寄送</ButtonLink>}
      >
        <p>請重新從信件中的連結進入。</p>
      </AuthResult>
    );
  }

  if (done) {
    return (
      <AuthResult title="密碼已更新">
        <p>其他裝置的登入已全部登出，正在帶你回登入頁…</p>
      </AuthResult>
    );
  }

  return (
    <AuthShell kicker="Almost there" title="設定新密碼">
      <form onSubmit={onSubmit} className="space-y-5">
        <Field
          id="newPassword"
          label="新密碼"
          hint="至少 8 碼，需包含英文字母與數字。"
          error={fieldErrors.newPassword}
          required
        >
          {(props) => (
            <Input
              {...props}
              type="password"
              autoComplete="new-password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
            />
          )}
        </Field>

        <Field
          id="confirm"
          label="再次輸入新密碼"
          error={mismatch ? "兩次輸入的密碼不一致。" : undefined}
          required
        >
          {(props) => (
            <Input
              {...props}
              type="password"
              autoComplete="new-password"
              value={confirm}
              onChange={(e) => {
                setConfirm(e.target.value);
                setMismatch(false);
              }}
            />
          )}
        </Field>

        <ErrorNote error={error} />
        <Button type="submit" loading={submitting} size="lg" className="w-full">
          更新密碼
        </Button>
      </form>
    </AuthShell>
  );
}

export default function ResetPasswordPage() {
  return (
    <Suspense fallback={<Spinner />}>
      <ResetForm />
    </Suspense>
  );
}
