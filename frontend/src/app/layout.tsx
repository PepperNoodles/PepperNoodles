import type { Metadata } from "next";
import { Sacramento, Sulphur_Point } from "next/font/google";
import { AuthProvider } from "@/components/AuthProvider";
import { SiteHeader } from "@/components/SiteHeader";
import "./globals.css";

// The two display faces the 2021 design used.
const sacramento = Sacramento({ weight: "400", subsets: ["latin"], variable: "--font-sacramento" });
const sulphur = Sulphur_Point({ weight: ["300", "400", "700"], subsets: ["latin"], variable: "--font-sulphur" });

export const metadata: Metadata = {
  title: "胡椒MAP | 美食地圖",
  description: "尋找附近的美食、閱讀評論、購買店家商品。",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="zh-Hant" className={`${sacramento.variable} ${sulphur.variable}`}>
      <body className="min-h-screen antialiased">
        <AuthProvider>
          <SiteHeader />
          {children}
          <footer className="border-t border-stone-200 bg-white py-8 text-center text-xs text-stone-500 dark:border-stone-800 dark:bg-stone-950">
            胡椒MAP · 重構自 2021 年 EEIT23 專案
          </footer>
        </AuthProvider>
      </body>
    </html>
  );
}
