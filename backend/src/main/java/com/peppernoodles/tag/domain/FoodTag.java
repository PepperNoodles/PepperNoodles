package com.peppernoodles.tag.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A cuisine or interest tag (日式, 火鍋, 素食 …), shared by users, restaurants,
 * products, and forum posts. Legacy entity: {@code FoodTag}.
 *
 * <p>Each of the four legacy join tables carried its own surrogate key and no
 * uniqueness constraint; they are now plain composite-key tables mapped as
 * {@code @ManyToMany} from the owning side.
 */
@Entity
@Table(name = "food_tags")
@Getter
@Setter
@NoArgsConstructor
public class FoodTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    public FoodTag(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        // Compare on the natural key so tags behave correctly inside a Set.
        return other instanceof FoodTag tag && name != null && name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
