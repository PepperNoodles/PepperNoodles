"use client";

import Link from "next/link";
import type { ReactNode } from "react";
import {
  IconAlert,
  IconArrowLeft,
  IconBowl,
  IconCheckCircle,
  IconChevronLeft,
  IconChevronRight,
  IconInfo,
  IconStar,
  IconStarFilled,
} from "./icons";

/* ------------------------------------------------------------------ *
 * Surfaces
 * ------------------------------------------------------------------ */

/**
 * The one card surface. `interactive` is for cards that are themselves a link —
 * it adds the hover lift, and the `cursor-pointer` that a wrapping <Link> does
 * not give a nested <div>.
 */
export function Card({
  children,
  className = "",
  interactive = false,
  as: Tag = "div",
}: {
  children: ReactNode;
  className?: string;
  interactive?: boolean;
  as?: "div" | "article" | "li";
}) {
  return (
    <Tag
      className={`rounded-2xl border border-line bg-white shadow-card ${
        interactive
          ? "cursor-pointer transition duration-200 hover:-translate-y-0.5 hover:border-line-strong hover:shadow-lift motion-reduce:hover:translate-y-0"
          : ""
      } ${className}`}
    >
      {children}
    </Tag>
  );
}

/* ------------------------------------------------------------------ *
 * Buttons
 * ------------------------------------------------------------------ */

type ButtonVariant = "primary" | "secondary" | "ghost" | "danger" | "quiet";
type ButtonSize = "sm" | "md" | "lg";

const BUTTON_VARIANTS: Record<ButtonVariant, string> = {
  // Pepper is the brand's action colour and is reserved for the one primary
  // action on a view. White on #ff3d1c is 4.0:1 — fine at 14px bold (large-text
  // threshold), which is why the label is never lighter than semibold.
  primary: "bg-pepper-fill text-white shadow-card hover:bg-pepper-dark active:bg-pepper-dark",
  secondary: "bg-ink text-white hover:bg-black",
  ghost: "border border-line-strong bg-white text-ink hover:border-ink hover:bg-mist",
  // Destructive is deliberately not pepper — "delete" must not look like "buy".
  danger: "bg-danger text-white hover:brightness-95",
  quiet: "text-subtle hover:bg-mist hover:text-ink",
};

/*
 * Sizes are expressed thumb-first. `sm` used to be a flat 36px, which is fine
 * under a mouse and below the 44px comfortable-target floor on a phone — so it
 * is 44px by default and only shrinks from `sm:` up, where a fine pointer is
 * the likely input. `md` and `lg` already clear the floor at every width.
 */
const BUTTON_SIZES: Record<ButtonSize, string> = {
  sm: "min-h-11 px-4 py-1.5 text-[13px] sm:min-h-9 sm:px-3.5",
  md: "min-h-11 px-5 py-2.5 text-sm",
  lg: "min-h-12 px-7 py-3 text-[15px]",
};

export function Button({
  children,
  variant = "primary",
  size = "md",
  loading = false,
  icon,
  className = "",
  ...props
}: React.ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: ButtonVariant;
  size?: ButtonSize;
  loading?: boolean;
  icon?: ReactNode;
}) {
  return (
    <button
      {...props}
      disabled={props.disabled || loading}
      aria-busy={loading || undefined}
      className={`inline-flex cursor-pointer items-center justify-center gap-2 rounded-full font-display font-bold uppercase tracking-wide transition duration-200 disabled:cursor-not-allowed disabled:opacity-50 disabled:shadow-none ${BUTTON_SIZES[size]} ${BUTTON_VARIANTS[variant]} ${className}`}
    >
      {loading ? <InlineSpinner /> : icon}
      {children}
    </button>
  );
}

/**
 * A link styled as a button. Kept separate from `Button` rather than adding an
 * `href` prop, so the two never disagree about which element is rendered — a
 * navigation must stay an <a> for middle-click, "open in new tab" and the
 * browser's own status bar.
 */
