package com.akash.hotelbookingmanagement.api;

import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing customer-related API endpoints.
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Creates a new customer.
     *
     * @param customer The customer to create.
     * @return ResponseEntity representing the HTTP response.
     */
    @PostMapping(value = "/")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.addCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    /**
     * Retrieves all customers.
     *
     * @return ResponseEntity representing the HTTP response.
     */
    @GetMapping(value = "/")
    public ResponseEntity<Iterable<Customer>> getAllCustomers() {
        Iterable<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id The ID of the customer.
     * @return ResponseEntity representing the HTTP response.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Integer id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates a customer.
     *
     * @param id          The ID of the customer to update.
     * @param customerNew The updated customer information.
     * @return ResponseEntity representing the HTTP response.
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id, @RequestBody Customer customerNew) {
        Customer updatedCustomer = customerService.updateCustomer(id, customerNew);
        if (updatedCustomer != null) {
            return ResponseEntity.ok(updatedCustomer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param id The ID of the customer to delete.
     * @return ResponseEntity representing the HTTP response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        boolean deleted = customerService.deleteCustomer(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
