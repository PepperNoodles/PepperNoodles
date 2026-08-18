import { beforeEach, describe, expect, it, vi } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { AuthProvider, useAuth } from "./AuthProvider";
import { tokenStore } from "@/lib/api";

function json(body: unknown, status = 200) {
  return new Response(JSON.stringify(body), {
    status,
    headers: { "content-type": "application/json" },
  });
}

const PROFILE = {
  userId: 5,
  email: "mei@example.com",
  nickname: "小美",
  realName: "王小美",
  avatarUrl: null,
  roles: ["ROLE_USER"],
};

/** Surfaces the context so assertions can read it from the DOM. */
function Probe() {
  const { user, loading, login, logout, hasRole } = useAuth();
  if (loading) return <p>loading</p>;
  return (
    <div>
      <p data-testid="name">{user ? user.displayName : "anonymous"}</p>
      <p data-testid="isCompany">{String(hasRole("ROLE_COMPANY"))}</p>
      <button onClick={() => login("mei@example.com", "Password123!")}>login</button>
      <button onClick={() => logout()}>logout</button>
    </div>
  );
}

function renderProbe() {
  return render(
    <AuthProvider>
      <Probe />
    </AuthProvider>,
  );
}

describe("AuthProvider", () => {
  beforeEach(() => {
    localStorage.clear();
  });

  it("settles as anonymous when there is no stored token, without calling the API", async () => {
    const fetchMock = vi.fn();
    vi.stubGlobal("fetch", fetchMock);

    renderProbe();

    await waitFor(() => expect(screen.getByTestId("name")).toHaveTextContent("anonymous"));
    expect(fetchMock).not.toHaveBeenCalled();
  });

  it("restores the session from a stored token on mount", async () => {
    localStorage.setItem("pn.accessToken", "access-1");
    localStorage.setItem("pn.refreshToken", "refresh-1");
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(json(PROFILE)));

    renderProbe();

    await waitFor(() => expect(screen.getByTestId("name")).toHaveTextContent("小美"));
  });

  it("falls back to the e-mail when the profile has no nickname", async () => {
    localStorage.setItem("pn.accessToken", "access-1");
    localStorage.setItem("pn.refreshToken", "refresh-1");
    vi.stubGlobal(
      "fetch",
      vi.fn().mockResolvedValue(json({ ...PROFILE, nickname: null, realName: null })),
    );

    renderProbe();

    await waitFor(() => expect(screen.getByTestId("name")).toHaveTextContent("mei@example.com"));
  });

  /** A stale token must not leave the UI stuck on a spinner. */
  it("clears a token the server rejects and settles as anonymous", async () => {
    localStorage.setItem("pn.accessToken", "stale");
    localStorage.setItem("pn.refreshToken", "stale");
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(json({ detail: "nope" }, 401)));

    renderProbe();

    await waitFor(() => expect(screen.getByTestId("name")).toHaveTextContent("anonymous"));
    expect(tokenStore.access).toBeNull();
  });

  it("stores both tokens on login and exposes the user", async () => {
    vi.stubGlobal(
      "fetch",
      vi.fn().mockResolvedValue(
        json({
          accessToken: "access-new",
          refreshToken: "refresh-new",
          tokenType: "Bearer",
          expiresIn: 900,
          expiresAt: "2026-08-18T00:00:00Z",
          user: { id: 5, email: "mei@example.com", displayName: "小美", roles: ["ROLE_USER"] },
        }),
      ),
    );

    renderProbe();
    await waitFor(() => expect(screen.getByTestId("name")).toHaveTextContent("anonymous"));

    await userEvent.click(screen.getByRole("button", { name: "login" }));

    await waitFor(() => expect(screen.getByTestId("name")).toHaveTextContent("小美"));
    expect(tokenStore.access).toBe("access-new");
    expect(tokenStore.refresh).toBe("refresh-new");
  });

  it("ends the local session on logout even if the server call fails", async () => {
    localStorage.setItem("pn.accessToken", "access-1");
    localStorage.setItem("pn.refreshToken", "refresh-1");
    vi.stubGlobal(
      "fetch",
      vi.fn().mockImplementation((url: string) =>
        url.endsWith("/auth/logout")
          ? Promise.reject(new Error("network down"))
          : Promise.resolve(json(PROFILE)),
      ),
    );

    renderProbe();
    await waitFor(() => expect(screen.getByTestId("name")).toHaveTextContent("小美"));

    await userEvent.click(screen.getByRole("button", { name: "logout" }));

    await waitFor(() => expect(screen.getByTestId("name")).toHaveTextContent("anonymous"));
    expect(tokenStore.access).toBeNull();
  });

  it("reports roles through hasRole", async () => {
    localStorage.setItem("pn.accessToken", "access-1");
    localStorage.setItem("pn.refreshToken", "refresh-1");
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(json({ ...PROFILE, roles: ["ROLE_COMPANY"] })));

    renderProbe();

    await waitFor(() => expect(screen.getByTestId("isCompany")).toHaveTextContent("true"));
  });

  it("throws a clear error when used outside the provider", () => {
    const quiet = vi.spyOn(console, "error").mockImplementation(() => {});
    expect(() => render(<Probe />)).toThrow(/useAuth must be used inside/);
    quiet.mockRestore();
  });
});
