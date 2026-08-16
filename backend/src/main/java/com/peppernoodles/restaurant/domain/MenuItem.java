package com.peppernoodles.restaurant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A menu photo. Legacy entity: {@code MenuDetail}, which held a BLOB and nothing
 * else — a menu page could be neither labelled nor ordered.
 */
@Entity
@Table(name = "restaurant_menu_items")
@Getter
@Setter
@NoArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column
    private String caption;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    /** Display order within the restaurant's menu. */
    @Column(nullable = false)
    private int position;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    public MenuItem(String imagePath, String caption, int position) {
        this.imagePath = imagePath;
        this.caption = caption;
        this.position = position;
    }
}
