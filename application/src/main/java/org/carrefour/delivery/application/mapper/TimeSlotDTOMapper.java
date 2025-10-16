package org.carrefour.delivery.application.mapper;

import org.carrefour.delivery.application.dto.TimeSlotDTO;
import org.carrefour.delivery.common.mapper.TwoWayMapper;
import org.carrefour.delivery.domain.model.TimeSlot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TimeSlotDTOMapper extends TwoWayMapper<TimeSlot, TimeSlotDTO> {
}
