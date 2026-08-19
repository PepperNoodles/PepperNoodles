"use client";

import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { Suspense, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { Card, Spinner } from "@/components/ui";

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

  return (
    <Card className="mx-auto my-16 max-w-md p-8 text-center">
      {state === "ok" ? (
        <>
          <h1 className="text-2xl font-bold">已取消訂閱</h1>
          <p className="mt-3 text-sm text-stone-600">
            我們不會再寄電子報給您。若是誤按，隨時可以到首頁重新訂閱。
          </p>
          <Link href="/" className="mt-6 inline-block text-pepper hover:underline">
            回首頁 →
          </Link>
        </>
      ) : (
        <>
          <h1 className="text-2xl font-bold">取消訂閱失敗</h1>
          <p className="mt-3 text-sm text-stone-600">{message}</p>
        </>
      )}
    </Card>
  );
}

export default function NewsletterUnsubscribePage() {
  return (
    <Suspense fallback={<Spinner />}>
      <Unsubscribe />
    </Suspense>
  );
}
