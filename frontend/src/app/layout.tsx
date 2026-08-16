import type { Metadata } from "next";
import { AuthProvider } from "@/components/AuthProvider";
import { SiteHeader } from "@/components/SiteHeader";
import "./globals.css";

export const metadata: Metadata = {
  title: "胡椒MAP | 美食地圖",
  description: "尋找附近的美食、閱讀評論、購買店家商品。",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="zh-Hant">
      <body className="min-h-screen bg-stone-50 text-stone-900 antialiased dark:bg-stone-950 dark:text-stone-100">
        <AuthProvider>
          <SiteHeader />
          <main className="mx-auto max-w-6xl px-4 py-8">{children}</main>
          <footer className="border-t border-stone-200 py-8 text-center text-xs text-stone-500 dark:border-stone-800">
            胡椒MAP · 重構自 2021 年 EEIT23 專案
          </footer>
        </AuthProvider>
      </body>
    </html>
  );
}
