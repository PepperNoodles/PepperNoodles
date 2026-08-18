import { defineConfig } from "vitest/config";
import react from "@vitejs/plugin-react";
import { fileURLToPath } from "node:url";

export default defineConfig({
  plugins: [react()],
  test: {
    environment: "jsdom",
    globals: true,
    setupFiles: ["./src/test/setup.ts"],
    // Playwright specs live under e2e/ and are run by `npm run test:e2e`.
    include: ["src/**/*.test.{ts,tsx}"],
  },
  resolve: {
    alias: {
      // fileURLToPath, not URL.pathname — this repo's path contains non-ASCII
      // characters, which pathname leaves percent-encoded so the alias misses.
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
});
