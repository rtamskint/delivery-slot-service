package org.carrefour.delivery.application.configuration.adapter;

import org.carrefour.delivery.domain.provider.DeliveryConfigurationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeliveryConfigurationAdapter implements DeliveryConfigurationProvider {

    @Value("${timeslot.drive.start-hour}")
    private int driveStartHour;

    @Value("${timeslot.drive.end-hour}")
    private int driveEndHour;

    @Value("${timeslot.drive.preparation-minutes}")
    private int drivePreparationMinutes;

    @Value("${timeslot.drive.max-days}")
    private int driveMaxDays;

    @Value("${timeslot.delivery-today.start-hour}")
    private int deliveryTodayStartHour;

    @Value("${timeslot.delivery-today.end-hour}")
    private int deliveryTodayEndHour;

    @Value("${timeslot.delivery.start-hour}")
    private int deliveryStartHour;

    @Value("${timeslot.delivery.end-hour}")
    private int deliveryEndHour;

    @Value("${timeslot.delivery.min-days}")
    private int deliveryMinDays;

    @Value("${timeslot.delivery.max-days}")
    private int deliveryMaxDays;

    @Value("${timeslot.delivery-asap.hours}")
    private int deliveryAsapHours;

    @Override public int getDriveStartHour()        { return driveStartHour; }
    @Override public int getDriveEndHour()          { return driveEndHour; }
    @Override public int getDrivePreparationMinutes() { return drivePreparationMinutes; }
    @Override public int getDriveMaxDays()          { return driveMaxDays; }
    @Override public int getDeliveryTodayStartHour(){ return deliveryTodayStartHour; }
    @Override public int getDeliveryTodayEndHour()  { return deliveryTodayEndHour; }
    @Override public int getDeliveryStartHour()     { return deliveryStartHour; }
    @Override public int getDeliveryEndHour()       { return deliveryEndHour; }
    @Override public int getDeliveryMinDays()       { return deliveryMinDays; }
    @Override public int getDeliveryMaxDays()       { return deliveryMaxDays; }
    @Override public int getDeliveryAsapHours()     { return deliveryAsapHours; }
}
