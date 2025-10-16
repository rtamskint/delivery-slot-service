package org.carrefour.delivery.common.mapper;

/**
 *
 * @param <S> source
 * @param <T> target
 */
public interface OneWayMapper<S, T> {
    T map(S source);
}