export function ButtonLink({
  href,
  children,
  variant = "primary",
  size = "md",
  icon,
  className = "",
}: {
  href: string;
  children: ReactNode;
  variant?: ButtonVariant;
  size?: ButtonSize;
  icon?: ReactNode;
  className?: string;
}) {
  return (
    <Link
      href={href}
      className={`inline-flex cursor-pointer items-center justify-center gap-2 rounded-full font-display font-bold uppercase tracking-wide transition duration-200 ${BUTTON_SIZES[size]} ${BUTTON_VARIANTS[variant]} ${className}`}
    >
      {icon}
      {children}
    </Link>
  );
}

function InlineSpinner() {
  return (
    <span
      aria-hidden
      className="h-4 w-4 shrink-0 animate-spin rounded-full border-2 border-current border-t-transparent opacity-70"
    />
  );
}

/* ------------------------------------------------------------------ *
 * Form controls
 * ------------------------------------------------------------------ */

/* One chrome for every control, so an <input>, a <select> and a <textarea>
   sitting in the same form are the same height and share a focus treatment. */
const CONTROL =
  "w-full rounded-xl border border-line-strong bg-white px-3.5 text-[15px] text-ink transition placeholder:text-subtle " +
  "hover:border-ink/30 focus:border-pepper focus:outline-none focus:ring-4 focus:ring-pepper/15 " +
  "disabled:cursor-not-allowed disabled:bg-mist disabled:text-subtle " +
  "aria-[invalid=true]:border-danger aria-[invalid=true]:ring-danger/15";

export function Input(props: React.InputHTMLAttributes<HTMLInputElement>) {
  return <input {...props} className={`${CONTROL} min-h-11 py-2.5 ${props.className ?? ""}`} />;
}

export function Textarea(props: React.TextareaHTMLAttributes<HTMLTextAreaElement>) {
  return <textarea {...props} className={`${CONTROL} resize-y py-2.5 leading-relaxed ${props.className ?? ""}`} />;
}

export function Select(props: React.SelectHTMLAttributes<HTMLSelectElement>) {
  return <select {...props} className={`${CONTROL} min-h-11 cursor-pointer py-2.5 ${props.className ?? ""}`} />;
}

/**
 * Label + control + hint/error, wired together.
 *
 * <p>The label is always visible — a placeholder disappears the moment typing
 * starts, which is exactly when the user still needs to know what the field
 * was. The hint and the error are joined into `aria-describedby` and the error
 * sits directly under its own field rather than in a summary at the top.
 */
export function Field({
  id,
  label,
  hint,
  error,
  required,
  children,
}: {
  id: string;
  label: string;
  hint?: string;
  error?: string | null;
  required?: boolean;
  children: (props: {
    id: string;
    required?: boolean;
    "aria-invalid"?: true;
    "aria-describedby"?: string;
  }) => ReactNode;
}) {
  const described = [hint ? `${id}-hint` : null, error ? `${id}-error` : null].filter(Boolean).join(" ");

  return (
    <div>
      <label htmlFor={id} className="mb-1.5 block text-sm font-semibold text-ink">
        {label}
        {required && (
          <span className="ml-0.5 text-pepper-ink" aria-hidden>
            *
          </span>
        )}
      </label>
      {children({
        id,
        required,
        "aria-invalid": error ? true : undefined,
        "aria-describedby": described || undefined,
      })}
      {hint && !error && (
        <p id={`${id}-hint`} className="mt-1.5 text-[13px] text-subtle">
          {hint}
        </p>
      )}
      {error && (
        <p id={`${id}-error`} className="mt-1.5 flex items-center gap-1.5 text-[13px] font-medium text-danger">
          <IconAlert className="shrink-0 text-base" />
          {error}
        </p>
      )}
    </div>
  );
}

/** Live "n / max" counter for a bounded textarea. Warns before the cap, not at it. */
export function CharCount({ value, max }: { value: string; max: number }) {
  const near = value.length > max * 0.9;
  return (
    <p className={`mt-1.5 text-right text-xs tabular ${near ? "font-medium text-warn" : "text-subtle"}`}>
      {value.length} / {max}
    </p>
  );
}

/* ------------------------------------------------------------------ *
 * Tags
 * ------------------------------------------------------------------ */

