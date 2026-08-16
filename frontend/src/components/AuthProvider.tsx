"use client";

import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";
import { api, tokenStore } from "@/lib/api";
import type { AuthResponse, AuthUser, Role } from "@/lib/types";

interface AuthContextValue {
  user: AuthUser | null;
  loading: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  refreshUser: () => Promise<void>;
  hasRole: (...roles: Role[]) => boolean;
}

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [loading, setLoading] = useState(true);

  const refreshUser = useCallback(async () => {
    if (!tokenStore.access && !tokenStore.refresh) {
      setUser(null);
      setLoading(false);
      return;
    }
    try {
      // /users/me returns the profile; project it onto the compact session shape.
      const profile = await api.get<{
        userId: number;
        email: string;
        nickname?: string | null;
        realName?: string | null;
        avatarUrl?: string | null;
        roles: Role[];
      }>("/users/me");

      setUser({
        id: profile.userId,
        email: profile.email,
        displayName: profile.nickname ?? profile.realName ?? profile.email,
        avatarPath: profile.avatarUrl,
        roles: profile.roles,
      });
    } catch {
      tokenStore.clear();
      setUser(null);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    void refreshUser();
  }, [refreshUser]);

  const login = useCallback(async (email: string, password: string) => {
    const auth = await api.post<AuthResponse>("/auth/login", { email, password }, { anonymous: true });
    tokenStore.save(auth);
    setUser(auth.user);
  }, []);

  const logout = useCallback(async () => {
    const refreshToken = tokenStore.refresh;
    if (refreshToken) {
      // Best effort: the local session ends regardless of what the server says.
      await api.post("/auth/logout", { refreshToken }, { anonymous: true }).catch(() => undefined);
    }
    tokenStore.clear();
    setUser(null);
  }, []);

  const value = useMemo<AuthContextValue>(
    () => ({
      user,
      loading,
      login,
      logout,
      refreshUser,
      hasRole: (...roles: Role[]) => !!user && roles.some((r) => user.roles.includes(r)),
    }),
    [user, loading, login, logout, refreshUser],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used inside <AuthProvider>");
  return context;
}
