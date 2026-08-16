package com.peppernoodles.payment.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * ECPay 綠界 CheckMacValue.
 *
 * <p>Implements the published algorithm directly rather than vendoring ECPay's
 * SDK, which the legacy project copied into {@code src/main/java/ecpay} as 36
 * files and which pulled in log4j 1.2.17.
 *
 * <p>The algorithm: sort parameters by key (case-insensitive), join as
 * {@code k=v&…}, wrap with the HashKey and HashIV, URL-encode, lower-case, apply
 * ECPay's .NET-compatible character substitutions, SHA-256, then upper-case.
 */
public final class EcpayCheckMac {

    private EcpayCheckMac() {}

    public static String compute(Map<String, String> params, String hashKey, String hashIv) {
        String ordered = new TreeMap<>(String.CASE_INSENSITIVE_ORDER) {
                    {
                        params.forEach((k, v) -> {
                            if (!"CheckMacValue".equalsIgnoreCase(k)) {
                                put(k, v);
                            }
                        });
                    }
                }
                .entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + (e.getValue() == null ? "" : e.getValue()))
                .collect(Collectors.joining("&"));

        String raw = "HashKey=" + hashKey + "&" + ordered + "&HashIV=" + hashIv;
        String encoded = dotNetUrlEncode(raw).toLowerCase(Locale.ROOT);
        return sha256Hex(encoded).toUpperCase(Locale.ROOT);
    }

    /** True when the callback's own CheckMacValue matches what we compute. */
    public static boolean verify(Map<String, String> params, String hashKey, String hashIv) {
        String provided = params.get("CheckMacValue");
        if (provided == null) {
            return false;
        }
        String expected = compute(params, hashKey, hashIv);
        // Constant-time comparison: this value authenticates the callback.
        return MessageDigest.isEqual(
                provided.getBytes(StandardCharsets.UTF_8), expected.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * ECPay expects .NET's {@code HttpUtility.UrlEncode} output, which differs
     * from Java's in a handful of characters.
     */
    private static String dotNetUrlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8)
                .replace("%2d", "-")
                .replace("%2D", "-")
                .replace("%5f", "_")
                .replace("%5F", "_")
                .replace("%2e", ".")
                .replace("%2E", ".")
                .replace("%21", "!")
                .replace("%2a", "*")
                .replace("%2A", "*")
                .replace("%28", "(")
                .replace("%29", ")");
    }

    private static String sha256Hex(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                hex.append("%02x".formatted(b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is required by every JVM", e);
        }
    }
}
