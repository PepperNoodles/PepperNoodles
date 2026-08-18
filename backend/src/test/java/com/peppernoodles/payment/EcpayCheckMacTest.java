package com.peppernoodles.payment;

import static org.assertj.core.api.Assertions.assertThat;

import com.peppernoodles.payment.service.EcpayCheckMac;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The CheckMacValue algorithm, tested without Spring or a database.
 *
 * <p>This is the one piece of the payment flow that must match a third party
 * byte for byte: a wrong signature is silently rejected by ECPay, and the
 * callback verification is what stops anyone marking an order paid.
 */
class EcpayCheckMacTest {

    private static final String HASH_KEY = "5294y06JbISpM5x9";
    private static final String HASH_IV = "v77hoKGq4kWxNNIS";

    private static Map<String, String> sampleOrder() {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("MerchantID", "2000132");
        params.put("MerchantTradeNo", "PN20260817000001");
        params.put("MerchantTradeDate", "2026/08/17 12:00:00");
        params.put("PaymentType", "aio");
        params.put("TotalAmount", "920");
        params.put("TradeDesc", "PepperNoodles Order");
        params.put("ItemName", "冷凍小籠包 20入 x2");
        params.put("ReturnURL", "http://localhost:8080/api/v1/payments/ecpay/callback");
        params.put("ChoosePayment", "ALL");
        params.put("EncryptType", "1");
        return params;
    }

    @Test
    @DisplayName("produces a 64-character uppercase SHA-256 digest")
    void producesUppercaseSha256() {
        String mac = EcpayCheckMac.compute(sampleOrder(), HASH_KEY, HASH_IV);

        assertThat(mac).hasSize(64).matches("[0-9A-F]{64}");
    }

    @Test
    @DisplayName("is independent of the order the parameters were added in")
    void isOrderIndependent() {
        Map<String, String> forward = sampleOrder();
        Map<String, String> reversed = new LinkedHashMap<>();
        forward.entrySet().stream()
                .sorted((a, b) -> b.getKey().compareTo(a.getKey()))
                .forEach(e -> reversed.put(e.getKey(), e.getValue()));

        assertThat(EcpayCheckMac.compute(reversed, HASH_KEY, HASH_IV))
                .isEqualTo(EcpayCheckMac.compute(forward, HASH_KEY, HASH_IV));
    }

    @Test
    @DisplayName("changes when any single value changes")
    void isSensitiveToEveryValue() {
        String original = EcpayCheckMac.compute(sampleOrder(), HASH_KEY, HASH_IV);

        Map<String, String> tampered = sampleOrder();
        tampered.put("TotalAmount", "1");

        assertThat(EcpayCheckMac.compute(tampered, HASH_KEY, HASH_IV)).isNotEqualTo(original);
    }

    @Test
    @DisplayName("ignores any CheckMacValue already present in the map")
    void ignoresExistingCheckMacValue() {
        Map<String, String> withMac = sampleOrder();
        String expected = EcpayCheckMac.compute(withMac, HASH_KEY, HASH_IV);
        withMac.put("CheckMacValue", "STALE");

        assertThat(EcpayCheckMac.compute(withMac, HASH_KEY, HASH_IV)).isEqualTo(expected);
    }

    @Test
    @DisplayName("verify accepts a correctly signed callback")
    void verifyAcceptsGoodSignature() {
        Map<String, String> callback = sampleOrder();
        callback.put("RtnCode", "1");
        callback.put("CheckMacValue", EcpayCheckMac.compute(callback, HASH_KEY, HASH_IV));

        assertThat(EcpayCheckMac.verify(callback, HASH_KEY, HASH_IV)).isTrue();
    }

    /** Without this an attacker could POST a fake "paid" callback. */
    @Test
    @DisplayName("verify rejects a tampered amount")
    void verifyRejectsTamperedCallback() {
        Map<String, String> callback = sampleOrder();
        callback.put("RtnCode", "1");
        callback.put("CheckMacValue", EcpayCheckMac.compute(callback, HASH_KEY, HASH_IV));

        callback.put("TotalAmount", "1");

        assertThat(EcpayCheckMac.verify(callback, HASH_KEY, HASH_IV)).isFalse();
    }

    @Test
    @DisplayName("verify rejects a callback with no signature at all")
    void verifyRejectsMissingSignature() {
        assertThat(EcpayCheckMac.verify(sampleOrder(), HASH_KEY, HASH_IV)).isFalse();
    }

    @Test
    @DisplayName("verify rejects a signature made with the wrong merchant key")
    void verifyRejectsWrongKey() {
        Map<String, String> callback = sampleOrder();
        callback.put("CheckMacValue", EcpayCheckMac.compute(callback, "WRONGKEY12345678", HASH_IV));

        assertThat(EcpayCheckMac.verify(callback, HASH_KEY, HASH_IV)).isFalse();
    }
}
