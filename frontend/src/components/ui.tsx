"use client";

import Link from "next/link";
import type { ReactNode } from "react";

export function Card({ children, className = "" }: { children: ReactNode; className?: string }) {
  return (
    <div
      className={`rounded-xl border border-stone-200 bg-white shadow-sm dark:border-stone-800 dark:bg-stone-900 ${className}`}
    >
      {children}
    </div>
  );
}

export function Button({
  children,
  variant = "primary",
  className = "",
  ...props
}: React.ButtonHTMLAttributes<HTMLButtonElement> & { variant?: "primary" | "ghost" | "danger" }) {
  const styles = {
    primary: "bg-red-600 text-white hover:bg-red-700 disabled:bg-stone-300",
    ghost:
      "border border-stone-300 text-stone-700 hover:bg-stone-100 dark:border-stone-700 dark:text-stone-200 dark:hover:bg-stone-800",
    danger: "bg-stone-800 text-white hover:bg-black",
  }[variant];
  return (
    <button
      {...props}
      className={`rounded-lg px-4 py-2 text-sm font-medium transition disabled:cursor-not-allowed disabled:opacity-60 ${styles} ${className}`}
    >
      {children}
    </button>
  );
}

export function Input(props: React.InputHTMLAttributes<HTMLInputElement>) {
  return (
    <input
      {...props}
      className={`w-full rounded-lg border border-stone-300 bg-white px-3 py-2 text-sm outline-none focus:border-red-500 focus:ring-2 focus:ring-red-100 dark:border-stone-700 dark:bg-stone-900 dark:focus:ring-red-950 ${props.className ?? ""}`}
    />
  );
}

export function TagPill({ children }: { children: ReactNode }) {
  return (
    <span className="rounded-full bg-amber-100 px-2.5 py-0.5 text-xs font-medium text-amber-900 dark:bg-amber-950 dark:text-amber-200">
      {children}
    </span>
  );
}

export function Stars({ average, count }: { average: string | null; count: number }) {
  if (!average || count === 0) {
    return <span className="text-xs text-stone-400">尚無評分</span>;
  }
  const value = Number(average);
  return (
    <span className="flex items-center gap-1 text-xs">
      <span className="text-amber-500" aria-hidden>
        {"★".repeat(Math.round(value))}
        <span className="text-stone-300 dark:text-stone-700">{"★".repeat(5 - Math.round(value))}</span>
      </span>
      <span className="font-medium text-stone-700 dark:text-stone-300">{value.toFixed(1)}</span>
      <span className="text-stone-400">({count})</span>
    </span>
  );
}

export function ErrorNote({ error }: { error: unknown }) {
  if (!error) return null;
  const message = error instanceof Error ? error.message : String(error);
  return (
    <p role="alert" className="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700 dark:bg-red-950 dark:text-red-300">
      {message}
    </p>
  );
}

export function Empty({ children }: { children: ReactNode }) {
  return <p className="py-12 text-center text-sm text-stone-500">{children}</p>;
}

export function Spinner() {
  return (
    <div className="flex justify-center py-12" role="status" aria-label="載入中">
      <div className="h-6 w-6 animate-spin rounded-full border-2 border-stone-300 border-t-red-600" />
    </div>
  );
}

export function NavLink({ href, children }: { href: string; children: ReactNode }) {
  return (
    <Link
      href={href}
      className="rounded-lg px-3 py-2 text-sm font-medium text-stone-600 transition hover:bg-stone-100 hover:text-stone-900 dark:text-stone-300 dark:hover:bg-stone-800 dark:hover:text-white"
    >
      {children}
    </Link>
  );
}

export function money(value: string | number) {
  return `NT$${Math.round(Number(value)).toLocaleString("zh-TW")}`;
}
