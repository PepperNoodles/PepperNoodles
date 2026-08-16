package com.peppernoodles.shop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 自動取消未付款訂單.
 *
 * <p>Replaces the legacy implementation, which spawned a custom thread pool and
 * scheduled a per-order cancellation task in memory — so a restart lost every
 * pending timer and those orders held their stock forever. This sweep is
 * stateless and recovers on its own.
 */
@Component
public class OrderExpirySweeper {

    private static final Logger log = LoggerFactory.getLogger(OrderExpirySweeper.class);

    private final OrderService orderService;

    public OrderExpirySweeper(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "${peppernoodles.orders.expiry-sweep-cron}")
    public void sweep() {
        try {
            orderService.expireOverdueOrders();
        } catch (Exception e) {
            // Never let a failure kill the scheduler thread.
            log.error("Order expiry sweep failed", e);
        }
    }
}
