"use client";

import { useEffect, useRef, useState } from "react";

declare global {
  interface Window {
    grecaptcha?: {
      render: (el: HTMLElement, options: Record<string, unknown>) => number;
      reset: (id?: number) => void;
    };
    onRecaptchaLoad?: () => void;
  }
}

const SITE_KEY = process.env.NEXT_PUBLIC_RECAPTCHA_SITE_KEY;

/**
 * Google reCAPTCHA v2 checkbox.
 *
 * <p>Renders nothing when no site key is configured, and reports a null token
 * so the form still submits — the backend's verifier is disabled in that case
 * too. That keeps local development and CI working without Google credentials
 * while the widget appears automatically once keys are set.
 */
export function Recaptcha({ onToken }: { onToken: (token: string | null) => void }) {
  const container = useRef<HTMLDivElement>(null);
  const widgetId = useRef<number | null>(null);
  const [failed, setFailed] = useState(false);

  useEffect(() => {
    if (!SITE_KEY || !container.current) return;

    function render() {
      if (!window.grecaptcha || !container.current || widgetId.current !== null) return;
      widgetId.current = window.grecaptcha.render(container.current, {
        sitekey: SITE_KEY,
        callback: (token: string) => onToken(token),
        "expired-callback": () => onToken(null),
        "error-callback": () => setFailed(true),
      });
    }

    if (window.grecaptcha) {
      render();
      return;
    }

    // Loaded once per page; the global callback is how Google signals readiness.
    window.onRecaptchaLoad = render;
    const script = document.createElement("script");
    script.src = "https://www.google.com/recaptcha/api.js?onload=onRecaptchaLoad&render=explicit";
    script.async = true;
    script.defer = true;
    script.onerror = () => setFailed(true);
    document.head.appendChild(script);
  }, [onToken]);

  if (!SITE_KEY) return null;

  if (failed) {
    return (
      <p className="text-xs text-stone-500">
        驗證元件載入失敗，請確認網路連線後重新整理。
      </p>
    );
  }

  return <div ref={container} />;
}

/** True when a site key is configured, so forms can require a token. */
export const recaptchaEnabled = Boolean(SITE_KEY);
