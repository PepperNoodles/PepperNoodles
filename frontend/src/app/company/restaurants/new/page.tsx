"use client";

import { useAuth } from "@/components/AuthProvider";
import { RestaurantForm } from "@/components/RestaurantForm";
import { ButtonLink, Gate, PageHeader, PageShell, Spinner } from "@/components/ui";

export default function NewRestaurantPage() {
  const { hasRole, loading } = useAuth();

  if (loading) return <Spinner />;
  if (!hasRole("ROLE_COMPANY", "ROLE_ADMIN")) {
    return (
      <Gate title="僅限企業會員" action={<ButtonLink href="/register/company">註冊企業帳號</ButtonLink>}>
        只有企業會員能登錄餐廳。
      </Gate>
    );
  }

  return (
    <PageShell width="reading">
      <PageHeader
        kicker="New listing"
        title="登錄新餐廳"
        description="填好基本資料與營業時間，儲存後就能繼續上傳照片與菜單。"
        back={{ href: "/company", label: "回到店家管理" }}
      />
      <RestaurantForm />
    </PageShell>
  );
}
