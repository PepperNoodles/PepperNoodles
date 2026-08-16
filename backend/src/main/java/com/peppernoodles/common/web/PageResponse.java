package com.peppernoodles.common.web;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;

/**
 * The single pagination envelope for every list endpoint.
 *
 * <p>The legacy controllers each invented their own shape — some returned a bare
 * list plus a {@code TotalPages} entry in a {@code Map<String, Object>}, others
 * stuffed a {@code totalpage} field onto the entity itself.
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last) {

    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast());
    }

    /** Maps the page's entities to DTOs while preserving the pagination metadata. */
    public static <E, T> PageResponse<T> of(Page<E> page, Function<E, T> mapper) {
        return of(page.map(mapper));
    }
}
