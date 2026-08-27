"use client";

import { useEffect, useState } from "react";
import { api, ApiError } from "@/lib/api";
import { Button, ErrorNote, Field, FilterChip, Input } from "@/components/ui";
import { AuthFooterLink, AuthResult, AuthShell, MailpitHint } from "@/components/AuthShell";
import { ResendVerification } from "@/components/ResendVerification";
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
      <AuthResult title="確認信已寄出">
        <p>
          我們寄了一封驗證信到 <strong className="font-semibold text-ink">{form.email}</strong>。
          點擊信中的連結後就能登入。
        </p>
        <div className="mt-5">
          <ResendVerification email={form.email} />
        </div>
        <MailpitHint />
      </AuthResult>
    );
  }

  function field(
    name: keyof typeof form,
    label: string,
    options: { type?: string; required?: boolean; hint?: string; autoComplete?: string } = {},
  ) {
    const { type = "text", required = false, hint, autoComplete } = options;
    return (
      <Field id={name} label={label} required={required} hint={hint} error={fieldErrors[name]}>
        {(props) => (
          <Input
            {...props}
            type={type}
            autoComplete={autoComplete}
            value={form[name]}
            onChange={(e) => setForm({ ...form, [name]: e.target.value })}
          />
        )}
      </Field>
    );
  }

  return (
    <AuthShell
      width="lg"
      kicker="Join the table"
      title="註冊會員"
      description="建立帳號即可收藏餐廳、發表評論、加好友並在商城下單。"
      footer={
        <>
          已經有帳號？ <AuthFooterLink href="/login">登入</AuthFooterLink>
          <span aria-hidden className="mx-2 text-subtle">·</span>
          要開店嗎？ <AuthFooterLink href="/register/company">註冊企業會員</AuthFooterLink>
        </>
      }
    >
      <form onSubmit={onSubmit} className="space-y-5">
        {field("email", "電子信箱", { type: "email", required: true, autoComplete: "email" })}
        {field("password", "密碼", {
          type: "password",
          required: true,
          hint: "至少 8 碼，需包含英文字母與數字。",
          autoComplete: "new-password",
        })}
        {field("realName", "姓名", { required: true, autoComplete: "name" })}

        <div className="grid gap-5 sm:grid-cols-2">
          {field("nickname", "暱稱", { hint: "其他會員看到的名稱。", autoComplete: "nickname" })}
          {field("phone", "手機號碼", { type: "tel", autoComplete: "tel" })}
        </div>
        {field("location", "居住地區")}

        <fieldset>
          <legend className="mb-2.5 text-sm font-semibold text-ink">興趣標籤</legend>
          <p className="mb-3 text-[13px] text-subtle">選幾個喜歡的類型，之後推薦會更準。</p>
          <div className="flex flex-wrap gap-2">
            {tags.map((tag) => {
              const active = selectedTags.includes(tag.id);
              return (
                <FilterChip
                  key={tag.id}
                  active={active}
                  onClick={() =>
                    setSelectedTags(
                      active ? selectedTags.filter((id) => id !== tag.id) : [...selectedTags, tag.id],
                    )
                  }
                >
                  {tag.name}
                </FilterChip>
              );
            })}
          </div>
        </fieldset>

        <ErrorNote error={error} />
        <Button type="submit" loading={submitting} size="lg" className="w-full">
          註冊
        </Button>
      </form>
    </AuthShell>
  );
}
