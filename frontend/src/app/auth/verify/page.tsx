"use client";

import { useSearchParams } from "next/navigation";
import { Suspense, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { ButtonLink, Spinner } from "@/components/ui";
import { AuthResult } from "@/components/AuthShell";

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

  if (state === "ok") {
    return (
      <AuthResult title="信箱驗證完成" action={<ButtonLink href="/login">前往登入</ButtonLink>}>
        <p>現在可以登入使用胡椒MAP 了。</p>
      </AuthResult>
    );
  }

  return (
    <AuthResult
      tone="failure"
      title="驗證失敗"
      action={<ButtonLink href="/login" variant="ghost">回到登入</ButtonLink>}
    >
      <p>{message}</p>
    </AuthResult>
  );
}

export default function VerifyPage() {
  return (
    <Suspense fallback={<Spinner />}>
      <Verify />
    </Suspense>
  );
}
