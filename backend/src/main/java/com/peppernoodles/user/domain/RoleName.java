package com.peppernoodles.user.domain;

/** The three authorities the platform recognises. Mirrors the seeded {@code roles} rows. */
public enum RoleName {

    /** 一般會員 — consumer account. */
    ROLE_USER,

    /** 企業會員 — restaurant owner. */
    ROLE_COMPANY,

    /** 系統管理員 — back-office administrator. */
    ROLE_ADMIN
}
