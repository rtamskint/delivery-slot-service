package org.carrefour.delivery.domain.usecase;

import org.carrefour.delivery.common.model.DeliveryMode;
import org.carrefour.delivery.domain.model.TimeSlot;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeliveryUseCase {
    Flux<TimeSlot> getAvailableTimeSlots(DeliveryMode deliveryMode);

    Mono<TimeSlot> bookTimeSlot(Long slotId, String orderNumber);

    Mono<TimeSlot> cancelBookingTimeSlot(Long slotId, String orderNumber);
}
