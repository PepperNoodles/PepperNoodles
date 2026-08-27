"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { Suspense, useState } from "react";
import { useAuth } from "@/components/AuthProvider";
import { Button, ErrorNote, Field, Input } from "@/components/ui";
import { AuthFooterLink, AuthShell } from "@/components/AuthShell";
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
    <AuthShell
      kicker="Welcome back"
      title="登入"
      description="登入後即可收藏餐廳、留下評論並購買店家商品。"
      footer={
        <>
          還沒有帳號？ <AuthFooterLink href="/register">註冊一個</AuthFooterLink>
          <span aria-hidden className="mx-2 text-subtle">·</span>
          <AuthFooterLink href="/register/company">企業註冊</AuthFooterLink>
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

        <Field id="password" label="密碼" required>
          {(props) => (
            <Input
              {...props}
              type="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          )}
        </Field>

        <ErrorNote error={error} />
        {/* Shown only after a failure — an unverified account is the most
            common reason a correct password still cannot sign in. */}
        {error != null && <ResendVerification email={email || undefined} />}

        <Button type="submit" loading={submitting} size="lg" className="w-full">
          登入
        </Button>
      </form>

      <p className="mt-5 text-center text-sm">
        <AuthFooterLink href="/forgot-password">忘記密碼？</AuthFooterLink>
      </p>
    </AuthShell>
  );
}

export default function LoginPage() {
  return (
    <Suspense fallback={null}>
      <LoginForm />
    </Suspense>
  );
}
