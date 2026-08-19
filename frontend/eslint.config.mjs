import { defineConfig, globalIgnores } from "eslint/config";
import nextVitals from "eslint-config-next/core-web-vitals";
import nextTs from "eslint-config-next/typescript";

const eslintConfig = defineConfig([
  ...nextVitals,
  ...nextTs,
  // Override default ignores of eslint-config-next.
  globalIgnores([
    // Default ignores of eslint-config-next:
    ".next/**",
    "out/**",
    "build/**",
    "next-env.d.ts",
    // Test output.
    "test-results/**",
    "playwright-report/**",
  ]),
  {
    rules: {
      /*
       * Downgraded, not disabled.
       *
       * Every occurrence is the same shape: an effect that flips a loading flag
       * and then fetches. The rule is right that this costs an extra render,
       * but the fix it wants is a data-fetching library (React Query, or Server
       * Components doing the fetch) — a change to how every page loads data,
       * not something to smuggle in alongside CI. Kept visible as a warning so
       * the debt stays on the screen.
       */
      "react-hooks/set-state-in-effect": "warn",
    },
  },
]);

export default eslintConfig;
