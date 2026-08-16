package com.peppernoodles.shop.api;

import com.peppernoodles.common.security.AuthenticatedUser;
import com.peppernoodles.common.security.CurrentUser;
import com.peppernoodles.common.web.PageResponse;
import com.peppernoodles.shop.api.dto.CheckoutRequest;
import com.peppernoodles.shop.api.dto.OrderDto;
import com.peppernoodles.shop.api.dto.SalesReportDto;
import com.peppernoodles.shop.service.OrderService;
import com.peppernoodles.shop.service.SalesReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Orders", description = "訂單、結帳、業績報表")
public class OrderController {

    private final OrderService orderService;
    private final SalesReportService salesReportService;

    public OrderController(OrderService orderService, SalesReportService salesReportService) {
        this.orderService = orderService;
        this.salesReportService = salesReportService;
    }

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "結帳",
            description = "Converts the cart into a PENDING order and reserves stock under a row lock.")
    public OrderDto checkout(
            @Valid @RequestBody CheckoutRequest request, @CurrentUser AuthenticatedUser caller) {
        return orderService.checkout(caller.id(), request);
    }

    @GetMapping
    @Operation(summary = "我的訂單")
    public PageResponse<OrderDto> myOrders(
            @CurrentUser AuthenticatedUser caller, @PageableDefault(size = 10) Pageable pageable) {
        return orderService.listMine(caller.id(), pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "訂單明細")
    public OrderDto get(@PathVariable Long id, @CurrentUser AuthenticatedUser caller) {
        return orderService.get(id, caller);
    }

    @PostMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "取消訂單", description = "Only while unpaid. Reserved stock is returned.")
    public void cancel(@PathVariable Long id, @CurrentUser AuthenticatedUser caller) {
        orderService.cancel(id, caller);
    }

    @GetMapping("/reports/sales")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY', 'ROLE_ADMIN')")
    @Operation(summary = "業績報表", description = "Daily and monthly revenue plus best sellers.")
    public SalesReportDto salesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "10") int topN) {
        return salesReportService.report(from, to, topN);
    }
}
