package org.carrefour.delivery.application.controller;

import org.carrefour.delivery.common.model.DeliveryMode;
import org.carrefour.delivery.domain.model.TimeSlot;
import org.carrefour.delivery.domain.provider.OrderPort;
import org.carrefour.delivery.domain.provider.TimeSlotPort;
import org.carrefour.delivery.domain.usecase.DeliveryUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class DeliveryControllerITTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TimeSlotPort timeSlotPort;

    @Autowired
    private OrderPort orderPort;

    private TimeSlot timeSlot1;
    private TimeSlot timeSlot2;

    @MockitoBean
    private DeliveryUseCase deliveryUseCase;


    @Test
    void testGetDeliveryMethods() {

        webTestClient.get()
                .uri("/api/delivery/methods")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DeliveryMode.class)
                .hasSize(4)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .contains(DeliveryMode.DRIVE, DeliveryMode.DELIVERY, DeliveryMode.DELIVERY_TODAY, DeliveryMode.DELIVERY_ASAP);
                });
    }


    @Test
    void testGetAvailableTimeSlots() {
        timeSlot1 = new TimeSlot();
        timeSlot1.setStartTime(LocalDateTime.now().plusHours(1));
        timeSlot1.setEndTime(LocalDateTime.now().plusHours(2));
        timeSlot1.setDeliveryMethod(DeliveryMode.DRIVE);

        timeSlot2 = new TimeSlot();
        timeSlot2.setStartTime(LocalDateTime.now().plusHours(3));
        timeSlot2.setEndTime(LocalDateTime.now().plusHours(4));
        timeSlot2.setDeliveryMethod(DeliveryMode.DRIVE);
        // Mock de la méthode du service
        when(deliveryUseCase.getAvailableTimeSlots(DeliveryMode.DRIVE))
                .thenReturn(Flux.just(timeSlot1, timeSlot2));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/delivery/time-slots")
                        .queryParam("method", DeliveryMode.DRIVE)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TimeSlot.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).isNotEmpty();
                    assertThat(response.getResponseBody().size()).isEqualTo(2); // Vérifiez le nombre de créneaux
                });
    }


    @Test
    void testBookTimeSlotSuccess() {
        Long slotId = 1L;
        String customerId = "12345";
        when(deliveryUseCase.bookTimeSlot(anyLong(),anyString())).thenReturn(Mono.empty());
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/delivery/time-slots/{slotId}")
                        .queryParam("customerId", customerId)
                        .build(slotId))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    void testCancelBookingSuccess() {
        Long slotId = 1L;
        String orderNumber = "ORDER_123";
        when(deliveryUseCase.cancelBookingTimeSlot(anyLong(),anyString())).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/api/delivery/time-slots/{slotId}")
                        .queryParam("customerId", orderNumber)
                        .build(slotId))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }


}
