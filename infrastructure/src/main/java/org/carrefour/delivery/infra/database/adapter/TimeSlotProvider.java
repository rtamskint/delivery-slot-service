package org.carrefour.delivery.infra.database.adapter;

import org.carrefour.delivery.common.model.DeliveryMode;
import org.carrefour.delivery.domain.model.TimeSlot;
import org.carrefour.delivery.domain.provider.TimeSlotPort;
import org.carrefour.delivery.infra.database.mapper.TimeSlotMapper;
import org.carrefour.delivery.infra.database.model.TimeSlotEntity;
import org.carrefour.delivery.infra.database.repository.TimeSlotRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Transactional(readOnly = true)
public class TimeSlotProvider implements TimeSlotPort {

    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotMapper timeSlotMapper;

    public TimeSlotProvider(TimeSlotRepository timeSlotRepository, TimeSlotMapper timeSlotMapper) {
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotMapper = timeSlotMapper;
    }

    @Override
    public Mono<TimeSlot> findTimeSlotById(long slotId) {
        Mono<TimeSlotEntity> timeSlotEntityMono = timeSlotRepository.findById(slotId);
        return timeSlotEntityMono.map(timeSlotMapper::map);
    }

    @Override
    public Flux<TimeSlot> findTimeSlotByDeliveryMethod(DeliveryMode deliveryMode) {
        Flux<TimeSlotEntity> timeSlotEntityMono = timeSlotRepository.findByDeliveryMethod(deliveryMode);
        return timeSlotEntityMono.map(timeSlotMapper::map);
    }
}
