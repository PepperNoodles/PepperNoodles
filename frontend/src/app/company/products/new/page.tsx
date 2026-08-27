"use client";

import { useAuth } from "@/components/AuthProvider";
import { ProductForm } from "@/components/ProductForm";
import { ButtonLink, Gate, PageHeader, PageShell, Spinner } from "@/components/ui";

export default function NewProductPage() {
  const { hasRole, loading } = useAuth();

  if (loading) return <Spinner />;
  if (!hasRole("ROLE_COMPANY", "ROLE_ADMIN")) {
    return (
      <Gate title="僅限企業會員" action={<ButtonLink href="/register/company">註冊企業帳號</ButtonLink>}>
        只有企業會員能上架商品。
      </Gate>
    );
  }

  return (
    <PageShell width="reading">
      <PageHeader
        kicker="New product"
        title="新增商品"
        description="商品圖片可以在建立後於編輯畫面上傳。"
        back={{ href: "/company", label: "回到店家管理" }}
      />
      <ProductForm />
    </PageShell>
  );
}
