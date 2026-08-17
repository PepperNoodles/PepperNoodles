"use client";

import Link from "next/link";
import { useState } from "react";
import { api, ApiError } from "@/lib/api";
import { Button, Card, ErrorNote, Input } from "@/components/ui";

/** 企業會員註冊 — creates an account holding ROLE_COMPANY. */
export default function RegisterCompanyPage() {
  const [form, setForm] = useState({
    email: "",
    password: "",
    companyName: "",
    phone: "",
    location: "",
  });
  const [error, setError] = useState<unknown>(null);
  const [done, setDone] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  const fieldErrors = error instanceof ApiError ? error.fieldErrors : {};

  async function onSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await api.post("/auth/register/company", form, { anonymous: true });
      setDone(true);
    } catch (e) {
      setError(e);
    } finally {
      setSubmitting(false);
    }
  }

  if (done) {
    return (
      <Card className="mx-auto my-16 max-w-md p-8 text-center">
        <h1 className="text-2xl font-bold">確認信已寄出 📬</h1>
        <p className="mt-3 text-sm text-stone-600">
          我們寄了一封驗證信到 <strong>{form.email}</strong>。點擊信中的連結後就能登入並開始登錄餐廳。
        </p>
        <p className="mt-4 text-xs text-stone-500">
          開發環境可到{" "}
          <a href="http://127.0.0.1:55324" target="_blank" rel="noreferrer" className="text-pepper hover:underline">
            Mailpit
          </a>{" "}
          收信。
        </p>
      </Card>
    );
  }

  function field(name: keyof typeof form, label: string, type = "text", required = false, hint?: string) {
    return (
      <div>
        <label htmlFor={name} className="mb-1 block text-sm font-medium">
          {label}
          {required && <span className="text-pepper"> *</span>}
        </label>
        <Input
          id={name}
          type={type}
          required={required}
          value={form[name]}
          onChange={(e) => setForm({ ...form, [name]: e.target.value })}
        />
        {hint && !fieldErrors[name] && <p className="mt-1 text-xs text-stone-500">{hint}</p>}
        {fieldErrors[name] && <p className="mt-1 text-xs text-pepper">{fieldErrors[name]}</p>}
      </div>
    );
  }

  return (
    <Card className="mx-auto my-16 max-w-lg p-8">
      <h1 className="text-2xl font-bold">企業會員註冊</h1>
      <p className="mt-2 text-sm text-stone-500">
        註冊後即可登錄餐廳、管理菜單與營業時間、上架商品，並查看銷售報表。
      </p>
      <form onSubmit={onSubmit} className="mt-6 space-y-4">
        {field("email", "電子信箱", "email", true)}
        {field("password", "密碼", "password", true, "至少 8 碼，需包含英文字母與數字。")}
        {field("companyName", "公司 / 店家名稱", "text", true)}
        {field("phone", "聯絡電話", "tel", false, "市話或手機，例如 02-27208889 或 0912345678。")}
        {field("location", "營業地區")}
        <ErrorNote error={error} />
        <Button type="submit" disabled={submitting} className="w-full">
          {submitting ? "送出中…" : "註冊企業帳號"}
        </Button>
      </form>
      <p className="mt-6 text-center text-sm text-stone-500">
        要註冊一般會員？{" "}
        <Link href="/register" className="text-pepper hover:underline">
          走這邊
        </Link>
      </p>
    </Card>
  );
}