/** A non-interactive label on a card. */
export function TagPill({ children }: { children: ReactNode }) {
  return (
    <span className="inline-flex items-center rounded-full border border-line bg-mist px-2.5 py-1 text-xs font-medium text-body">
      {children}
    </span>
  );
}

/**
 * A toggleable filter. Selected state is carried by fill *and* a check glyph,
 * not colour alone, and the hit area clears 40px even though the pill reads
 * smaller — the padding does the work.
 */
export function FilterChip({
  active,
  children,
  ...props
}: React.ButtonHTMLAttributes<HTMLButtonElement> & { active: boolean }) {
  return (
    <button
      type="button"
      aria-pressed={active}
      {...props}
      className={`inline-flex min-h-10 cursor-pointer items-center gap-1.5 rounded-full border px-4 text-[13px] font-medium transition duration-200 ${
        active
          ? "border-pepper bg-pepper-tint text-pepper-ink"
          : "border-line-strong bg-white text-body hover:border-ink/40 hover:bg-mist"
      }`}
    >
      {active && <IconCheck aria-hidden className="text-sm" />}
      {children}
    </button>
  );
}

function IconCheck(props: React.SVGProps<SVGSVGElement>) {
  return (
    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={3} strokeLinecap="round" width="1em" height="1em" {...props}>
      <path d="m5 12.5 4.5 4.5L19 7" />
    </svg>
  );
}

/** Status label. Tone carries an icon too, so the meaning survives greyscale. */
export function Badge({
  tone = "neutral",
  children,
}: {
  tone?: "neutral" | "success" | "warn" | "danger" | "brand";
  children: ReactNode;
}) {
  const tones = {
    neutral: "border-line bg-mist text-body",
    success: "border-success/25 bg-success-tint text-success",
    warn: "border-warn/25 bg-warn-tint text-warn",
    danger: "border-danger/25 bg-danger-tint text-danger",
    brand: "border-pepper/25 bg-pepper-tint text-pepper-ink",
  }[tone];
  return (
    <span className={`inline-flex items-center gap-1 rounded-full border px-2.5 py-1 text-xs font-semibold ${tones}`}>
      {children}
    </span>
  );
}

/* ------------------------------------------------------------------ *
 * Rating
 * ------------------------------------------------------------------ */

/**
 * Five stars plus the numeric average.
 *
 * <p>The stars are `aria-hidden` and the real value is given as text, so a
 * screen reader hears "4.3 分，12 則評論" rather than five separate glyphs.
 */
export function Stars({
  average,
  count,
  size = "sm",
}: {
  average: string | null;
  count: number;
  size?: "sm" | "md";
}) {
  if (!average || count === 0) {
    return <span className="text-xs text-subtle">尚無評分</span>;
  }
  const value = Number(average);
  const filled = Math.round(value);
  const glyph = size === "md" ? "text-lg" : "text-sm";

  return (
    <span className="flex items-center gap-1.5">
      <span className={`flex text-gold ${glyph}`} aria-hidden>
        {Array.from({ length: 5 }, (_, i) =>
          i < filled ? <IconStarFilled key={i} /> : <IconStar key={i} className="text-line-strong" />,
        )}
      </span>
      <span className="text-sm font-semibold tabular text-ink">{value.toFixed(1)}</span>
      <span className="text-xs text-subtle tabular">({count})</span>
    </span>
  );
}

/* ------------------------------------------------------------------ *
 * Feedback
 * ------------------------------------------------------------------ */

export function Alert({
  tone = "info",
  title,
  children,
}: {
  tone?: "info" | "success" | "warn" | "danger";
  title?: string;
  children: ReactNode;
}) {
  const config = {
    info: { cls: "border-line bg-mist text-body", Icon: IconInfo },
    success: { cls: "border-success/25 bg-success-tint text-success", Icon: IconCheckCircle },
    warn: { cls: "border-warn/25 bg-warn-tint text-warn", Icon: IconAlert },
    danger: { cls: "border-danger/25 bg-danger-tint text-danger", Icon: IconAlert },
  }[tone];

  return (
    <div className={`flex gap-3 rounded-xl border px-4 py-3 text-sm ${config.cls}`}>
      <config.Icon className="mt-0.5 shrink-0 text-base" />
      <div className="min-w-0">
        {title && <p className="font-semibold">{title}</p>}
        <div className={title ? "mt-0.5" : ""}>{children}</div>
      </div>
    </div>
  );
}

