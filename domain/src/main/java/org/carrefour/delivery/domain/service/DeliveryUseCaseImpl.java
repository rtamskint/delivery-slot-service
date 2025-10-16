package org.carrefour.delivery.domain.service;

import org.carrefour.delivery.common.model.DeliveryMode;
import org.carrefour.delivery.domain.model.Order;
import org.carrefour.delivery.domain.model.TimeSlot;
import org.carrefour.delivery.domain.provider.DeliveryConfigurationProvider;
import org.carrefour.delivery.domain.provider.OrderPort;
import org.carrefour.delivery.domain.provider.TimeSlotPort;
import org.carrefour.delivery.domain.usecase.DeliveryUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;

public class DeliveryUseCaseImpl implements DeliveryUseCase {
    
    
    private  final TimeSlotPort timeSlotPort;
    private final OrderPort orderPort;
    private final DeliveryConfigurationProvider config;

    public DeliveryUseCaseImpl(TimeSlotPort timeSlotPort, OrderPort orderPort, DeliveryConfigurationProvider config) {
        this.timeSlotPort = timeSlotPort;
        this.orderPort = orderPort;
        this.config = config;
    }

    /**
     * Retrieves all available time slots for a given delivery method.
     *
     * @param deliveryMode the delivery method.
     * @return a Flux of available time slots.
     */
    @Override
    public Flux<TimeSlot> getAvailableTimeSlots(DeliveryMode deliveryMode) {
        return switch (deliveryMode) {
            case DRIVE -> getAvailableDriveSlots();
            case DELIVERY_TODAY -> getAvailableDeliveryTodaySlots();
            case DELIVERY_ASAP -> getAvailableASAPSlots();
            case DELIVERY -> getAvailableDeliverySlots();

        };
    }

    /**
     * Books a time slot by its ID.
     *
     * @param slotId the ID of the time slot to book.
     * @return a Mono of the booked time slot, or an error if not available.
     */
    @Override
    public Mono<TimeSlot> bookTimeSlot(Long slotId, String orderNumber) {
        // Récupérer le créneau horaire par ID
        return orderPort.findOrderByTimeSlotId(slotId)
                .any(order -> order.getCustomerId().equals(orderNumber))
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalStateException("This order has already booked this time slot"));
                    } else {
                        Order newOrder = new Order();
                        newOrder.setCustomerId(orderNumber);
                        newOrder.setTimeSlotId(slotId);

                        return orderPort.saveOrder(newOrder)
                                .then(timeSlotPort.findTimeSlotById(slotId));
                    }
                });
    }

    /**
     * Cancels the booking of a time slot by its ID.
     *
     * @param slotId the ID of the time slot to cancel.
     * @return a Mono of the updated time slot, or an error if not booked.
     */
    @Override
    public Mono<TimeSlot> cancelBookingTimeSlot(Long slotId, String orderNumber ) {
        return orderPort.findOrderByTimeSlotId(slotId)
                .filter(order -> order.getCustomerId().equals(orderNumber))
                .singleOrEmpty()
                .flatMap(order -> orderPort.deleteOrderById(order.getId()).then(timeSlotPort.findTimeSlotById(slotId)))
                .switchIfEmpty(Mono.error(new NoSuchElementException("Booking not found")));
    }


    private Flux<TimeSlot> getAvailableDriveSlots() {
        LocalDateTime currentTime = LocalDateTime.now().plusMinutes(config.getDrivePreparationMinutes());
        LocalDateTime maxDate = currentTime.plusDays(config.getDriveMaxDays());

        return timeSlotPort.findTimeSlotByDeliveryMethod(DeliveryMode.DRIVE)
                .filter(slot -> slot.getStartTime().isAfter(currentTime) && slot.getEndTime().isBefore(maxDate)
                        && isWithinDriveTimeWindow(slot));
    }

    private boolean isWithinDriveTimeWindow(TimeSlot slot) {
        LocalTime startTime = LocalTime.of(config.getDriveStartHour(), 0);
        LocalTime endTime = LocalTime.of(config.getDriveEndHour(), 0);
        LocalTime slotTime = slot.getStartTime().toLocalTime();

        return !slotTime.isBefore(startTime) && !slotTime.isAfter(endTime);
    }

    private Flux<TimeSlot> getAvailableDeliveryTodaySlots() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime endTime = currentTime.withHour(config.getDeliveryTodayEndHour()).withMinute(0);

        return timeSlotPort.findTimeSlotByDeliveryMethod(DeliveryMode.DELIVERY_TODAY)
                .filter(slot -> slot.getStartTime().isAfter(currentTime) && slot.getEndTime().isBefore(endTime)
                        && isWithinDeliveryTodayTimeWindow(slot));
    }

    private boolean isWithinDeliveryTodayTimeWindow(TimeSlot slot) {
        LocalTime startTime = LocalTime.of(config.getDeliveryTodayStartHour(), 0);
        LocalTime endTime = LocalTime.of(config.getDeliveryTodayEndHour(), 0);
        LocalTime slotTime = slot.getStartTime().toLocalTime();

        return !slotTime.isBefore(startTime) && !slotTime.isAfter(endTime);
    }

    private Flux<TimeSlot> getAvailableASAPSlots() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime asapTime = currentTime.plusHours(config.getDeliveryAsapHours());

        return timeSlotPort.findTimeSlotByDeliveryMethod(DeliveryMode.DELIVERY_ASAP)
                .filter(slot -> slot.getStartTime().isAfter(currentTime) && slot.getStartTime().isBefore(asapTime));
    }

    private Flux<TimeSlot> getAvailableDeliverySlots() {
        LocalDateTime minDate = LocalDateTime.now().plusDays(config.getDeliveryMinDays()).withHour(config.getDeliveryStartHour()).withMinute(0);
        LocalDateTime maxDate = minDate.plusDays(config.getDeliveryMaxDays());

        return timeSlotPort.findTimeSlotByDeliveryMethod(DeliveryMode.DELIVERY)
                .filter(slot -> slot.getStartTime().isAfter(minDate) && slot.getEndTime().isBefore(maxDate)
                        && isWithinDeliveryTimeWindow(slot));
    }

    private boolean isWithinDeliveryTimeWindow(TimeSlot slot) {
        LocalTime startTime = LocalTime.of(config.getDeliveryStartHour(), 0);
        LocalTime endTime = LocalTime.of(config.getDeliveryEndHour(), 0);
        LocalTime slotTime = slot.getStartTime().toLocalTime();

        return !slotTime.isBefore(startTime) && !slotTime.isAfter(endTime);
    }
}
