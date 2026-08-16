"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { api } from "@/lib/api";
import { useAuth } from "@/components/AuthProvider";
import { Card, Empty, ErrorNote, Spinner, Stars, money } from "@/components/ui";
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
      <p className="py-12 text-center text-sm text-stone-500">
        這個頁面只有企業會員能存取。
        <Link href="/" className="ml-1 text-red-600 hover:underline">
          回首頁
        </Link>
      </p>
    );
  }

  return (
    <div className="space-y-8">
      <h1 className="text-2xl font-bold">店家管理</h1>
      <ErrorNote error={error} />

      <section>
        <h2 className="mb-3 text-lg font-semibold">我的餐廳 ({restaurants.length})</h2>
        {restaurants.length === 0 ? (
          <Empty>還沒有登記任何餐廳。</Empty>
        ) : (
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {restaurants.map((restaurant) => (
              <Link key={restaurant.id} href={`/restaurants/${restaurant.id}`}>
                <Card className="h-full p-4 transition hover:shadow-md">
                  <h3 className="font-semibold">{restaurant.name}</h3>
                  <p className="mt-1 text-sm text-stone-500">{restaurant.address}</p>
                  <div className="mt-2">
                    <Stars average={restaurant.rating.average} count={restaurant.rating.reviewCount} />
                  </div>
                </Card>
              </Link>
            ))}
          </div>
        )}
      </section>

      <section>
        <h2 className="mb-3 text-lg font-semibold">我的商品 ({products.length})</h2>
        {products.length === 0 ? (
          <Empty>還沒有上架任何商品。</Empty>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm">
              <thead className="border-b border-stone-200 text-xs uppercase text-stone-500 dark:border-stone-800">
                <tr>
                  <th className="py-2">商品</th>
                  <th className="py-2">餐廳</th>
                  <th className="py-2 text-right">價格</th>
                  <th className="py-2 text-right">庫存</th>
                  <th className="py-2">狀態</th>
                </tr>
              </thead>
              <tbody>
                {products.map((product) => (
                  <tr key={product.id} className="border-b border-stone-100 dark:border-stone-900">
                    <td className="py-2">
                      <Link href={`/shop/${product.id}`} className="hover:underline">
                        {product.name}
                      </Link>
                    </td>
                    <td className="py-2 text-stone-500">{product.restaurantName}</td>
                    <td className="py-2 text-right">{money(product.price)}</td>
                    <td className="py-2 text-right">{product.quantity}</td>
                    <td className="py-2 text-xs">
                      {product.status === "LISTED" ? (
                        <span className="text-green-600">上架中</span>
                      ) : (
                        <span className="text-stone-400">下架中</span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </div>
  );
}