/** Announced immediately — it always reports something the user just triggered. */
export function ErrorNote({ error }: { error: unknown }) {
  if (!error) return null;
  const message = error instanceof Error ? error.message : String(error);
  return (
    <div role="alert">
      <Alert tone="danger">{message}</Alert>
    </div>
  );
}

export function SuccessNote({ children }: { children: ReactNode }) {
  return (
    <div role="status">
      <Alert tone="success">{children}</Alert>
    </div>
  );
}

/**
 * Empty state. An icon and a next step, rather than a bare sentence in the
 * middle of a blank page — "nothing here" should always come with "do this".
 */
export function Empty({
  children,
  icon,
  action,
}: {
  children: ReactNode;
  icon?: ReactNode;
  action?: ReactNode;
}) {
  return (
    <div className="flex flex-col items-center rounded-2xl border border-dashed border-line-strong bg-mist/60 px-6 py-14 text-center">
      <span className="mb-3 text-3xl text-line-strong" aria-hidden>
        {icon ?? <IconBowl />}
      </span>
      <p className="max-w-sm text-sm text-body">{children}</p>
      {action && <div className="mt-5">{action}</div>}
    </div>
  );
}

export function Spinner() {
  return (
    <div className="flex justify-center py-16" role="status" aria-label="載入中">
      <div className="h-7 w-7 animate-spin rounded-full border-2 border-line-strong border-t-pepper" />
    </div>
  );
}

/* ------------------------------------------------------------------ *
 * Skeletons
 * ------------------------------------------------------------------ *
 * A whole-page spinner throws the layout away and rebuilds it, which reads as
 * slower than it is and shifts everything when the data lands. These reserve
 * the real shape instead.
 */

export function Skeleton({ className = "" }: { className?: string }) {
  return <div className={`skeleton rounded-lg ${className}`} aria-hidden />;
}

export function CardSkeleton() {
  return (
    <div className="rounded-2xl border border-line bg-white p-5 shadow-card">
      <Skeleton className="h-5 w-2/3" />
      <Skeleton className="mt-3 h-4 w-full" />
      <Skeleton className="mt-2 h-4 w-1/2" />
      <div className="mt-4 flex gap-2">
        <Skeleton className="h-6 w-16 rounded-full" />
        <Skeleton className="h-6 w-16 rounded-full" />
      </div>
    </div>
  );
}

export function CardGridSkeleton({ count = 6, columns = "sm:grid-cols-2 lg:grid-cols-3" }: { count?: number; columns?: string }) {
  return (
    <div className={`grid gap-5 ${columns}`} role="status" aria-label="載入中">
      {Array.from({ length: count }, (_, i) => (
        <CardSkeleton key={i} />
      ))}
    </div>
  );
}

export function ListSkeleton({ rows = 5 }: { rows?: number }) {
  return (
    <div className="space-y-3" role="status" aria-label="載入中">
      {Array.from({ length: rows }, (_, i) => (
        <div key={i} className="rounded-2xl border border-line bg-white p-5 shadow-card">
          <Skeleton className="h-4 w-1/3" />
          <Skeleton className="mt-3 h-4 w-full" />
        </div>
      ))}
    </div>
  );
}

/* ------------------------------------------------------------------ *
 * Page structure
 * ------------------------------------------------------------------ *
 * Before this, every page hand-rolled its own <h1 className="text-2xl
 * font-bold"> and its own back-link, so no two pages agreed on the height of
 * their own header. These three components are the whole vocabulary.
 */

/** Standard page container. One width scale, one padding rhythm, everywhere. */
export function PageShell({
  children,
  width = "wide",
  className = "",
}: {
  children: ReactNode;
  width?: "narrow" | "reading" | "wide" | "full";
  className?: string;
}) {
  const max = {
    narrow: "max-w-2xl",
    reading: "max-w-3xl",
    wide: "max-w-7xl",
    full: "max-w-[96rem]",
  }[width];
  return <div className={`mx-auto w-full ${max} px-5 py-10 sm:px-6 lg:py-14 ${className}`}>{children}</div>;
}

