package com.peppernoodles.payment.api;

import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.CurrentUser;
import com.peppernoodles.payment.service.EcpayService;
import com.peppernoodles.payment.service.EcpayService.EcpayCheckoutForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "綠界金流")
public class PaymentController {

    private final EcpayService ecpayService;

    public PaymentController(EcpayService ecpayService) {
        this.ecpayService = ecpayService;
    }

    @PostMapping("/ecpay/orders/{orderId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "建立綠界付款表單",
            description = "Returns the action URL and signed fields for the browser to POST to ECPay.")
    public EcpayCheckoutForm prepare(@PathVariable Long orderId, @CurrentUser AuthenticatedUser caller) {
        return ecpayService.prepareCheckout(orderId, caller);
    }

    /**
     * ECPay's server-to-server notification.
     *
     * <p>Unauthenticated by design — the caller is ECPay, and the request is
     * authenticated by its CheckMacValue signature instead. Must return exactly
     * {@code 1|OK} or ECPay keeps retrying.
     */
    @PostMapping(path = "/ecpay/callback", consumes = "application/x-www-form-urlencoded")
    @Operation(summary = "綠界付款結果通知", description = "Called by ECPay, verified by CheckMacValue.")
    public String callback(@RequestParam Map<String, String> params) {
        return ecpayService.handleCallback(params);
    }
}
