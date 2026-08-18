import { beforeEach, describe, expect, it, vi } from "vitest";
import { ApiError, api, query, tokenStore } from "./api";

const BASE = "http://localhost:8080/api/v1";

/** Builds a fetch Response without pulling in a server. */
function json(body: unknown, status = 200) {
  return new Response(JSON.stringify(body), {
    status,
    headers: { "content-type": "application/json" },
  });
}

function noContent() {
  return new Response(null, { status: 204 });
}

function authResponse(suffix: string) {
  return {
    accessToken: `access-${suffix}`,
    refreshToken: `refresh-${suffix}`,
    tokenType: "Bearer",
    expiresIn: 900,
    expiresAt: "2026-08-18T00:00:00Z",
    user: { id: 1, email: "a@example.com", displayName: "A", roles: ["ROLE_USER"] },
  };
}

describe("query", () => {
  it("omits empty values and keeps zero and false", () => {
    expect(query({ a: 1, b: "", c: null, d: undefined, e: 0, f: false })).toBe("?a=1&e=0&f=false");
  });

  it("repeats a key for each array element", () => {
    expect(query({ tagIds: [1, 2, 3] })).toBe("?tagIds=1&tagIds=2&tagIds=3");
  });

  it("returns an empty string when nothing survives", () => {
    expect(query({ a: undefined, b: "" })).toBe("");
  });

  it("encodes values", () => {
    expect(query({ q: "小籠包" })).toContain(encodeURIComponent("小籠包"));
  });
});

