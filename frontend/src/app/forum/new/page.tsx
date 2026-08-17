"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Button, Card, ErrorNote, Spinner } from "@/components/ui";
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
      <p className="py-16 text-center text-sm text-stone-500">
        請先{" "}
        <Link href="/login?next=/forum/new" className="text-pepper hover:underline">
          登入
        </Link>
        。
      </p>
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
    <div className="mx-auto max-w-2xl px-6 py-10">
      <Link href="/forum" className="text-sm text-stone-500 hover:text-pepper">
        ← 回到專欄
      </Link>
      <h1 className="mb-6 mt-2 text-2xl font-bold">發表文章</h1>

      <Card className="p-6">
        <form onSubmit={onSubmit} className="space-y-4">
          <div>
            <label htmlFor="body" className="mb-1 block text-sm font-medium">
              內容
            </label>
            <textarea
              id="body"
              required
              rows={10}
              maxLength={5000}
              value={body}
              onChange={(e) => setBody(e.target.value)}
              placeholder="分享你的美食心得…"
              className="w-full rounded-lg border border-stone-300 bg-white px-3 py-2 text-sm outline-none focus:border-pepper"
            />
            <p className="mt-1 text-right text-xs text-stone-400">{body.length} / 5000</p>
          </div>

          <div>
            <span className="mb-2 block text-sm font-medium">標籤</span>
            <div className="flex flex-wrap gap-2">
              {tags.map((tag) => {
                const active = selected.includes(tag.id);
                return (
                  <button
                    type="button"
                    key={tag.id}
                    aria-pressed={active}
                    onClick={() =>
                      setSelected(active ? selected.filter((i) => i !== tag.id) : [...selected, tag.id])
                    }
                    className={`rounded-full px-3 py-1 text-xs font-medium transition ${
                      active ? "bg-pepper text-white" : "bg-stone-100 text-stone-700 hover:bg-stone-200"
                    }`}
                  >
                    {tag.name}
                  </button>
                );
              })}
            </div>
          </div>

          <ErrorNote error={error} />
          <Button type="submit" disabled={submitting}>
            {submitting ? "發表中…" : "發表"}
          </Button>
        </form>
      </Card>
    </div>
  );
}
