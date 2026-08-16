package com.peppernoodles.shop.domain;

/**
 * Listing state.
 *
 * <p>The legacy schema stored the Chinese labels 上架中 / 下架中 directly in the
 * column and compared them as string literals scattered through the queries.
 */
public enum ProductStatus {
    /** 上架中 */
    LISTED,
    /** 下架中 */
    DELISTED
}
