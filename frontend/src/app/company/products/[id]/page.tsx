"use client";

import { useRouter } from "next/navigation";
import { use, useCallback, useEffect, useState } from "react";
import { api } from "@/lib/api";
import { ProductForm } from "@/components/ProductForm";
import {
  Button,
  ButtonLink,
  Card,
  ErrorNote,
  Gate,
  ImageUploadField,
  PageHeader,
  PageShell,
  Spinner,
} from "@/components/ui";
import { IconTrash } from "@/components/icons";
import type { ProductDetail } from "@/lib/types";

export default function EditProductPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params);
  const router = useRouter();
  const [product, setProduct] = useState<ProductDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [uploading, setUploading] = useState(false);
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
    setUploading(true);
    const body = new FormData();
    body.append("file", file);
    try {
      await api.post(`/shop/products/${id}/image`, body);
      load();
    } catch (e) {
      setError(e);
    } finally {
      setUploading(false);
    }
  }

  if (loading) return <Spinner />;
  if (!product) {
    return (
      <PageShell width="reading">
        <ErrorNote error={error} />
      </PageShell>
    );
  }
  if (!product.editable) {
    return (
      <Gate title="沒有編輯權限" action={<ButtonLink href="/company" variant="ghost">回到店家管理</ButtonLink>}>
        您沒有權限修改這項商品。
      </Gate>
    );
  }

  return (
    <PageShell width="reading">
      <PageHeader
        title={`編輯「${product.name}」`}
        back={{ href: "/company", label: "回到店家管理" }}
        actions={<ButtonLink href={`/shop/${id}`} variant="ghost">查看公開頁面</ButtonLink>}
      />

      <div className="space-y-12">
        <ErrorNote error={error} />

        <Card className="p-6 sm:p-7">
          <h2 className="font-display text-base font-bold text-ink">商品圖片</h2>
          <div className="mt-5">
            <ImageUploadField
              label="上傳圖片"
              inputLabel="上傳商品圖片"
              shape="square"
              imageUrl={product.imageUrl}
              emptyLabel="尚無圖片"
              uploading={uploading}
              onFile={uploadImage}
              hint="正方形照片最好看，建議 800×800 以上。"
            />
          </div>
        </Card>

        <ProductForm existing={product} />

        <Card className="border-danger/30 p-6 sm:p-7">
          <h2 className="font-display text-base font-bold text-danger">刪除商品</h2>
          <p className="measure mt-2 text-sm leading-relaxed text-body">
            下架只是隱藏商品；刪除則無法復原，且會從既有訂單的商品連結中消失。
          </p>
          <Button
            variant="danger"
            className="mt-5"
            icon={<IconTrash />}
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
    </PageShell>
  );
}
