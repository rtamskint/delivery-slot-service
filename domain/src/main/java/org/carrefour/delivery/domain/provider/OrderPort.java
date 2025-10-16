package org.carrefour.delivery.domain.provider;

import org.carrefour.delivery.domain.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderPort {
    Flux<Order> findOrderByTimeSlotId(long slotId);

    Mono<Void> deleteOrderById(long slotId);

    Mono<Order> saveOrder(Order order);

}
