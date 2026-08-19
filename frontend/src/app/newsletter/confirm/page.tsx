"use client";

import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { Suspense, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { Card, Spinner } from "@/components/ui";

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

  return (
    <Card className="mx-auto my-16 max-w-md p-8 text-center">
      {state === "ok" ? (
        <>
          <h1 className="text-2xl font-bold">訂閱完成 ✓</h1>
          <p className="mt-3 text-sm text-stone-600">
            之後有新的餐廳、優惠與專欄文章，我們會寄給您。取消訂閱的連結在每封信裡。
          </p>
          <Link href="/" className="mt-6 inline-block text-pepper hover:underline">
            回首頁 →
          </Link>
        </>
      ) : (
        <>
          <h1 className="text-2xl font-bold">確認失敗</h1>
          <p className="mt-3 text-sm text-stone-600">{message}</p>
        </>
      )}
    </Card>
  );
}

export default function NewsletterConfirmPage() {
  return (
    <Suspense fallback={<Spinner />}>
      <Confirm />
    </Suspense>
  );
}
