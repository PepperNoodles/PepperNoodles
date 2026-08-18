import { defineConfig, devices } from "@playwright/test";

/**
 * End-to-end tests against the real stack.
 *
 * <p>These assume Supabase, the backend and the frontend are already running —
 * see the README. They are kept out of `npm test` because they need all three;
 * `npm run test:e2e` runs them.
 */
export default defineConfig({
  testDir: "./e2e",
  fullyParallel: false, // the suite shares one seeded database
  workers: 1,
  retries: 0,
  reporter: [["list"]],
  timeout: 30_000,
  use: {
    baseURL: process.env.E2E_BASE_URL ?? "http://localhost:3000",
    trace: "retain-on-failure",
    locale: "zh-TW",
  },
  projects: [{ name: "chromium", use: { ...devices["Desktop Chrome"] } }],
});
