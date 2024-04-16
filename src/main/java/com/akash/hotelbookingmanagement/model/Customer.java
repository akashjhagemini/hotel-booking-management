package com.akash.hotelbookingmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the hotel booking system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Customer {
    /**
     * The unique identifier for the customer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer customerId;

    /**
     * The full name of the customer.
     */
    @NotBlank(message = "Full name must be specified")
    private String fullName;

    /**
     * The address of the customer.
     */
    @NotBlank(message = "Address must be specified")
    private String address;

    /**
     * The age of the customer.
     */
    @NotNull(message = "Age must be specified")
    @Min(value = 0, message = "Age must be a positive number")
    private Integer age;

    /**
     * The contact number of the customer.
     */
    @Column(unique = true)
    @NotBlank(message = "Contact number must be specified")
    @Size(min = 10, max = 10, message = "Contact number must be 10 digits")
    private String contactNumber;

//
//    @ManyToMany(mappedBy = "checkedInCustomers")
//    private List<Room> roomList=new ArrayList<>();

    public Customer(String fullName, String address, Integer age, String contactNumber) {
        this.fullName = fullName;
        this.address = address;
        this.age = age;
        this.contactNumber = contactNumber;
    }
}
