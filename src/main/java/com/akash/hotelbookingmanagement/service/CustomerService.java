package com.akash.hotelbookingmanagement.service;

import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing customers.
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Adds a new customer.
     *
     * @param customer The customer to add.
     * @return The added customer.
     */
    public Customer addCustomer(final @Valid Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * Retrieves all customers.
     *
     * @return Iterable of customers.
     */
    public Iterable<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id The ID of the customer.
     * @return The customer if found, otherwise null.
     */
    public Customer getCustomerById(final Integer id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        return customerOptional.orElse(null);
    }

    /**
     * Updates a customer.
     *
     * @param id          The ID of the customer to update.
     * @param customerNew The updated customer information.
     * @return The updated customer if found, otherwise null.
     */
    public Customer updateCustomer(final Integer id, final Customer customerNew) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customerOld = customerOptional.get();

            if (customerNew.getAddress() != null) {
                customerOld.setAddress(customerNew.getAddress());
            }
            if (customerNew.getFullName() != null) {
                customerOld.setFullName(customerNew.getFullName());
            }
            if (customerNew.getAge() != null) {
                customerOld.setAge(customerNew.getAge());
            }
            if (customerNew.getContactNumber() != null) {
                customerOld.setContactNumber(customerNew.getContactNumber());
            }

            return customerRepository.save(customerOld);
        } else {
            return null;
        }
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param id The ID of the customer to delete.
     * @return True if the customer was deleted successfully, otherwise false.
     */
    public boolean deleteCustomer(final Integer id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
