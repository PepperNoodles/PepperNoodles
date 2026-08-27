import Link from "next/link";
import type { ReactNode } from "react";
import { IconCheckCircle } from "./icons";

/**
 * The frame every credential screen sits in — login, register, password reset,
 * e-mail verification, newsletter confirmation.
 *
 * <p>Previously each of those was a bare `<Card className="mx-auto my-16 …">`
 * with its own width, its own heading size and its own idea of spacing. They
 * are the same moment in the same flow and should look like it.
 */
export function AuthShell({
  kicker,
  title,
  description,
  children,
  footer,
  width = "md",
}: {
  kicker?: string;
  title: string;
  description?: ReactNode;
  children: ReactNode;
  footer?: ReactNode;
  width?: "md" | "lg";
}) {
  return (
    /*
      `flex-1` + centring: the tinted band fills whatever height is left below
      the header, so a short form does not leave a white strip above the
      footer, and a tall one still scrolls normally.
    */
    <div className="flex flex-1 items-center bg-mist py-16 lg:py-24">
      <div className={`mx-auto w-full px-5 sm:px-6 ${width === "lg" ? "max-w-xl" : "max-w-md"}`}>
        <div className="rounded-3xl border border-line bg-white p-8 shadow-lift sm:p-10">
          <header className="mb-7">
            {kicker && (
              <span aria-hidden className="block font-script text-3xl leading-none text-pepper">
                {kicker}
              </span>
            )}
            <h1 className="mt-1 font-display text-2xl font-bold tracking-tight text-ink sm:text-3xl">{title}</h1>
            {description && <p className="mt-2.5 text-sm leading-relaxed text-body">{description}</p>}
          </header>
          {children}
        </div>
        {footer && <div className="mt-6 text-center text-sm text-subtle">{footer}</div>}
      </div>
    </div>
  );
}

/**
 * The "we've done the thing" screen — confirmation sent, password changed,
 * subscription confirmed. Same frame, with a success mark instead of a form.
 */
export function AuthResult({
  title,
  children,
  tone = "success",
  action,
}: {
  title: string;
  children?: ReactNode;
  tone?: "success" | "failure";
  action?: ReactNode;
}) {
  return (
    <div className="flex flex-1 items-center bg-mist py-16 lg:py-24">
      <div className="mx-auto w-full max-w-md px-5 sm:px-6">
        <div className="rounded-3xl border border-line bg-white p-10 text-center shadow-lift">
          <span
            aria-hidden
            className={`mx-auto mb-5 flex h-14 w-14 items-center justify-center rounded-full text-2xl ${
              tone === "success" ? "bg-success-tint text-success" : "bg-danger-tint text-danger"
            }`}
          >
            {tone === "success" ? <IconCheckCircle /> : <IconExclamation />}
          </span>
          <h1 className="font-display text-2xl font-bold tracking-tight text-ink">{title}</h1>
          {children && <div className="mt-3 text-sm leading-relaxed text-body">{children}</div>}
          {action && <div className="mt-7">{action}</div>}
        </div>
      </div>
    </div>
  );
}

function IconExclamation() {
  return (
    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={1.75} strokeLinecap="round" width="1em" height="1em" aria-hidden>
      <circle cx="12" cy="12" r="9" />
      <path d="M12 7.5v5.5" />
      <circle cx="12" cy="16.3" r="0.9" fill="currentColor" stroke="none" />
    </svg>
  );
}

/**
 * The Mailpit hint shown after any transactional e-mail is queued. Rendered
 * only outside production — it is a developer convenience, not user-facing copy.
 */
export function MailpitHint() {
  if (process.env.NODE_ENV === "production") return null;
  return (
    <p className="mt-5 text-xs text-subtle">
      開發環境可到{" "}
      <a
        href="http://127.0.0.1:55324"
        target="_blank"
        rel="noreferrer"
        className="font-medium text-pepper-ink underline-offset-2 hover:underline"
      >
        Mailpit
      </a>{" "}
      收信。
    </p>
  );
}

/** Consistent bottom-of-card link row. */
export function AuthFooterLink({ href, children }: { href: string; children: ReactNode }) {
  return (
    <Link href={href} className="font-medium text-pepper-ink underline-offset-2 hover:underline">
      {children}
    </Link>
  );
}
