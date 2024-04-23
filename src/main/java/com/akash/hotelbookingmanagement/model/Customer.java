package com.akash.hotelbookingmanagement.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Represents a customer in the hotel booking system.
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    /**
     * The unique identifier for the customer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Size(min = CONTACT_NUMBER_LENGTH, max = CONTACT_NUMBER_LENGTH, message = "Contact number must be 10 digits")
    private String contactNumber;

    /**
     * Length of the contact number.
     */
    private static final int CONTACT_NUMBER_LENGTH = 10;


    /**
     * Constructor with non-iterable properties.
     *
     * @param fullName      The full name of the customer.
     * @param address       The address of the customer.
     * @param age           The age of the customer.
     * @param contactNumber The contact number of the customer.
     */
    public Customer(final String fullName, final String address, final Integer age, final String contactNumber) {
        this.fullName = fullName;
        this.address = address;
        this.age = age;
        this.contactNumber = contactNumber;
    }

}
