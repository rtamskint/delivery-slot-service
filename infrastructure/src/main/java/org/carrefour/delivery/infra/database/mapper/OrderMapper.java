package org.carrefour.delivery.infra.database.mapper;

import org.carrefour.delivery.common.mapper.TwoWayMapper;
import org.carrefour.delivery.domain.model.Order;
import org.carrefour.delivery.infra.database.model.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper extends TwoWayMapper<OrderEntity, Order> {

}
