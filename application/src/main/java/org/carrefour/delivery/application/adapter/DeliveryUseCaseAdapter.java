package org.carrefour.delivery.application.adapter;

import org.carrefour.delivery.application.dto.TimeSlotDTO;
import org.carrefour.delivery.application.mapper.TimeSlotDTOMapper;
import org.carrefour.delivery.common.model.DeliveryMode;
import org.carrefour.delivery.domain.usecase.DeliveryUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DeliveryUseCaseAdapter {

    private final DeliveryUseCase deliveryUseCase;
    private final TimeSlotDTOMapper timeSlotDTOMapper;

    public DeliveryUseCaseAdapter(DeliveryUseCase deliveryUseCase, TimeSlotDTOMapper timeSlotDTOMapper) {
        this.deliveryUseCase = deliveryUseCase;
        this.timeSlotDTOMapper = timeSlotDTOMapper;
    }

    public Flux<TimeSlotDTO> getAvailableTimeSlots(DeliveryMode deliveryMode) {
        return deliveryUseCase.getAvailableTimeSlots(deliveryMode).map(timeSlotDTOMapper::map);
    }

    public Mono<TimeSlotDTO> bookTimeSlot(Long slotId, String orderNumber) {
        return deliveryUseCase.bookTimeSlot(slotId, orderNumber).map(timeSlotDTOMapper::map);
    }

    public Mono<TimeSlotDTO> cancelBooking(Long slotId, String orderNumber) {
        return deliveryUseCase.cancelBookingTimeSlot(slotId, orderNumber).map(timeSlotDTOMapper::map);
    }
}
