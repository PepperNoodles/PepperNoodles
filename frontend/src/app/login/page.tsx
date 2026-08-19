"use client";

import Link from "next/link";
import { useRouter, useSearchParams } from "next/navigation";
import { Suspense, useState } from "react";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, ErrorNote, Input } from "@/components/ui";
import { ResendVerification } from "@/components/ResendVerification";

function LoginForm() {
  const { login } = useAuth();
  const router = useRouter();
  const params = useSearchParams();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<unknown>(null);
  const [submitting, setSubmitting] = useState(false);

  async function onSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await login(email, password);
      router.push(params.get("next") ?? "/");
    } catch (e) {
      setError(e);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <Card className="mx-auto my-16 max-w-md p-8">
      <h1 className="text-2xl font-bold">登入</h1>
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
        <div>
          <label htmlFor="password" className="mb-1 block text-sm font-medium">
            密碼
          </label>
          <Input
            id="password"
            type="password"
            autoComplete="current-password"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <ErrorNote error={error} />
        {/* Shown only after a failure — an unverified account is the most
            common reason a correct password still cannot sign in. */}
        {error != null && <ResendVerification email={email || undefined} />}
        <Button type="submit" disabled={submitting} className="w-full">
          {submitting ? "登入中…" : "登入"}
        </Button>
      </form>
      <p className="mt-4 text-center text-sm">
        <Link href="/forgot-password" className="text-stone-500 hover:text-pepper hover:underline">
          忘記密碼？
        </Link>
      </p>
      <p className="mt-2 text-center text-sm text-stone-500">
        還沒有帳號？{" "}
        <Link href="/register" className="text-pepper hover:underline">
          註冊一個
        </Link>
        {" · "}
        <Link href="/register/company" className="text-pepper hover:underline">
          企業註冊
        </Link>
      </p>
    </Card>
  );
}

export default function LoginPage() {
  return (
    <Suspense fallback={null}>
      <LoginForm />
    </Suspense>
  );
}
