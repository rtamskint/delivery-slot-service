package org.carrefour.delivery.domain.provider;

import org.carrefour.delivery.common.model.DeliveryMode;
import org.carrefour.delivery.domain.model.TimeSlot;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TimeSlotPort {
    Mono<TimeSlot> findTimeSlotById(long slotId);

    Flux<TimeSlot> findTimeSlotByDeliveryMethod(DeliveryMode deliveryMode);
}
