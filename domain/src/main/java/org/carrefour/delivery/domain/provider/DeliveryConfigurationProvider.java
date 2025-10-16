package org.carrefour.delivery.domain.provider;


public interface DeliveryConfigurationProvider {
    int getDriveStartHour();
    int getDriveEndHour();
    int getDrivePreparationMinutes();
    int getDriveMaxDays();

    int getDeliveryTodayStartHour();
    int getDeliveryTodayEndHour();

    int getDeliveryStartHour();
    int getDeliveryEndHour();
    int getDeliveryMinDays();
    int getDeliveryMaxDays();

    int getDeliveryAsapHours();
}

