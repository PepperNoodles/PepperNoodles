package com.peppernoodles.common.storage;

/** The Storage buckets created by {@code 20260816120700_storage_buckets.sql}. */
public enum StorageBucket {
    RESTAURANT_PHOTOS("restaurant-photos"),
    MENU_PHOTOS("menu-photos"),
    EVENT_PHOTOS("event-photos"),
    PRODUCT_PHOTOS("product-photos"),
    USER_AVATARS("user-avatars"),
    FORUM_IMAGES("forum-images");

    private final String bucketName;

    StorageBucket(String bucketName) {
        this.bucketName = bucketName;
    }

    public String bucketName() {
        return bucketName;
    }
}
