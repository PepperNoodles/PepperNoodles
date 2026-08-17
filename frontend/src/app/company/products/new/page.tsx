"use client";

import Link from "next/link";
import { useAuth } from "@/components/AuthProvider";
import { ProductForm } from "@/components/ProductForm";
import { Spinner } from "@/components/ui";

export default function NewProductPage() {
  const { hasRole, loading } = useAuth();
  if (loading) return <Spinner />;
  if (!hasRole("ROLE_COMPANY", "ROLE_ADMIN")) {
    return <p className="py-16 text-center text-sm text-stone-500">只有企業會員能上架商品。</p>;
  }
  return (
    <div className="mx-auto max-w-3xl px-6 py-10">
      <Link href="/company" className="text-sm text-stone-500 hover:text-pepper">
        ← 回到店家管理
      </Link>
      <h1 className="mb-6 mt-2 text-2xl font-bold">新增商品</h1>
      <ProductForm />
    </div>
  );
}
