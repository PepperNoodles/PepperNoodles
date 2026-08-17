"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { use, useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { ProductForm } from "@/components/ProductForm";
import { Button, Card, ErrorNote, Spinner } from "@/components/ui";
import type { ProductDetail } from "@/lib/types";

export default function EditProductPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const router = useRouter();
  const [product, setProduct] = useState<ProductDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown>(null);

  const load = useCallback(() => {
    api
      .get<ProductDetail>(`/shop/products/${id}`)
      .then(setProduct)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [id]);

  useEffect(load, [load]);

  async function uploadImage(file: File) {
    setError(null);
    const body = new FormData();
    body.append("file", file);
    try {
      await api.post(`/shop/products/${id}/image`, body);
      load();
    } catch (e) {
      setError(e);
    }
  }

  if (loading) return <Spinner />;
  if (!product) return <div className="mx-auto max-w-3xl px-6 py-10"><ErrorNote error={error} /></div>;
  if (!product.editable) {
    return <p className="py-16 text-center text-sm text-stone-500">您沒有權限修改這項商品。</p>;
  }

  return (
    <div className="mx-auto max-w-3xl space-y-6 px-6 py-10">
      <div>
        <Link href="/company" className="text-sm text-stone-500 hover:text-pepper">
          ← 回到店家管理
        </Link>
        <h1 className="mt-2 text-2xl font-bold">編輯「{product.name}」</h1>
      </div>

      <ErrorNote error={error} />

      <Card className="p-6">
        <h2 className="font-display text-lg font-bold">商品圖片</h2>
        <div className="mt-4 flex items-center gap-4">
          {product.imageUrl ? (
            /* eslint-disable-next-line @next/next/no-img-element */
            <img src={product.imageUrl} alt="" className="h-24 w-24 rounded object-cover" />
          ) : (
            <div className="flex h-24 w-24 items-center justify-center rounded bg-stone-100 text-3xl">🍜</div>
          )}
          <input
            type="file"
            accept="image/*"
            aria-label="上傳商品圖片"
            onChange={(e) => e.target.files?.[0] && uploadImage(e.target.files[0])}
            className="text-sm"
          />
        </div>
      </Card>

      <ProductForm existing={product} />

      <Card className="border-red-200 p-6">
        <h2 className="font-display text-lg font-bold text-pepper">刪除商品</h2>
        <Button
          variant="danger"
          className="mt-3"
          onClick={async () => {
            if (!confirm(`確定要刪除「${product.name}」嗎？`)) return;
            try {
              await api.delete(`/shop/products/${id}`);
              router.push("/company");
            } catch (e) {
              setError(e);
            }
          }}
        >
          刪除這項商品
        </Button>
      </Card>
    </div>
  );
}
