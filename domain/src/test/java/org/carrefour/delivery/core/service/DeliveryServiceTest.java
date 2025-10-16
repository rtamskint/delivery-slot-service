package org.carrefour.delivery.core.service;


import org.carrefour.delivery.common.model.DeliveryMode;
import org.carrefour.delivery.domain.model.Order;
import org.carrefour.delivery.domain.model.TimeSlot;
import org.carrefour.delivery.domain.provider.DeliveryConfigurationProvider;
import org.carrefour.delivery.domain.provider.OrderPort;
import org.carrefour.delivery.domain.provider.TimeSlotPort;
import org.carrefour.delivery.domain.service.DeliveryUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private TimeSlotPort timeSlotPort;

    @Mock
    private OrderPort orderPort;

    @Mock
    private DeliveryConfigurationProvider deliveryConfigurationProvider;

    @InjectMocks
    private DeliveryUseCaseImpl deliveryUseCase;

    private TimeSlot timeSlot;
    private Order existingOrder;
    private Order newOrder;


    @BeforeEach
    void setUp() {
        // Création d'un TimeSlot et de commandes pour les tests
        timeSlot = new TimeSlot();
        timeSlot.setId(1L);
        timeSlot.setStartTime(LocalDateTime.now().plusDays(1));
        timeSlot.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setCustomerId("12345");
        existingOrder.setTimeSlotId(1L);

        newOrder = new Order();
        newOrder.setCustomerId("67890");
        newOrder.setTimeSlotId(1L);

        // Simuler quelques créneaux horaires pour chaque méthode de livraison
        TimeSlot timeSlotDrive = new TimeSlot();
        timeSlotDrive.setId(1L);
        timeSlotDrive.setDeliveryMethod(DeliveryMode.DRIVE);
        timeSlotDrive.setStartTime(LocalDateTime.now().plusMinutes(10));
        timeSlotDrive.setEndTime(LocalDateTime.now().plusHours(1));

        TimeSlot timeSlotDelivery = new TimeSlot();
        timeSlotDelivery.setId(2L);
        timeSlotDelivery.setDeliveryMethod(DeliveryMode.DELIVERY);
        timeSlotDelivery.setStartTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
        timeSlotDelivery.setEndTime(LocalDateTime.now().plusDays(1).withHour(12).withMinute(0));

    }

    @Test
    void testBookTimeSlot_Success() {
        when(timeSlotPort.findTimeSlotById(1L)).thenReturn(Mono.just(timeSlot));
        when(orderPort.findOrderByTimeSlotId(1L)).thenReturn(Flux.empty());
        when(orderPort.saveOrder(any(Order.class))).thenReturn(Mono.just(newOrder));

        // Appel de la méthode à tester
        Mono<TimeSlot> result = deliveryUseCase.bookTimeSlot(1L, "67890");

        // Vérification du résultat avec StepVerifier
        StepVerifier.create(result)
                .expectNextMatches(slot -> slot.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void testBookTimeSlot_AlreadyBookedBySameOrder() {
        when(orderPort.findOrderByTimeSlotId(1L)).thenReturn(Flux.just(existingOrder));

        Mono<TimeSlot> result = deliveryUseCase.bookTimeSlot(1L, "12345");

        StepVerifier.create(result)
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    void testBookTimeSlot_AlreadyBookedByAnotherOrder() {
        when(timeSlotPort.findTimeSlotById(1L)).thenReturn(Mono.just(timeSlot));
        when(orderPort.findOrderByTimeSlotId(1L)).thenReturn(Flux.just(existingOrder));
        when(orderPort.saveOrder(any(Order.class))).thenReturn(Mono.just(newOrder));

        Mono<TimeSlot> result = deliveryUseCase.bookTimeSlot(1L, "67890");

        StepVerifier.create(result)
                .expectNextMatches(slot -> slot.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void testCancelBooking_Success() {
        when(orderPort.findOrderByTimeSlotId(1L)).thenReturn(Flux.just(existingOrder));

        when(orderPort.deleteOrderById(existingOrder.getId())).thenReturn(Mono.empty());

        when(timeSlotPort.findTimeSlotById(1L)).thenReturn(Mono.just(timeSlot));

        Mono<TimeSlot> result = deliveryUseCase.cancelBookingTimeSlot(1L, "12345");

        StepVerifier.create(result)
                .expectNextMatches(slot -> slot.getId().equals(1L))
                .verifyComplete();

        verify(orderPort, times(1)).deleteOrderById(existingOrder.getId());
    }


    @Test
    void testGetAvailableDriveSlots() {
        LocalDateTime now = LocalDateTime.now().plusMinutes(180);
        TimeSlot slot1 = new TimeSlot();
        slot1.setDeliveryMethod(DeliveryMode.DRIVE);
        slot1.setStartTime(now.plusHours(1));
        slot1.setEndTime(now.plusHours(2));

        TimeSlot slot2 = new TimeSlot();
        slot2.setDeliveryMethod(DeliveryMode.DRIVE);
        slot2.setStartTime(now.plusDays(1));
        slot2.setEndTime(now.plusDays(1).plusHours(2));


        when(timeSlotPort.findTimeSlotByDeliveryMethod(DeliveryMode.DRIVE))
                .thenReturn(Flux.fromIterable(Arrays.asList(slot1, slot2)));

        Flux<TimeSlot> result = deliveryUseCase.getAvailableTimeSlots(DeliveryMode.DRIVE);

        StepVerifier.create(result)
                .expectNext(slot1, slot2);

    }

    @Test
    void testGetAvailableDeliveryTodaySlots() {
        LocalDateTime now = LocalDateTime.now();
        TimeSlot slot1 = new TimeSlot();
        slot1.setDeliveryMethod(DeliveryMode.DELIVERY_TODAY);
        slot1.setStartTime(now.plusHours(1));
        slot1.setEndTime(now.plusHours(2));

        TimeSlot slot2 = new TimeSlot();
        slot2.setDeliveryMethod(DeliveryMode.DELIVERY_TODAY);
        slot2.setStartTime(now.plusHours(4));
        slot2.setEndTime(now.plusHours(5));

        when(timeSlotPort.findTimeSlotByDeliveryMethod(DeliveryMode.DELIVERY_TODAY))
                .thenReturn(Flux.fromIterable(Arrays.asList(slot1, slot2)));

        Flux<TimeSlot> result = deliveryUseCase.getAvailableTimeSlots(DeliveryMode.DELIVERY_TODAY);

        StepVerifier.create(result)
                .expectNext(slot1, slot2);

    }

    @Test
    void testGetAvailableASAPSlots() {
        LocalDateTime now = LocalDateTime.now();

        TimeSlot slot1 = new TimeSlot();
        slot1.setDeliveryMethod(DeliveryMode.DELIVERY_TODAY);
        slot1.setStartTime(now.plusMinutes(30));
        slot1.setEndTime(now.plusMinutes(90));
        slot1.setDeliveryMethod(DeliveryMode.DELIVERY_ASAP);

        when(timeSlotPort.findTimeSlotByDeliveryMethod(DeliveryMode.DELIVERY_ASAP))
                .thenReturn(Flux.fromIterable(Arrays.asList(slot1)));

        Flux<TimeSlot> result = deliveryUseCase.getAvailableTimeSlots(DeliveryMode.DELIVERY_ASAP);

        StepVerifier.create(result)
                .expectNext(slot1);

    }

    @Test
    void testGetAvailableDeliverySlots() {
        // Arrange
        LocalDateTime now = LocalDateTime.now().plusDays(2).withHour(7).withMinute(0);
        TimeSlot slot1 = new TimeSlot();
        slot1.setDeliveryMethod(DeliveryMode.DELIVERY);
        slot1.setStartTime(now.plusHours(1));
        slot1.setEndTime(now.plusHours(2));


        when(timeSlotPort.findTimeSlotByDeliveryMethod(DeliveryMode.DELIVERY))
                .thenReturn(Flux.just(slot1));

        Flux<TimeSlot> result = deliveryUseCase.getAvailableTimeSlots(DeliveryMode.DELIVERY);

        StepVerifier.create(result)
                .expectNext(slot1);

    }
}
