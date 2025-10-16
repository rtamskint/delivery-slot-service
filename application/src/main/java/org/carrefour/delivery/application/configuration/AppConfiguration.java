package org.carrefour.delivery.application.configuration;

import org.carrefour.delivery.domain.provider.DeliveryConfigurationProvider;
import org.carrefour.delivery.domain.provider.OrderPort;
import org.carrefour.delivery.domain.provider.TimeSlotPort;
import org.carrefour.delivery.domain.service.DeliveryUseCaseImpl;
import org.carrefour.delivery.domain.usecase.DeliveryUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    DeliveryUseCase deliveryUseCase(TimeSlotPort timeSlotPort, OrderPort orderPort, DeliveryConfigurationProvider config) {
        return new DeliveryUseCaseImpl(timeSlotPort, orderPort, config);
    }
}
