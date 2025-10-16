package org.carrefour.delivery.domain.model;


/**
 * Represents an order in the system.
 * This class contains the details of an order, including its identifier,
 * order number, and the associated time slot ID.
 */

public class Order {

    private Long id;

    private String customerId;

    private Long timeSlotId;

    /**
     * Default constructor.
     */
    public Order() {
    }

    /**
     * Constructor with parameters.
     *
     * @param id the identifier of the order
     * @param orderNumber the number of the order
     * @param timeSlotId the ID of the associated time slot
     */
    public Order(Long id, String orderNumber, Long timeSlotId) {
        this.id = id;
        this.customerId = orderNumber;
        this.timeSlotId = timeSlotId;
    }

    /**
     * Gets the identifier of the order.
     *
     * @return the identifier of the order
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the identifier of the order.
     *
     * @param id the identifier of the order
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the number of the order.
     *
     * @return the number of the order
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the number of the order.
     *
     * @param customerId the number of the order
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the ID of the associated time slot.
     *
     * @return the ID of the time slot
     */
    public Long getTimeSlotId() {
        return timeSlotId;
    }

    /**
     * Sets the ID of the associated time slot.
     *
     * @param timeSlotId the ID of the time slot
     */
    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }
}