export function PageHeader({
  title,
  kicker,
  description,
  actions,
  back,
}: {
  title: string;
  /** The 2021 script flourish. Decorative — it is never the accessible name. */
  kicker?: string;
  description?: ReactNode;
  actions?: ReactNode;
  back?: { href: string; label: string };
}) {
  return (
    <header className="mb-8 border-b border-line pb-6">
      {back && (
        <Link
          href={back.href}
          className="-ml-2 mb-1 inline-flex min-h-11 items-center gap-1.5 rounded-lg px-2 text-sm font-medium text-subtle transition hover:bg-mist hover:text-pepper-ink sm:-ml-0 sm:mb-3 sm:min-h-0 sm:px-0 sm:hover:bg-transparent"
        >
          <IconArrowLeft className="text-base" />
          {back.label}
        </Link>
      )}
      <div className="flex flex-wrap items-end justify-between gap-x-6 gap-y-4">
        <div className="min-w-0">
          {kicker && (
            <span aria-hidden className="block font-script text-2xl leading-none text-pepper">
              {kicker}
            </span>
          )}
          <h1 className="mt-1 font-display text-3xl font-bold tracking-tight text-ink sm:text-[2rem]">{title}</h1>
          {description && <p className="measure mt-2 text-[15px] leading-relaxed text-body">{description}</p>}
        </div>
        {actions && <div className="flex flex-wrap items-center gap-2.5">{actions}</div>}
      </div>
    </header>
  );
}

/** Heading for a block inside a page. */
export function SectionHeader({
  title,
  count,
  action,
  description,
}: {
  title: string;
  count?: number;
  action?: ReactNode;
  description?: string;
}) {
  return (
    <div className="mb-4 flex flex-wrap items-end justify-between gap-3">
      <div>
        <h2 className="font-display text-xl font-bold tracking-tight text-ink">
          {title}
          {count !== undefined && <span className="ml-2 text-base font-normal tabular text-subtle">{count}</span>}
        </h2>
        {description && <p className="mt-1 text-sm text-subtle">{description}</p>}
      </div>
      {action}
    </div>
  );
}

/** The centred, script-kickered heading the 2021 marketing sections used. */
export function DisplayHeading({ kicker, title }: { kicker: string; title: string }) {
  return (
    <div className="mb-10 text-center">
      <span aria-hidden className="font-script text-4xl leading-none text-pepper">
        {kicker}
      </span>
      <h2 className="mt-2 font-display text-3xl font-bold tracking-tight sm:text-4xl">{title}</h2>
    </div>
  );
}

/**
 * Prev/next pager.
 *
 * <p>The page indicator is a live region: paging is an async content swap with
 * no focus change, so without it a screen-reader user gets no confirmation that
 * anything happened.
 */
export function Pagination({
  page,
  totalPages,
  first,
  last,
  onChange,
}: {
  page: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  onChange: (next: number) => void;
}) {
  if (totalPages <= 1) return null;
  return (
    <nav className="flex items-center justify-center gap-4 pt-4" aria-label="分頁">
      <Button variant="ghost" size="sm" disabled={first} onClick={() => onChange(page - 1)} icon={<IconChevronLeft />}>
        上一頁
      </Button>
      <span className="text-sm tabular text-subtle" aria-live="polite">
        第 {page + 1} / {totalPages} 頁
      </span>
      <Button variant="ghost" size="sm" disabled={last} onClick={() => onChange(page + 1)}>
        下一頁
        <IconChevronRight />
      </Button>
    </nav>
  );
}

/** A single figure — dashboards and reports. */
export function StatCard({ label, value, tone }: { label: string; value: ReactNode; tone?: "brand" }) {
  return (
    <div className="rounded-2xl border border-line bg-white p-5 shadow-card">
      <p className={`font-display text-2xl font-bold tabular ${tone === "brand" ? "text-pepper" : "text-ink"}`}>
        {value}
      </p>
      <p className="mt-1 text-[13px] text-subtle">{label}</p>
    </div>
  );
}

