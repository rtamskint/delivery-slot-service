package org.carrefour.delivery.infra.database.repository;

import org.carrefour.delivery.common.model.DeliveryMode;
import org.carrefour.delivery.infra.database.model.TimeSlotEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Repository interface for performing CRUD operations on TimeSlot entities.
 */
@Repository
public interface TimeSlotRepository extends ReactiveCrudRepository<TimeSlotEntity, Long> {

    /**
     * Retrieves all available time slots for a specific delivery method.
     *
     * @param deliveryMode the delivery method.
     * @return a Flux of available time slots.
     */
    Flux<TimeSlotEntity> findByDeliveryMethod(DeliveryMode deliveryMode);
}
