package org.carrefour.delivery.infra.database.mapper;

import org.carrefour.delivery.common.mapper.TwoWayMapper;
import org.carrefour.delivery.domain.model.TimeSlot;
import org.carrefour.delivery.infra.database.model.TimeSlotEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TimeSlotMapper extends TwoWayMapper<TimeSlotEntity, TimeSlot> {
}
