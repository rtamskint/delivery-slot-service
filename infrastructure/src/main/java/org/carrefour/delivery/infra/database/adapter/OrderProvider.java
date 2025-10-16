package org.carrefour.delivery.infra.database.adapter;

import org.carrefour.delivery.domain.model.Order;
import org.carrefour.delivery.domain.provider.OrderPort;
import org.carrefour.delivery.infra.database.mapper.OrderMapper;
import org.carrefour.delivery.infra.database.model.OrderEntity;
import org.carrefour.delivery.infra.database.repository.OrderRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Transactional(readOnly = true)
public class OrderProvider implements OrderPort {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderProvider(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Flux<Order> findOrderByTimeSlotId(long slotId) {
        Flux<OrderEntity> orderEntityFlux = orderRepository.findByTimeSlotId(slotId);
        return orderEntityFlux.map(orderMapper::map);
    }

    @Override
    @Transactional
    public Mono<Void> deleteOrderById(long orderId) {
        return orderRepository.deleteById(orderId);
    }

    @Override
    @Transactional
    public Mono<Order> saveOrder(Order order) {
        Mono<OrderEntity> orderEntityMono = orderRepository.save(orderMapper.inverseMap(order));
        return orderEntityMono.map(orderMapper::map);
    }
}
