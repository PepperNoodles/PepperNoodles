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
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * One opening interval on one day. Legacy entity: {@code RestaurantBusinHour}.
 *
 * <p>The legacy row held up to three open/close pairs in wide string columns
 * ({@code open_time}, {@code open_time_2nd}, {@code open_time_3rd}), so a fourth
 * service period was impossible and every comparison was a string comparison.
 *
 * <p>{@code dayOfWeek} is stored as 0 = Sunday … 6 = Saturday to match the
 * JavaScript {@code Date#getDay} the frontend uses. {@link #getDay()} converts
 * to {@link DayOfWeek} for server-side logic.
 */
@Entity
@Table(name = "restaurant_business_hours")
@Getter
@Setter
@NoArgsConstructor
public class BusinessHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    /** 0 = Sunday … 6 = Saturday. */
    @Column(name = "day_of_week", nullable = false)
    private short dayOfWeek;

    @Column(name = "opens_at", nullable = false)
    private LocalTime opensAt;

    @Column(name = "closes_at", nullable = false)
    private LocalTime closesAt;

    public BusinessHour(short dayOfWeek, LocalTime opensAt, LocalTime closesAt) {
        this.dayOfWeek = dayOfWeek;
        this.opensAt = opensAt;
        this.closesAt = closesAt;
    }

    public DayOfWeek getDay() {
        // DayOfWeek is 1 = Monday … 7 = Sunday.
        return dayOfWeek == 0 ? DayOfWeek.SUNDAY : DayOfWeek.of(dayOfWeek);
    }

    public boolean covers(LocalTime time) {
        return !time.isBefore(opensAt) && time.isBefore(closesAt);
    }
}
