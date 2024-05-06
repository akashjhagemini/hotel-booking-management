package com.akash.hotelbookingmanagement.model;

import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * Represents a room in the hotel.
 */
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    /**
     * The unique identifier for the room.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomNumber;

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
     * The price per day for the room.
     */
    @Positive(message = "Price per day must be positive")
    private Integer pricePerDay;

    /**
     * The availability status of the room.
     */
    private Boolean availability = true;

    /**
     * Flag indicating whether the room is currently checked in.
     */
    private Boolean isCheckedIn = false;

    /**
     * Flag indicating whether the room is checked out.
     */
    private Boolean isCheckedOut = false;

    /**
     * The list of customers currently checked in to the room.
     */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "customer_room",
            joinColumns = @JoinColumn(name = "roomNumber"),
            inverseJoinColumns = @JoinColumn(name = "customerId"))
    private List<Customer> checkedInCustomers;
}
