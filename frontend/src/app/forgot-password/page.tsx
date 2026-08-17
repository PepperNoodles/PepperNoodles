"use client";

import Link from "next/link";
import { useState } from "react";
import { api } from "@/lib/api";
import { Button, Card, ErrorNote, Input } from "@/components/ui";

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
      <Card className="mx-auto my-16 max-w-md p-8 text-center">
        <h1 className="text-2xl font-bold">請查看信箱 📬</h1>
        <p className="mt-3 text-sm text-stone-600">
          如果 <strong>{email}</strong> 是已註冊的帳號，我們已寄出重設密碼的連結。連結一小時內有效。
        </p>
        <p className="mt-4 text-xs text-stone-500">
          開發環境可到{" "}
          <a href="http://127.0.0.1:55324" target="_blank" rel="noreferrer" className="text-pepper hover:underline">
            Mailpit
          </a>{" "}
          收信。
        </p>
        <Link href="/login" className="mt-6 inline-block text-sm text-pepper hover:underline">
          回到登入 →
        </Link>
      </Card>
    );
  }

  return (
    <Card className="mx-auto my-16 max-w-md p-8">
      <h1 className="text-2xl font-bold">忘記密碼</h1>
      <p className="mt-2 text-sm text-stone-500">輸入註冊信箱，我們會寄一封重設密碼的連結給你。</p>
      <form onSubmit={onSubmit} className="mt-6 space-y-4">
        <div>
          <label htmlFor="email" className="mb-1 block text-sm font-medium">
            電子信箱
          </label>
          <Input
            id="email"
            type="email"
            autoComplete="email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>
        <ErrorNote error={error} />
        <Button type="submit" disabled={submitting} className="w-full">
          {submitting ? "寄送中…" : "寄送重設連結"}
        </Button>
      </form>
      <p className="mt-6 text-center text-sm text-stone-500">
        想起來了？{" "}
        <Link href="/login" className="text-pepper hover:underline">
          回到登入
        </Link>
      </p>
    </Card>
  );
}
