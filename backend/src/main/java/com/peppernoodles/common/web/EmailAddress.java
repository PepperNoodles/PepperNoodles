package com.peppernoodles.common.web;

import java.util.Locale;

/**
 * Canonical form for e-mail addresses.
 *
 * <p>Addresses are stored lower-cased and a database CHECK constraint enforces
 * it, so every write path must normalise first. Doing it here rather than with
 * {@code citext} keeps the column a plain {@code text} that Hibernate validates
 * and a plain Postgres test container understands.
 */
public final class EmailAddress {

    private EmailAddress() {}

    /** Trims surrounding whitespace and lower-cases. Returns {@code null} for {@code null}. */
    public static String normalise(String raw) {
        return raw == null ? null : raw.trim().toLowerCase(Locale.ROOT);
    }
}
