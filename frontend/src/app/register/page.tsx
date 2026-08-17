"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api, ApiError } from "@/lib/api";
import { Button, Card, ErrorNote, Input } from "@/components/ui";
import type { Tag } from "@/lib/types";

export default function RegisterPage() {
  const [form, setForm] = useState({
    email: "",
    password: "",
    realName: "",
    nickname: "",
    phone: "",
    location: "",
  });
  const [tags, setTags] = useState<Tag[]>([]);
  const [selectedTags, setSelectedTags] = useState<number[]>([]);
  const [error, setError] = useState<unknown>(null);
  const [done, setDone] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    api.get<Tag[]>("/food-tags", { anonymous: true }).then(setTags).catch(() => setTags([]));
  }, []);

  const fieldErrors = error instanceof ApiError ? error.fieldErrors : {};

  async function onSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await api.post("/auth/register", { ...form, foodTagIds: selectedTags }, { anonymous: true });
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
        <p className="mt-3 text-sm text-stone-600 dark:text-stone-400">
          我們寄了一封驗證信到 <strong>{form.email}</strong>。點擊信中的連結後就能登入。
        </p>
        <p className="mt-4 text-xs text-stone-500">
          開發環境可到 Mailpit 收信：
          <a href="http://127.0.0.1:55324" className="text-pepper hover:underline" target="_blank" rel="noreferrer">
            127.0.0.1:55324
          </a>
        </p>
      </Card>
    );
  }

  function field(name: keyof typeof form, label: string, type = "text", required = false) {
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
        {fieldErrors[name] && <p className="mt-1 text-xs text-pepper">{fieldErrors[name]}</p>}
      </div>
    );
  }

  return (
    <Card className="mx-auto my-16 max-w-lg p-8">
      <h1 className="text-2xl font-bold">註冊會員</h1>
      <form onSubmit={onSubmit} className="mt-6 space-y-4">
        {field("email", "電子信箱", "email", true)}
        {field("password", "密碼（至少 8 碼，含英文與數字）", "password", true)}
        {field("realName", "姓名", "text", true)}
        {field("nickname", "暱稱")}
        {field("phone", "手機號碼", "tel")}
        {field("location", "居住地區")}

        <div>
          <span className="mb-2 block text-sm font-medium">興趣標籤</span>
          <div className="flex flex-wrap gap-2">
            {tags.map((tag) => {
              const active = selectedTags.includes(tag.id);
              return (
                <button
                  type="button"
                  key={tag.id}
                  aria-pressed={active}
                  onClick={() =>
                    setSelectedTags(active ? selectedTags.filter((id) => id !== tag.id) : [...selectedTags, tag.id])
                  }
                  className={`rounded-full px-3 py-1 text-xs font-medium transition ${
                    active
                      ? "bg-red-600 text-white"
                      : "bg-stone-100 text-stone-700 hover:bg-stone-200 dark:bg-stone-800 dark:text-stone-300"
                  }`}
                >
                  {tag.name}
                </button>
              );
            })}
          </div>
        </div>

        <ErrorNote error={error} />
        <Button type="submit" disabled={submitting} className="w-full">
          {submitting ? "送出中…" : "註冊"}
        </Button>
      </form>
      <p className="mt-6 text-center text-sm text-stone-500">
        已經有帳號？{" "}
        <Link href="/login" className="text-pepper hover:underline">
          登入
        </Link>
      </p>
      <p className="mt-2 text-center text-sm text-stone-500">
        要開店嗎？{" "}
        <Link href="/register/company" className="text-pepper hover:underline">
          註冊企業會員
        </Link>
      </p>
    </Card>
  );
}
