"use client";

import { useSearchParams } from "next/navigation";
import { Suspense, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { ButtonLink, Spinner } from "@/components/ui";
import { AuthResult } from "@/components/AuthShell";

/** One click, no login — anything more is a dark pattern. */
function Unsubscribe() {
  const token = useSearchParams().get("token");
  const [state, setState] = useState<"pending" | "ok" | "failed">("pending");
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (!token) {
      setState("failed");
      setMessage("取消訂閱連結不完整。");
      return;
    }
    api
      .post("/newsletter/unsubscribe", { token }, { anonymous: true })
      .then(() => setState("ok"))
      .catch((e) => {
        setState("failed");
        setMessage(e instanceof Error ? e.message : "取消訂閱失敗。");
      });
  }, [token]);

  if (state === "pending") return <Spinner />;

  if (state === "ok") {
    return (
      <AuthResult title="已取消訂閱" action={<ButtonLink href="/" variant="ghost">回首頁</ButtonLink>}>
        <p>我們不會再寄電子報給您。若是誤按，隨時可以到首頁重新訂閱。</p>
      </AuthResult>
    );
  }

  return (
    <AuthResult tone="failure" title="取消訂閱失敗" action={<ButtonLink href="/" variant="ghost">回首頁</ButtonLink>}>
      <p>{message}</p>
    </AuthResult>
  );
}

export default function NewsletterUnsubscribePage() {
  return (
    <Suspense fallback={<Spinner />}>
      <Unsubscribe />
    </Suspense>
  );
}
