package org.carrefour.delivery.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.carrefour.delivery.application.adapter.DeliveryUseCaseAdapter;
import org.carrefour.delivery.application.dto.TimeSlotDTO;
import org.carrefour.delivery.common.model.DeliveryMode;
import org.carrefour.delivery.domain.model.TimeSlot;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing delivery methods and time slots.
 */
@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final DeliveryUseCaseAdapter deliveryUseCaseAdapter;

    public DeliveryController( DeliveryUseCaseAdapter deliveryUseCaseAdapter) {
        this.deliveryUseCaseAdapter = deliveryUseCaseAdapter;
    }

    /**
     * Returns all available delivery methods.
     *
     * @return a Flux of delivery methods.
     */
    @Operation(summary = "Get available delivery methods",
            description = "This method returns the available delivery methods")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved delivery methods"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/methods")
    public Flux<DeliveryMode> getDeliveryMethods() {
        return Flux.fromArray(DeliveryMode.values());
    }

    /**
     * Retrieves available time slots for a specific delivery method.
     *
     * @param method the delivery method.
     * @return a Flux of available time slots.
     */
    @Operation(summary = "Get available time slots by delivery method",
            description = "This method returns the list of the available time slots for specific delivery method")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved time slots",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TimeSlotDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid delivery method"),
            @ApiResponse(responseCode = "404", description = "No time slots available")
    })
    @GetMapping("/time-slots")
    public Flux<TimeSlotDTO> getAvailableTimeSlots(@RequestParam("method") DeliveryMode method) {
        return deliveryUseCaseAdapter.getAvailableTimeSlots(method);
    }

    /**
     * Books a time slot by its ID.
     *
     * @param slotId     the ID of the time slot.
     * @param customerId the custom Id of the order
     * @return a Mono of the booked time slot.
     */
    @Operation(summary = "Book a time slot",
            description = "This method books the time slot for a specific customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully booked time slot",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TimeSlot.class))}),
            @ApiResponse(responseCode = "404", description = "Time slot not found"),
            @ApiResponse(responseCode = "400", description = "Time slot is already booked")
    })
    @PostMapping("/time-slots/{slotId}")
    public Mono<TimeSlotDTO> bookTimeSlots(@PathVariable("slotId") Long slotId, @RequestParam("customerId") String customerId) {
        return deliveryUseCaseAdapter.bookTimeSlot(slotId, customerId);


    }

    /**
     * Cancels a time slot booking by its ID.
     *
     * @param slotId     the ID of the time slot.
     * @param customerId the number of order
     * @return a Mono of the updated time slot.
     */
    @Operation(summary = "Cancel a booking",
            description = "This method cancels a time slot booked by a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully canceled booking",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TimeSlot.class))}),
            @ApiResponse(responseCode = "404", description = "Time slot not found"),
            @ApiResponse(responseCode = "400", description = "Time slot is not booked")
    })
    @DeleteMapping("/time-slots/{slotId}")
    public Mono<TimeSlotDTO> cancelBooking(@PathVariable("slotId") Long slotId, @RequestParam("customerId") String customerId) {
        return deliveryUseCaseAdapter.cancelBooking(slotId, customerId);
    }


}
