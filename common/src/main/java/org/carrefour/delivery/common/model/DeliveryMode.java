package org.carrefour.delivery.common.model;

/**
 * Enum representing the various delivery Mode available.
 */
public enum DeliveryMode {
    DRIVE("Drive pickup"),
    DELIVERY("Standard delivery"),
    DELIVERY_TODAY("Same day delivery"),
    DELIVERY_ASAP("Express delivery");

    private final String description;

    DeliveryMode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
