"use client";

import { useState } from "react";
import { api, ApiError } from "@/lib/api";
import { Button, ErrorNote, Field, Input } from "@/components/ui";
import { AuthFooterLink, AuthResult, AuthShell, MailpitHint } from "@/components/AuthShell";
import { IconChart, IconPackage, IconStore } from "@/components/icons";

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
      <AuthResult title="確認信已寄出">
        <p>
          我們寄了一封驗證信到 <strong className="font-semibold text-ink">{form.email}</strong>。
          點擊信中的連結後就能登入並開始登錄餐廳。
        </p>
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
      kicker="For restaurants"
      title="企業會員註冊"
      footer={
        <>
          要註冊一般會員？ <AuthFooterLink href="/register">走這邊</AuthFooterLink>
        </>
      }
    >
      {/* What the account unlocks, up front — the reason to fill the form in. */}
      <ul className="mb-7 space-y-3 rounded-2xl border border-line bg-mist p-5">
        {[
          [<IconStore key="s" />, "登錄餐廳，管理菜單與營業時間"],
          [<IconPackage key="p" />, "上架商品，接收線上訂單"],
          [<IconChart key="c" />, "查看每月營收與熱銷排行"],
        ].map(([icon, text]) => (
          <li key={String(text)} className="flex items-center gap-3 text-sm text-body">
            <span aria-hidden className="text-lg text-pepper">
              {icon}
            </span>
            {text}
          </li>
        ))}
      </ul>

      <form onSubmit={onSubmit} className="space-y-5">
        {field("email", "電子信箱", { type: "email", required: true, autoComplete: "email" })}
        {field("password", "密碼", {
          type: "password",
          required: true,
          hint: "至少 8 碼，需包含英文字母與數字。",
          autoComplete: "new-password",
        })}
        {field("companyName", "公司 / 店家名稱", { required: true, autoComplete: "organization" })}
        {field("phone", "聯絡電話", {
          type: "tel",
          hint: "市話或手機，例如 02-27208889 或 0912345678。",
          autoComplete: "tel",
        })}
        {field("location", "營業地區")}

        <ErrorNote error={error} />
        <Button type="submit" loading={submitting} size="lg" className="w-full">
          註冊企業帳號
        </Button>
      </form>
    </AuthShell>
  );
}
