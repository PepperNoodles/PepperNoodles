"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { use, useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { Button, Card, ErrorNote, Spinner } from "@/components/ui";
import type { ForumPostDetail, Tag } from "@/lib/types";

export default function EditForumPostPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const router = useRouter();
  const [post, setPost] = useState<ForumPostDetail | null>(null);
  const [body, setBody] = useState("");
  const [tags, setTags] = useState<Tag[]>([]);
  const [selected, setSelected] = useState<number[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState<unknown>(null);

  const load = useCallback(() => {
    Promise.all([
      api.get<ForumPostDetail>(`/forum/posts/${id}`),
      api.get<Tag[]>("/food-tags", { anonymous: true }),
    ])
      .then(([detail, allTags]) => {
        setPost(detail);
        setBody(detail.body);
        setSelected(detail.tags.map((t) => t.id));
        setTags(allTags);
      })
      .catch(setError)
      .finally(() => setLoading(false));
  }, [id]);

  useEffect(load, [load]);

  async function save(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    setSaving(true);
    try {
      await api.put(`/forum/posts/${id}`, { body, tagIds: selected });
      router.push(`/forum/${id}`);
    } catch (e) {
      setError(e);
    } finally {
      setSaving(false);
    }
  }

  async function uploadImage(file: File) {
    setError(null);
    setUploading(true);
    try {
      const form = new FormData();
      form.append("file", file);
      await api.post(`/forum/posts/${id}/image`, form);
      load();
    } catch (e) {
      setError(e);
    } finally {
      setUploading(false);
    }
  }

  if (loading) return <Spinner />;
  if (!post) return <div className="mx-auto max-w-2xl px-6 py-10"><ErrorNote error={error} /></div>;
  if (!post.editable) {
    return <p className="py-16 text-center text-sm text-stone-500">您沒有權限編輯這篇文章。</p>;
  }

  return (
    <div className="mx-auto max-w-2xl space-y-6 px-6 py-10">
      <div>
        <Link href={`/forum/${id}`} className="text-sm text-stone-500 hover:text-pepper">
          ← 回到文章
        </Link>
        <h1 className="mt-2 text-2xl font-bold">編輯文章</h1>
      </div>

      <ErrorNote error={error} />

      <Card className="p-6">
        <h2 className="font-display text-lg font-bold">配圖</h2>
        <div className="mt-4 flex flex-wrap items-center gap-4">
          {post.imageUrl ? (
            /* eslint-disable-next-line @next/next/no-img-element */
            <img src={post.imageUrl} alt="" className="h-28 w-40 rounded object-cover" />
          ) : (
            <div className="flex h-28 w-40 items-center justify-center rounded bg-stone-100 text-sm text-stone-400">
              尚無配圖
            </div>
          )}
          <div>
            <input
              type="file"
              accept="image/*"
              aria-label="上傳文章配圖"
              disabled={uploading}
              onChange={(e) => e.target.files?.[0] && uploadImage(e.target.files[0])}
              className="text-sm"
            />
            {uploading && <p className="mt-2 text-xs text-stone-500">上傳中…</p>}
          </div>
        </div>
      </Card>

      <Card className="p-6">
        <form onSubmit={save} className="space-y-4">
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

          <div className="flex gap-3">
            <Button type="submit" disabled={saving}>
              {saving ? "儲存中…" : "儲存變更"}
            </Button>
            <Button type="button" variant="ghost" onClick={() => router.back()}>
              取消
            </Button>
          </div>
        </form>
      </Card>
    </div>
  );
}
