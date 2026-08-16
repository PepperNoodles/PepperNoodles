"use client";

import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { Suspense, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { Card, Spinner } from "@/components/ui";

function Verify() {
  const token = useSearchParams().get("token");
  const [state, setState] = useState<"pending" | "ok" | "failed">("pending");
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (!token) {
      setState("failed");
      setMessage("驗證連結不完整。");
      return;
    }
    api
      .post("/auth/verify-email", { token }, { anonymous: true })
      .then(() => setState("ok"))
      .catch((e) => {
        setState("failed");
        setMessage(e instanceof Error ? e.message : "驗證失敗。");
      });
  }, [token]);

  if (state === "pending") return <Spinner />;

  return (
    <Card className="mx-auto max-w-md p-8 text-center">
      {state === "ok" ? (
        <>
          <h1 className="text-2xl font-bold">信箱驗證完成 ✓</h1>
          <p className="mt-3 text-sm text-stone-600 dark:text-stone-400">現在可以登入使用胡椒MAP 了。</p>
          <Link href="/login" className="mt-6 inline-block text-red-600 hover:underline">
            前往登入 →
          </Link>
        </>
      ) : (
        <>
          <h1 className="text-2xl font-bold">驗證失敗</h1>
          <p className="mt-3 text-sm text-stone-600 dark:text-stone-400">{message}</p>
        </>
      )}
    </Card>
  );
}

export default function VerifyPage() {
  return (
    <Suspense fallback={<Spinner />}>
      <Verify />
    </Suspense>
  );
}
