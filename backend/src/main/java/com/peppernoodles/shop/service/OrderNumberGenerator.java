package com.peppernoodles.shop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

/**
 * Produces order numbers of the form {@code PN20260817-000042}.
 *
 * <p>Backed by the {@code order_no_seq} Postgres sequence. The first version
 * counted existing rows to derive the next value, which is a read-then-write
 * race — two simultaneous checkouts both computed the same number and the
 * second insert failed on the unique constraint. A sequence allocates outside
 * transaction isolation, so concurrent callers can never collide, and a
 * rolled-back checkout simply leaves a gap.
 */
@Component
public class OrderNumberGenerator {

    private static final ZoneId TAIPEI = ZoneId.of("Asia/Taipei");
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyyMMdd");

    @PersistenceContext
    private EntityManager entityManager;

    public String next() {
        Number sequence = (Number) entityManager
                .createNativeQuery("select nextval('public.order_no_seq')")
                .getSingleResult();
        return "PN%s-%06d".formatted(LocalDate.now(TAIPEI).format(DAY), sequence.longValue());
    }
}