describe("api", () => {
  beforeEach(() => {
    localStorage.clear();
  });

  it("sends the access token when one is stored", async () => {
    tokenStore.save(authResponse("1"));
    const fetchMock = vi.fn().mockResolvedValue(json({ ok: true }));
    vi.stubGlobal("fetch", fetchMock);

    await api.get("/users/me");

    const headers = fetchMock.mock.calls[0][1].headers as Headers;
    expect(headers.get("Authorization")).toBe("Bearer access-1");
  });

  it("omits the token on anonymous calls", async () => {
    tokenStore.save(authResponse("1"));
    const fetchMock = vi.fn().mockResolvedValue(json([]));
    vi.stubGlobal("fetch", fetchMock);

    await api.get("/food-tags", { anonymous: true });

    const headers = fetchMock.mock.calls[0][1].headers as Headers;
    expect(headers.get("Authorization")).toBeNull();
  });

  it("returns undefined for 204 rather than trying to parse a body", async () => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(noContent()));

    await expect(api.delete("/cart")).resolves.toBeUndefined();
  });

  it("throws ApiError carrying the RFC 7807 body", async () => {
    vi.stubGlobal(
      "fetch",
      vi.fn().mockResolvedValue(
        json(
          {
            type: "about:blank",
            title: "Conflict",
            status: 409,
            detail: "庫存不足",
            instance: "/api/v1/orders/checkout",
            code: "conflict",
            timestamp: "2026-08-18T00:00:00Z",
          },
          409,
        ),
      ),
    );

    await expect(api.post("/orders/checkout", {})).rejects.toMatchObject({
      status: 409,
      message: "庫存不足",
    });
  });

  it("exposes field errors for form rendering", async () => {
    vi.stubGlobal(
      "fetch",
      vi.fn().mockResolvedValue(
        json({ status: 422, detail: "驗證失敗", errors: { email: "格式不正確" } }, 422),
      ),
    );

    const error = await api.post("/auth/register", {}).catch((e) => e as ApiError);
    expect(error).toBeInstanceOf(ApiError);
    expect((error as ApiError).fieldErrors).toEqual({ email: "格式不正確" });
  });

  it("still throws when the error body is not JSON", async () => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(new Response("gateway down", { status: 502 })));

    await expect(api.get("/restaurants")).rejects.toMatchObject({ status: 502 });
  });

  it("does not set a JSON content type for FormData uploads", async () => {
    tokenStore.save(authResponse("1"));
    const fetchMock = vi.fn().mockResolvedValue(json({ imageUrl: "x" }));
    vi.stubGlobal("fetch", fetchMock);

    const body = new FormData();
    body.append("file", new Blob(["x"]), "x.png");
    await api.post("/shop/products/1/image", body);

    const headers = fetchMock.mock.calls[0][1].headers as Headers;
    // Setting it manually would clobber the multipart boundary the browser adds.
    expect(headers.get("Content-Type")).toBeNull();
  });

  describe("token refresh", () => {
    it("refreshes once on 401 and replays the original request", async () => {
      tokenStore.save(authResponse("old"));

      const fetchMock = vi
        .fn()
        .mockResolvedValueOnce(json({ detail: "expired" }, 401))
        .mockResolvedValueOnce(json(authResponse("new")))
        .mockResolvedValueOnce(json({ nickname: "小美" }));
      vi.stubGlobal("fetch", fetchMock);

      await expect(api.get("/users/me")).resolves.toEqual({ nickname: "小美" });

      expect(fetchMock).toHaveBeenCalledTimes(3);
      expect(fetchMock.mock.calls[1][0]).toBe(`${BASE}/auth/refresh`);
      expect(tokenStore.access).toBe("access-new");

      // The replay must carry the new token, not the expired one.
      const replayHeaders = fetchMock.mock.calls[2][1].headers as Headers;
      expect(replayHeaders.get("Authorization")).toBe("Bearer access-new");
    });

    /**
     * The backend treats a reused refresh token as theft and revokes the whole
     * family. Without single-flight, three requests expiring together would each
     * POST the same token and log the user out.
     */
    it("issues exactly one refresh when several requests 401 at once", async () => {
      tokenStore.save(authResponse("old"));

      const fetchMock = vi.fn().mockImplementation((url: string) => {
        if (url.endsWith("/auth/refresh")) {
          return Promise.resolve(json(authResponse("new")));
        }
        // Every protected call fails until the token has been replaced.
        return Promise.resolve(
          tokenStore.access === "access-new" ? json({ ok: true }) : json({ detail: "expired" }, 401),
        );
      });
      vi.stubGlobal("fetch", fetchMock);

      const results = await Promise.all([
        api.get("/users/me"),
        api.get("/cart"),
        api.get("/orders"),
      ]);

      expect(results).toEqual([{ ok: true }, { ok: true }, { ok: true }]);

      const refreshCalls = fetchMock.mock.calls.filter((c) => String(c[0]).endsWith("/auth/refresh"));
      expect(refreshCalls).toHaveLength(1);
    });

    it("clears the session and gives up when the refresh itself fails", async () => {
      tokenStore.save(authResponse("old"));

      vi.stubGlobal(
        "fetch",
        vi.fn().mockImplementation((url: string) =>
          Promise.resolve(
            url.endsWith("/auth/refresh")
              ? json({ detail: "revoked" }, 401)
              : json({ detail: "expired" }, 401),
          ),
        ),
      );

      await expect(api.get("/users/me")).rejects.toMatchObject({ status: 401 });
      expect(tokenStore.access).toBeNull();
      expect(tokenStore.refresh).toBeNull();
    });

    it("does not retry when there is no refresh token to use", async () => {
      const fetchMock = vi.fn().mockResolvedValue(json({ detail: "unauthorised" }, 401));
      vi.stubGlobal("fetch", fetchMock);

      await expect(api.get("/users/me")).rejects.toMatchObject({ status: 401 });
      expect(fetchMock).toHaveBeenCalledTimes(1);
    });

    it("does not try to refresh an anonymous request", async () => {
      tokenStore.save(authResponse("old"));
      const fetchMock = vi.fn().mockResolvedValue(json({ detail: "nope" }, 401));
      vi.stubGlobal("fetch", fetchMock);

      await expect(api.get("/forum/posts", { anonymous: true })).rejects.toMatchObject({ status: 401 });
      expect(fetchMock).toHaveBeenCalledTimes(1);
    });

    it("gives up after one retry instead of looping forever", async () => {
      tokenStore.save(authResponse("old"));

      // Refresh keeps succeeding but the resource keeps 401ing.
      const fetchMock = vi.fn().mockImplementation((url: string) =>
        Promise.resolve(
          url.endsWith("/auth/refresh") ? json(authResponse("new")) : json({ detail: "still 401" }, 401),
        ),
      );
      vi.stubGlobal("fetch", fetchMock);

      await expect(api.get("/users/me")).rejects.toMatchObject({ status: 401 });

      const refreshCalls = fetchMock.mock.calls.filter((c) => String(c[0]).endsWith("/auth/refresh"));
      expect(refreshCalls).toHaveLength(1);
    });
  });
});
