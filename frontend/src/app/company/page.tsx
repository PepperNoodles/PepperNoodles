"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import {
  Badge,
  ButtonLink,
  Card,
  Empty,
  ErrorNote,
  Gate,
  PageHeader,
  PageShell,
  SectionHeader,
  DataTable,
  Spinner,
  Stars,
  money,
} from "@/components/ui";
import { IconArrowRight, IconChart, IconPackage, IconPlus, IconStore } from "@/components/icons";
import type { Page, Product, RestaurantSummary } from "@/lib/types";

export default function CompanyPage() {
  const { hasRole, loading: authLoading } = useAuth();
  const [restaurants, setRestaurants] = useState<RestaurantSummary[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [error, setError] = useState<unknown>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (authLoading) return;
    if (!hasRole("ROLE_COMPANY", "ROLE_ADMIN")) {
      setLoading(false);
      return;
    }
    api
      .get<Page<RestaurantSummary>>("/users/me/restaurants")
      .then(async (page) => {
        setRestaurants(page.content);
        const lists = await Promise.all(
          page.content.map((r) =>
            api
              .get<Page<Product>>(`/shop/products?restaurantId=${r.id}&size=50`, { anonymous: true })
              .then((p) => p.content)
              .catch(() => []),
          ),
        );
        setProducts(lists.flat());
      })
      .catch(setError)
      .finally(() => setLoading(false));
  }, [authLoading, hasRole]);

  if (authLoading || loading) return <Spinner />;
  if (!hasRole("ROLE_COMPANY", "ROLE_ADMIN")) {
    return (
      <Gate title="僅限企業會員" action={<ButtonLink href="/register/company">註冊企業帳號</ButtonLink>}>
        這個頁面只有企業會員能存取。
      </Gate>
    );
  }

  return (
    <PageShell>
      <PageHeader
        kicker="Merchant"
        title="店家管理"
        actions={
          <>
            <ButtonLink href="/company/reports" variant="ghost" icon={<IconChart />}>
              銷售報表
            </ButtonLink>
            <ButtonLink href="/company/products/new" variant="ghost" icon={<IconPlus />}>
              新增商品
            </ButtonLink>
            <ButtonLink href="/company/restaurants/new" icon={<IconStore />}>
              登錄餐廳
            </ButtonLink>
          </>
        }
      />

      <div className="space-y-12">
        <ErrorNote error={error} />

        {/* ---------- Restaurants ---------- */}
        <section>
          <SectionHeader title="我的餐廳" count={restaurants.length} />
          {restaurants.length === 0 ? (
            <Empty
              icon={<IconStore />}
              action={<ButtonLink href="/company/restaurants/new">登錄第一間餐廳</ButtonLink>}
            >
              還沒有登記任何餐廳。登錄後就能管理菜單、營業時間與活動。
            </Empty>
          ) : (
            <ul className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
              {restaurants.map((restaurant) => (
                <li key={restaurant.id} className="flex">
                  <Link href={`/company/restaurants/${restaurant.id}`} className="group w-full">
                    <Card interactive className="flex h-full flex-col p-5">
                      <h3 className="font-display text-base font-bold text-ink transition group-hover:text-pepper-ink">
                        {restaurant.name}
                      </h3>
                      <p className="mt-1.5 text-sm text-subtle">{restaurant.address}</p>
                      <div className="mt-3">
                        <Stars average={restaurant.rating.average} count={restaurant.rating.reviewCount} />
                      </div>
                      <p className="mt-auto inline-flex items-center gap-1.5 pt-4 text-[13px] font-semibold text-pepper-ink">
                        管理這間餐廳
                        <IconArrowRight aria-hidden className="text-base" />
                      </p>
                    </Card>
                  </Link>
                </li>
              ))}
            </ul>
          )}
        </section>

        {/* ---------- Products ---------- */}
        <section>
          <SectionHeader title="我的商品" count={products.length} />
          {products.length === 0 ? (
            <Empty
              icon={<IconPackage />}
              action={<ButtonLink href="/company/products/new">新增第一件商品</ButtonLink>}
            >
              還沒有上架任何商品。
            </Empty>
          ) : (
            <DataTable
              caption="我的商品列表"
              rows={products}
              rowKey={(p) => p.id}
              columns={[
                {
                  key: "name",
                  header: "商品",
                  primary: true,
                  cell: (p) => (
                    <Link
                      href={`/company/products/${p.id}`}
                      className="-mx-2 inline-flex min-h-11 items-center rounded-lg px-2 font-semibold text-ink transition hover:bg-mist hover:text-pepper-ink sm:mx-0 sm:min-h-0 sm:px-0 sm:hover:bg-transparent sm:hover:underline sm:underline-offset-2"
                    >
                      {p.name}
                    </Link>
                  ),
                },
                { key: "restaurant", header: "餐廳", cell: (p) => <span className="text-subtle">{p.restaurantName}</span> },
                { key: "price", header: "價格", align: "right", cell: (p) => <span className="tabular text-ink">{money(p.price)}</span> },
                { key: "stock", header: "庫存", align: "right", cell: (p) => <span className="tabular">{p.quantity}</span> },
                {
                  key: "status",
                  header: "狀態",
                  cell: (p) => (p.status === "LISTED" ? <Badge tone="success">上架中</Badge> : <Badge>下架中</Badge>),
                },
              ]}
            />
          )}
        </section>
      </div>
    </PageShell>
  );
}
