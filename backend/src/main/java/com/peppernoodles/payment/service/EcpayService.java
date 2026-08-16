package com.peppernoodles.payment.service;

import com.peppernoodles.common.config.ApplicationProperties;
import com.peppernoodles.common.error.ApiExceptions.ConflictException;
import com.peppernoodles.common.error.ApiExceptions.ForbiddenException;
import com.peppernoodles.common.error.ApiExceptions.NotFoundException;
import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.shop.domain.Order;
import com.peppernoodles.shop.domain.OrderStatus;
import com.peppernoodles.shop.domain.Payment;
import com.peppernoodles.shop.repository.OrderRepository;
import com.peppernoodles.shop.repository.PaymentRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 綠界金流 integration. */
@Service
public class EcpayService {

    private static final Logger log = LoggerFactory.getLogger(EcpayService.class);
    private static final ZoneId TAIPEI = ZoneId.of("Asia/Taipei");
    private static final DateTimeFormatter TRADE_DATE = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private static final String STAGE_URL =
            "https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5";
    private static final String PRODUCTION_URL = "https://payment.ecpay.com.tw/Cashier/AioCheckOut/V5";

    private final OrderRepository orders;
    private final PaymentRepository payments;
    private final ApplicationProperties properties;

    public EcpayService(
            OrderRepository orders, PaymentRepository payments, ApplicationProperties properties) {
        this.orders = orders;
        this.payments = payments;
        this.properties = properties;
    }

    /**
     * Builds the parameters the browser posts to ECPay.
     *
     * <p>Returned to the client rather than rendered as an auto-submitting HTML
     * form (which is what the legacy JSP did), so the frontend controls the flow.
     */
    @Transactional
    public EcpayCheckoutForm prepareCheckout(Long orderId, AuthenticatedUser caller) {
        var config = properties.ecpay();
        if (!config.enabled()) {
            throw new ConflictException("金流功能尚未啟用。");
        }

        Order order = orders.findDetailedById(orderId).orElseThrow(() -> NotFoundException.of("訂單", orderId));
        if (!order.isOwnedBy(caller.id())) {
            throw new ForbiddenException("您沒有權限支付這筆訂單。");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ConflictException("這筆訂單目前無法付款。");
        }

        // ECPay's MerchantTradeNo is limited to 20 alphanumeric characters.
        String tradeNo = order.getOrderNo().replace("-", "");
        Payment payment = payments
                .findByMerchantTradeNo(tradeNo)
                .orElseGet(() -> payments.save(new Payment(order, tradeNo, order.getTotalCost())));

        String itemNames = order.getItems().stream()
                .map(i -> "%s x%d".formatted(i.getProductName(), i.getQuantity()))
                .collect(Collectors.joining("#"));

        Map<String, String> params = new LinkedHashMap<>();
        params.put("MerchantID", config.merchantId());
        params.put("MerchantTradeNo", payment.getMerchantTradeNo());
        params.put("MerchantTradeDate", LocalDateTime.now(TAIPEI).format(TRADE_DATE));
        params.put("PaymentType", "aio");
        params.put("TotalAmount", String.valueOf(order.getTotalCost().longValue()));
        params.put("TradeDesc", "PepperNoodles Order");
        params.put("ItemName", itemNames.length() > 400 ? itemNames.substring(0, 400) : itemNames);
        params.put("ReturnURL", config.returnUrl());
        params.put("ClientBackURL", config.clientBackUrl() + "/" + order.getId());
        params.put("ChoosePayment", "ALL");
        params.put("EncryptType", "1");
        params.put("CheckMacValue", EcpayCheckMac.compute(params, config.hashKey(), config.hashIv()));

        String action = "Production".equalsIgnoreCase(config.mode()) ? PRODUCTION_URL : STAGE_URL;
        return new EcpayCheckoutForm(action, params);
    }

    /**
     * Handles ECPay's server-to-server callback.
     *
     * <p>Authenticated by CheckMacValue, not by a session — the request comes
     * from ECPay, not the buyer. Idempotent, because gateways retry.
     *
     * @return the literal body ECPay expects
     */
    @Transactional
    public String handleCallback(Map<String, String> params) {
        var config = properties.ecpay();

        if (!EcpayCheckMac.verify(params, config.hashKey(), config.hashIv())) {
            log.warn("Rejected an ECPay callback with a bad CheckMacValue: {}", params.get("MerchantTradeNo"));
            return "0|CheckMacValue verify fail";
        }

        String tradeNo = params.get("MerchantTradeNo");
        Payment payment = payments.findByMerchantTradeNo(tradeNo).orElse(null);
        if (payment == null) {
            log.warn("ECPay callback for unknown trade {}", tradeNo);
            return "0|Unknown trade";
        }

        payment.setRawCallback(toJson(params));
        boolean succeeded = "1".equals(params.get("RtnCode"));

        if (succeeded) {
            payment.setStatus(Payment.Status.SUCCEEDED);
            payment.getOrder().markPaid();
            log.info("Order {} paid via ECPay", payment.getOrder().getOrderNo());
        } else {
            payment.setStatus(Payment.Status.FAILED);
            log.warn("ECPay reported failure for {}: {}", tradeNo, params.get("RtnMsg"));
        }

        return "1|OK";
    }

    private static String toJson(Map<String, String> params) {
        return params.entrySet().stream()
                .map(e -> "\"%s\":\"%s\"".formatted(escape(e.getKey()), escape(e.getValue())))
                .collect(Collectors.joining(",", "{", "}"));
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /** Everything the browser needs to post the buyer to ECPay. */
    public record EcpayCheckoutForm(String action, Map<String, String> fields) {}
}
