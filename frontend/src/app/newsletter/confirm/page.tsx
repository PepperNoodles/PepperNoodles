"use client";

import { useSearchParams } from "next/navigation";
import { Suspense, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { ButtonLink, Spinner } from "@/components/ui";
import { AuthResult } from "@/components/AuthShell";

function Confirm() {
  const token = useSearchParams().get("token");
  const [state, setState] = useState<"pending" | "ok" | "failed">("pending");
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (!token) {
      setState("failed");
      setMessage("確認連結不完整。");
      return;
    }
    api
      .post("/newsletter/confirm", { token }, { anonymous: true })
      .then(() => setState("ok"))
      .catch((e) => {
        setState("failed");
        setMessage(e instanceof Error ? e.message : "確認失敗。");
      });
  }, [token]);

  if (state === "pending") return <Spinner />;

  if (state === "ok") {
    return (
      <AuthResult title="訂閱完成" action={<ButtonLink href="/">回首頁</ButtonLink>}>
        <p>之後有新的餐廳、優惠與專欄文章，我們會寄給您。取消訂閱的連結在每封信裡。</p>
      </AuthResult>
    );
  }

  return (
    <AuthResult tone="failure" title="確認失敗" action={<ButtonLink href="/" variant="ghost">回首頁</ButtonLink>}>
      <p>{message}</p>
    </AuthResult>
  );
}

export default function NewsletterConfirmPage() {
  return (
    <Suspense fallback={<Spinner />}>
      <Confirm />
    </Suspense>
  );
}
