package com.peppernoodles.shop.domain;

/** 訂單狀態. Legacy stored the Chinese label 已付款 in a free-text column. */
public enum OrderStatus {
    /** Awaiting payment, still inside its hold window. */
    PENDING,
    PAID,
    /** Cancelled by the buyer before payment. */
    CANCELLED,
    /** 訂單保留逾時 — swept by the scheduled job. */
    EXPIRED
}
