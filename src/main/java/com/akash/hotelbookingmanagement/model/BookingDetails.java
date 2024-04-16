package com.akash.hotelbookingmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Entity class representing booking details.
 */
@Data
@Entity
public class BookingDetails {

    /**
     * Enumeration for mode of booking.
     */
    public enum ModeOfBooking {
        Online,
        Offline,
    }

    /**
     * Enumeration for mode of payment.
     */
    public enum ModeOfPayment {
        Prepaid,
        Online,
        Cash,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bookingId;

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
    private ModeOfBooking modeOfBooking;

    /**
     * Mode of payment for the reservation.
     */
    @NotNull(message = "Mode of payment must be specified")
    @Enumerated(EnumType.STRING)
    private ModeOfPayment modeOfPayment;

    /**
     * List of customers associated with the booking.
     */
    @NotNull(message = "At least one customer must be specified")
    @Size(min = 1, message = "At least one customer must be specified")
    @OneToMany
    private List<Customer> customerList;

    /**
     * List of rooms booked for the reservation.
     */
    @NotNull(message = "At least one room must be specified")
    @Size(min = 1, message = "At least one room must be specified")
    @OneToMany
    private List<Room> roomList;

    /**
     * Total bill amount for the booking.
     */
    @Min(value = 0, message = "Bill amount cannot be negative")
    private Integer billAmount;

    /**
     * Amount paid for the booking.
     */
    @Min(value = 0, message = "Paid amount cannot be negative")
    private Integer paidAmount;

    /**
     * Checks if the advance payment has been made for booking more than 3 rooms.
     *
     * @return True if advance payment is done, false otherwise.
     */
    public boolean isAdvancePaymentDone() {
        return roomList.size() > 3 && this.getBillAmount() / 2 <= this.getPaidAmount();
    }

    /**
     * Checks if children are accompanied by at least one adult.
     *
     * @return True if children are not accompanied by adult, false otherwise.
     */
    public boolean isNotAccompaniedByAdult() {
        int children = (int) this.getCustomerList().stream().filter(customer -> customer.getAge() < 18).count();
        return children == this.getCustomerList().size();
    }
}
