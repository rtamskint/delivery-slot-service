package org.carrefour.delivery.common.mapper;

public interface TwoWayMapper<S, T> extends OneWayMapper<S, T> {
    S inverseMap(T source);
}
