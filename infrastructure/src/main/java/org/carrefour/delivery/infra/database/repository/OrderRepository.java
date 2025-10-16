package org.carrefour.delivery.infra.database.repository;

import org.carrefour.delivery.infra.database.model.OrderEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Repository interface for managing {@link OrderEntity} entities.
 *
 * This interface provides methods for performing CRUD operations on
 * orders, specifically in a reactive manner using Spring Data.
 */
@Repository
public interface OrderRepository extends ReactiveCrudRepository<OrderEntity, Long> {

    /**
     * Finds all orders associated with a specific time slot ID.
     *
     * @param timeSlotId the ID of the time slot for which to find orders
     * @return a Flux containing all orders associated with the given time slot ID
     */
    Flux<OrderEntity> findByTimeSlotId(Long timeSlotId);
}
