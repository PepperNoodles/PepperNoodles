"use client";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import {
  Button,
  ButtonLink,
  Card,
  CharCount,
  ErrorNote,
  Gate,
  PageHeader,
  PageShell,
  Spinner,
  TagPicker,
  Textarea,
} from "@/components/ui";
import type { Tag } from "@/lib/types";

export default function NewForumPostPage() {
  const { user, loading } = useAuth();
  const router = useRouter();
  const [body, setBody] = useState("");
  const [tags, setTags] = useState<Tag[]>([]);
  const [selected, setSelected] = useState<number[]>([]);
  const [error, setError] = useState<unknown>(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    api.get<Tag[]>("/food-tags", { anonymous: true }).then(setTags).catch(() => setTags([]));
  }, []);

  if (loading) return <Spinner />;
  if (!user) {
    return (
      <Gate title="請先登入" action={<ButtonLink href="/login?next=/forum/new">前往登入</ButtonLink>}>
        登入後即可發表專欄文章。
      </Gate>
    );
  }

  async function onSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await api.post("/forum/posts", { body, tagIds: selected });
      router.push("/forum");
    } catch (e) {
      setError(e);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <PageShell width="narrow">
      <PageHeader
        title="發表文章"
        description="配圖可以在文章建立後於編輯畫面加上。"
        back={{ href: "/forum", label: "回到專欄" }}
      />

      <Card className="p-6 sm:p-8">
        <form onSubmit={onSubmit} className="space-y-6">
          <div>
            <label htmlFor="body" className="mb-1.5 block text-sm font-semibold text-ink">
              內容
              <span className="ml-0.5 text-pepper-ink" aria-hidden>
                *
              </span>
            </label>
            <Textarea
              id="body"
              required
              rows={12}
              maxLength={5000}
              value={body}
              onChange={(e) => setBody(e.target.value)}
              placeholder="分享你的美食心得…"
            />
            <CharCount value={body} max={5000} />
          </div>

          <TagPicker
            legend="標籤"
            hint="選幾個相關的類型，讓讀者更容易找到這篇。"
            tags={tags}
            selected={selected}
            onChange={setSelected}
          />

          <ErrorNote error={error} />
          <div className="flex gap-3">
            <Button type="submit" loading={submitting}>
              發表
            </Button>
            <Button type="button" variant="ghost" onClick={() => router.back()}>
              取消
            </Button>
          </div>
        </form>
      </Card>
    </PageShell>
  );
}
