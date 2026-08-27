"use client";

import { useRouter } from "next/navigation";
import { use, useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import {
  Button,
  ButtonLink,
  Card,
  CharCount,
  ErrorNote,
  Gate,
  ImageUploadField,
  PageHeader,
  PageShell,
  Spinner,
  TagPicker,
  Textarea,
} from "@/components/ui";
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
  if (!post) {
    return (
      <PageShell width="narrow">
        <ErrorNote error={error} />
      </PageShell>
    );
  }
  if (!post.editable) {
    return (
      <Gate title="沒有編輯權限" action={<ButtonLink href={`/forum/${id}`} variant="ghost">回到文章</ButtonLink>}>
        只有作者本人可以編輯這篇文章。
      </Gate>
    );
  }

  return (
    <PageShell width="narrow">
      <PageHeader title="編輯文章" back={{ href: `/forum/${id}`, label: "回到文章" }} />

      <div className="space-y-5">
        <ErrorNote error={error} />

        <Card className="p-6 sm:p-8">
          <h2 className="font-display text-base font-bold text-ink">配圖</h2>
          <div className="mt-5">
            <ImageUploadField
              label="上傳配圖"
              inputLabel="上傳文章配圖"
              imageUrl={post.imageUrl}
              emptyLabel="尚無配圖"
              uploading={uploading}
              onFile={uploadImage}
              hint="橫幅比例最好看，建議 1200×630 以上。"
            />
          </div>
        </Card>

        <Card className="p-6 sm:p-8">
          <form onSubmit={save} className="space-y-6">
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
              />
              <CharCount value={body} max={5000} />
            </div>

            <TagPicker legend="標籤" tags={tags} selected={selected} onChange={setSelected} />

            <div className="flex gap-3">
              <Button type="submit" loading={saving}>
                儲存變更
              </Button>
              <Button type="button" variant="ghost" onClick={() => router.back()}>
                取消
              </Button>
            </div>
          </form>
        </Card>
      </div>
    </PageShell>
  );
}
