package com.akash.hotelbookingmanagement.dto;

import com.akash.hotelbookingmanagement.model.enums.ModeOfBooking;
import com.akash.hotelbookingmanagement.model.enums.ModeOfPayment;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Entity class representing booking details.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailsDto {

    /**
     * Duration of the booking in days.
     */
    @NotNull(message = "Duration must be specified")
    @Min(value = 1, message = "Duration must be greater than 0")
    private Integer duration;

    /**
     * Start date of the booking.
     */
    @NotNull(message = "Start date must be specified")
    private LocalDate startDate;

    /**
     * End date of the booking.
     */
    @NotNull(message = "End date must be specified")
    private LocalDate endDate;

    /**
     * Mode of booking for the reservation.
     */
    @NotNull(message = "Mode of booking must be specified")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum")
    private ModeOfBooking modeOfBooking;

    /**
     * Mode of payment for the reservation.
     */
    @NotNull(message = "Mode of payment must be specified")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum")
    private ModeOfPayment modeOfPayment;

    /**
     * List of customer's ids associated with the booking.
     */
    private List<Integer> customerIdList;

    /**
     * List of roomNumbers booked for the reservation.
     */
    private List<Integer> roomNumberList;

    /**
     * Amount paid for the booking.
     */
    @Min(value = 0, message = "Paid amount cannot be negative")
    private Integer paidAmount;

}