/**
 * The "you can't see this" screen, shared by every gated route. Previously each
 * page wrote its own centred sentence, at a different size each time.
 */
export function Gate({ title, children, action }: { title: string; children?: ReactNode; action?: ReactNode }) {
  return (
    <PageShell width="narrow" className="flex flex-1 items-center">
      <div className="w-full rounded-2xl border border-line bg-white px-8 py-16 text-center shadow-card">
        <span className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-full bg-mist text-2xl text-line-strong" aria-hidden>
          <IconBowl />
        </span>
        <h1 className="font-display text-2xl font-bold text-ink">{title}</h1>
        {children && <p className="mx-auto mt-3 max-w-sm text-sm leading-relaxed text-body">{children}</p>}
        {action && <div className="mt-7 flex justify-center">{action}</div>}
      </div>
    </PageShell>
  );
}

/** Full-bleed photographic banner used by the home hero and the shop header. */
export function HeroBanner({
  image,
  kicker,
  title,
  children,
  size = "md",
}: {
  image: string;
  kicker?: string;
  title: string;
  children?: ReactNode;
  size?: "sm" | "md" | "lg";
}) {
  const height = { sm: "min-h-[300px]", md: "min-h-[380px]", lg: "min-h-[620px] lg:min-h-[760px]" }[size];
  return (
    <section
      className={`hero-overlay relative flex ${height} items-center justify-center bg-cover bg-center`}
      style={{ backgroundImage: `url(${image})` }}
    >
      <div className="relative z-10 mx-auto w-full max-w-3xl px-6 text-center">
        {kicker && (
          <span aria-hidden className="on-photo block font-script text-4xl leading-tight text-mint sm:text-5xl">
            {kicker}
          </span>
        )}
        <h1
          className={`on-photo mt-2 font-display font-bold leading-[1.08] tracking-tight text-white ${
            size === "lg" ? "text-5xl sm:text-6xl lg:text-7xl" : "text-4xl sm:text-5xl"
          }`}
        >
          {title}
        </h1>
        {children}
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------ *
 * Misc
 * ------------------------------------------------------------------ */

export function NavLink({ href, children }: { href: string; children: ReactNode }) {
  return (
    <Link
      href={href}
      className="rounded-lg px-3 py-2 text-sm font-medium text-body transition hover:bg-mist hover:text-ink"
    >
      {children}
    </Link>
  );
}

/** Inline text link inside a sentence. Uses the darker pepper for 4.9:1. */
export function TextLink({ href, children, className = "" }: { href: string; children: ReactNode; className?: string }) {
  return (
    <Link href={href} className={`font-medium text-pepper-ink underline-offset-2 hover:underline ${className}`}>
      {children}
    </Link>
  );
}

export function money(value: string | number) {
  return `NT$${Math.round(Number(value)).toLocaleString("zh-TW")}`;
}

/* ------------------------------------------------------------------ *
 * Composite controls
 * ------------------------------------------------------------------ */

/**
 * Multi-select over the food tags.
 *
 * <p>Six pages rendered their own copy of this loop, and three of them styled
 * the selected state with a raw `bg-red-600` that is not the brand red.
 */
export function TagPicker({
  legend,
  hint,
  tags,
  selected,
  onChange,
}: {
  legend: string;
  hint?: string;
  tags: { id: number; name: string }[];
  selected: number[];
  onChange: (next: number[]) => void;
}) {
  if (tags.length === 0) return null;
  return (
    <fieldset>
      <legend className="mb-2 text-sm font-semibold text-ink">{legend}</legend>
      {hint && <p className="mb-3 text-[13px] text-subtle">{hint}</p>}
      <div className="flex flex-wrap gap-2">
        {tags.map((tag) => {
          const active = selected.includes(tag.id);
          return (
            <FilterChip
              key={tag.id}
              active={active}
              onClick={() =>
                onChange(active ? selected.filter((id) => id !== tag.id) : [...selected, tag.id])
              }
            >
              {tag.name}
            </FilterChip>
          );
        })}
      </div>
    </fieldset>
  );
}

/**
 * Image upload with a preview.
 *
 * <p>A bare `<input type="file">` renders as an OS-native button that matches
 * nothing else on the page and cannot be sized. The input is kept — it is the
 * real, keyboard-reachable control — and visually replaced by its own label.
 */
export function ImageUploadField({
  label,
  inputLabel,
  imageUrl,
  alt = "",
  emptyLabel = "尚無圖片",
  hint,
  uploading,
  shape = "landscape",
  onFile,
}: {
  /** Visible text on the button. */
  label: string;
  /**
   * Accessible name for the input itself. The visible label reads "上傳圖片" on
   * every one of these, which is useless when a page has two — this is what a
   * screen reader announces, and what a test addresses the control by.
   */
  inputLabel: string;
  imageUrl?: string | null;
  alt?: string;
  emptyLabel?: string;
  hint?: string;
  uploading?: boolean;
  shape?: "landscape" | "square";
  onFile: (file: File) => void;
}) {
  const box = shape === "square" ? "h-28 w-28" : "h-28 w-44";
  return (
    <div className="flex flex-wrap items-center gap-5">
      {imageUrl ? (
        // eslint-disable-next-line @next/next/no-img-element
        <img src={imageUrl} alt={alt} className={`${box} shrink-0 rounded-xl object-cover ring-1 ring-line`} />
      ) : (
        <div
          className={`${box} flex shrink-0 items-center justify-center rounded-xl border border-dashed border-line-strong bg-mist text-center text-[13px] text-subtle`}
        >
          {emptyLabel}
        </div>
      )}
      <div>
        <label className="inline-flex min-h-11 cursor-pointer items-center gap-2 rounded-full border border-line-strong bg-white px-5 font-display text-sm font-bold uppercase tracking-wide text-ink transition hover:border-ink hover:bg-mist has-[:focus-visible]:outline has-[:focus-visible]:outline-2 has-[:focus-visible]:outline-offset-2 has-[:focus-visible]:outline-pepper">
          <UploadGlyph />
          {uploading ? "上傳中…" : label}
          <input
            type="file"
            accept="image/*"
            aria-label={inputLabel}
            disabled={uploading}
            onChange={(e) => e.target.files?.[0] && onFile(e.target.files[0])}
            className="sr-only"
          />
        </label>
        {hint && <p className="mt-2.5 text-[13px] text-subtle">{hint}</p>}
      </div>
    </div>
  );
}

function UploadGlyph() {
  return (
    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={1.75} strokeLinecap="round" strokeLinejoin="round" width="1em" height="1em" aria-hidden>
      <path d="M12 16V4m0 0L7.5 8.5M12 4l4.5 4.5" />
      <path d="M4 16v2.5A1.5 1.5 0 0 0 5.5 20h13a1.5 1.5 0 0 0 1.5-1.5V16" />
    </svg>
  );
}

/* ------------------------------------------------------------------ *
 * Data table
 * ------------------------------------------------------------------ *
 * The back office had five hand-rolled tables, each with slightly different
 * padding and header casing. These give them one voice.
 *
 * <p>They also reflow. A five-column table inside an `overflow-x-auto` box is
 * technically responsive — nothing spills off the page — but on a phone it
 * means dragging a 576px-wide grid sideways to read one member's status, with
 * the column headings scrolled out of view. Below `sm` each row is rendered
 * again as a card with its own labels, and the table is hidden; from `sm` up
 * the reverse. Two markup paths rather than CSS `display:block` trickery,
 * because flattening a <table> strips its roles from assistive technology and
 * each path here stays semantically correct for the width it serves.
 */

export interface Column<T> {
  /** Stable key, used for React keys and nothing else. */
  key: string;
  header: string;
  align?: "left" | "right";
  cell: (row: T) => ReactNode;
  /**
   * Marks the column that identifies the row. On mobile it becomes the card's
   * heading instead of a labelled field; there should be exactly one.
   */
  primary?: boolean;
  /** Hidden on the mobile card — for columns that only repeat the heading. */
  hideOnCard?: boolean;
}

export function DataTable<T>({
  columns,
  rows,
  rowKey,
  caption,
}: {
  columns: Column<T>[];
  rows: T[];
  rowKey: (row: T) => string | number;
  /** Announced to screen readers; never painted. */
  caption: string;
}) {
  const primary = columns.find((c) => c.primary);
  const rest = columns.filter((c) => c !== primary && !c.hideOnCard);

  return (
    <>
      {/* ---- phones: one card per row ---- */}
      <ul className="space-y-3 sm:hidden" aria-label={caption}>
        {rows.map((row) => (
          <li key={rowKey(row)} className="rounded-2xl border border-line bg-white p-4 shadow-card">
            {primary && (
              <div className="break-words font-display text-[15px] font-bold text-ink [&_a]:inline-flex [&_a]:min-h-11 [&_a]:items-center">
                {primary.cell(row)}
              </div>
            )}
            <dl className={primary ? "mt-3 space-y-2" : "space-y-2"}>
              {rest.map((column) => (
                <div key={column.key} className="flex items-baseline justify-between gap-4 text-sm">
                  <dt className="shrink-0 text-xs uppercase tracking-wider text-subtle">{column.header}</dt>
                  <dd className="min-w-0 break-words text-right text-body [&_a]:inline-flex [&_a]:min-h-11 [&_a]:items-center">
                    {column.cell(row)}
                  </dd>
                </div>
              ))}
            </dl>
          </li>
        ))}
      </ul>

      {/* ---- from `sm` up: the real table ---- */}
      <div className="hidden overflow-x-auto rounded-2xl border border-line bg-white shadow-card sm:block">
        <table className="w-full text-left text-sm">
          <caption className="sr-only">{caption}</caption>
          <thead>
            <tr>
              {columns.map((column) => (
                <Th key={column.key} align={column.align}>
                  {column.header}
                </Th>
              ))}
            </tr>
          </thead>
          <tbody>
            {rows.map((row) => (
              <Tr key={rowKey(row)}>
                {columns.map((column) => (
                  <Td key={column.key} align={column.align}>
                    {column.cell(row)}
                  </Td>
                ))}
              </Tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}

export function Th({
  children,
  align = "left",
}: {
  children: ReactNode;
  align?: "left" | "right";
}) {
  return (
    <th
      scope="col"
      className={`whitespace-nowrap border-b border-line px-4 py-3.5 text-xs font-semibold uppercase tracking-wider text-subtle ${
        align === "right" ? "text-right" : ""
      }`}
    >
      {children}
    </th>
  );
}

export function Td({
  children,
  align = "left",
  className = "",
}: {
  children: ReactNode;
  align?: "left" | "right";
  className?: string;
}) {
  return (
    <td className={`px-4 py-3.5 align-middle ${align === "right" ? "text-right" : ""} ${className}`}>
      {children}
    </td>
  );
}

export function Tr({ children }: { children: ReactNode }) {
  return <tr className="border-b border-line transition last:border-0 hover:bg-mist">{children}</tr>;
}

/** Checkbox with its label, sized so the whole row is the hit target. */
export function Checkbox({
  id,
  label,
  hint,
  checked,
  onChange,
}: {
  id: string;
  label: string;
  hint?: string;
  checked: boolean;
  onChange: (checked: boolean) => void;
}) {
  return (
    <label
      htmlFor={id}
      className="flex cursor-pointer items-start gap-3 rounded-xl border border-line p-4 transition hover:border-line-strong hover:bg-mist has-[:focus-visible]:outline has-[:focus-visible]:outline-2 has-[:focus-visible]:outline-offset-2 has-[:focus-visible]:outline-pepper"
    >
      <input
        id={id}
        type="checkbox"
        checked={checked}
        onChange={(e) => onChange(e.target.checked)}
        className="mt-0.5 h-6 w-6 shrink-0 cursor-pointer accent-[var(--color-pepper-fill)] sm:h-5 sm:w-5"
      />
      <span>
        <span className="block text-sm font-medium text-ink">{label}</span>
        {hint && <span className="mt-0.5 block text-[13px] text-subtle">{hint}</span>}
      </span>
    </label>
  );
}
