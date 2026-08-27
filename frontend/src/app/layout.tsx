import type { Metadata } from "next";
import { Sacramento, Sulphur_Point } from "next/font/google";
import { AuthProvider } from "@/components/AuthProvider";
import { SiteHeader } from "@/components/SiteHeader";
import { SiteFooter } from "@/components/SiteFooter";
import "./globals.css";

// The two display faces the 2021 design used. Body copy is 繁體中文 and uses the
// platform's own CJK face — see --font-sans in globals.css — so neither of these
// is ever asked to render a character it does not have.
const sacramento = Sacramento({
  weight: "400",
  subsets: ["latin"],
  variable: "--font-sacramento",
  display: "swap",
});
const sulphur = Sulphur_Point({
  weight: ["300", "400", "700"],
  subsets: ["latin"],
  variable: "--font-sulphur",
  display: "swap",
});

export const metadata: Metadata = {
  title: {
    default: "胡椒MAP | 美食地圖",
    template: "%s | 胡椒MAP",
  },
  description: "尋找附近的美食、閱讀評論、購買店家商品。",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="zh-Hant" className={`${sacramento.variable} ${sulphur.variable}`}>
      <body className="flex min-h-screen flex-col antialiased">
        {/*
          First tab stop on every page. Off-screen until focused — a keyboard
          user should not have to tab through ten nav links on every navigation
          to reach the content.
        */}
        <a
          href="#main"
          className="sr-only z-50 focus:not-sr-only focus:absolute focus:left-4 focus:top-4 focus:rounded-full focus:bg-ink focus:px-5 focus:py-3 focus:font-display focus:text-sm focus:font-bold focus:uppercase focus:text-white"
        >
          跳到主要內容
        </a>

        <AuthProvider>
          <SiteHeader />
          {/*
            A flex column, so a page can opt into filling the leftover height
            with `flex-1` (AuthShell and Gate do, to carry their tinted
            background down to the footer).

            One consequence, worth knowing before adding a page: in a flex
            container `mx-auto` sets both cross-axis margins to `auto`, and an
            item with an auto cross-axis margin is NOT stretched by
            `align-items`. A top-level `mx-auto max-w-*` wrapper therefore
            shrink-wraps its content unless it also says `w-full`. PageShell
            handles this; a hand-rolled wrapper must not forget it.
          */}
          <main id="main" className="flex flex-1 flex-col">
            {children}
          </main>
          <SiteFooter />
        </AuthProvider>
      </body>
    </html>
  );
}
