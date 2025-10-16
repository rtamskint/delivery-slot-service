package org.carrefour.delivery.domain.model;


import org.carrefour.delivery.common.model.DeliveryMode;

import java.time.LocalDateTime;

/**
 * Entity representing a time slot for delivery.
 */

public class TimeSlot {

    private Long id;

    private DeliveryMode deliveryMode;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public TimeSlot() {
    }

    /**
     * Gets the ID of the time slot.
     *
     * @return the ID of the time slot.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the time slot.
     *
     * @param id the ID of the time slot.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the delivery method associated with this time slot.
     *
     * @return the delivery method.
     */
    public DeliveryMode getDeliveryMethod() {
        return deliveryMode;
    }

    /**
     * Sets the delivery method for this time slot.
     *
     * @param deliveryMode the delivery method.
     */
    public void setDeliveryMethod(DeliveryMode deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    /**
     * Gets the start time of the time slot.
     *
     * @return the start time.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the time slot.
     *
     * @param startTime the start time.
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the time slot.
     *
     * @return the end time.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the time slot.
     *
     * @param endTime the end time.
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

}
