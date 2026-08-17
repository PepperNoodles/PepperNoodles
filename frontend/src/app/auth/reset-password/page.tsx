"use client";

import Link from "next/link";
import { useRouter, useSearchParams } from "next/navigation";
import { Suspense, useState } from "react";
import { api, ApiError } from "@/lib/api";
import { Button, Card, ErrorNote, Input, Spinner } from "@/components/ui";

function ResetForm() {
  const token = useSearchParams().get("token");
  const router = useRouter();
  const [newPassword, setNewPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [error, setError] = useState<unknown>(null);
  const [done, setDone] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const fieldErrors = error instanceof ApiError ? error.fieldErrors : {};

  async function onSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);

    // Checked here rather than server-side: the API only ever receives one password.
    if (newPassword !== confirm) {
      setError(new Error("兩次輸入的密碼不一致。"));
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
      <Card className="mx-auto my-16 max-w-md p-8 text-center">
        <h1 className="text-2xl font-bold">連結不完整</h1>
        <p className="mt-3 text-sm text-stone-600">請重新從信件中的連結進入。</p>
        <Link href="/forgot-password" className="mt-6 inline-block text-sm text-pepper hover:underline">
          重新寄送 →
        </Link>
      </Card>
    );
  }

  if (done) {
    return (
      <Card className="mx-auto my-16 max-w-md p-8 text-center">
        <h1 className="text-2xl font-bold">密碼已更新 ✓</h1>
        <p className="mt-3 text-sm text-stone-600">其他裝置的登入已全部登出，正在帶你回登入頁…</p>
      </Card>
    );
  }

  return (
    <Card className="mx-auto my-16 max-w-md p-8">
      <h1 className="text-2xl font-bold">設定新密碼</h1>
      <form onSubmit={onSubmit} className="mt-6 space-y-4">
        <div>
          <label htmlFor="newPassword" className="mb-1 block text-sm font-medium">
            新密碼
          </label>
          <Input
            id="newPassword"
            type="password"
            autoComplete="new-password"
            required
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
          <p className="mt-1 text-xs text-stone-500">至少 8 碼，需包含英文字母與數字。</p>
          {fieldErrors.newPassword && <p className="mt-1 text-xs text-pepper">{fieldErrors.newPassword}</p>}
        </div>
        <div>
          <label htmlFor="confirm" className="mb-1 block text-sm font-medium">
            再次輸入新密碼
          </label>
          <Input
            id="confirm"
            type="password"
            autoComplete="new-password"
            required
            value={confirm}
            onChange={(e) => setConfirm(e.target.value)}
          />
        </div>
        <ErrorNote error={error} />
        <Button type="submit" disabled={submitting} className="w-full">
          {submitting ? "更新中…" : "更新密碼"}
        </Button>
      </form>
    </Card>
  );
}

export default function ResetPasswordPage() {
  return (
    <Suspense fallback={<Spinner />}>
      <ResetForm />
    </Suspense>
  );
}
