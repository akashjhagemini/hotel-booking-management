package com.akash.hotelbookingmanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomDto {
    /**
     * The type of the room.
     */
    @NotBlank(message = "Type must be specified")
    private String type;

    /**
     * The maximum occupancy of the room.
     */
    @Min(value = 1, message = "Occupancy must be greater than zero")
    private Integer occupancy;

    /**
     * The availability status of the room.
     */
    private Boolean availability;

    /**
     * Flag indicating whether the room is currently checked in.
     */
    private Boolean isCheckedIn;

    /**
     * Flag indicating whether the room is checked out.
     */
    private Boolean isCheckedOut;

    /**
     * The price per day for the room.
     */
    @Positive(message = "Price per day must be positive")
    private Integer pricePerDay;

    /**
     * The list of customers currently checked in to the room.
     */
    private List<Integer> checkedInCustomerIdList;
}
