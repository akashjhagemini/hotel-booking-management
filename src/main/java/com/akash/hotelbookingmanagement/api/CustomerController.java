package com.akash.hotelbookingmanagement.api;

import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @PostMapping(value = "/")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        LOGGER.info("Request received to create a new customer");
        Customer savedCustomer = customerService.addCustomer(customer);
        LOGGER.info("New customer created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    @GetMapping(value = "/")
    public ResponseEntity<Iterable<Customer>> getAllCustomers() {
        LOGGER.info("Request received to fetch all customers");
        Iterable<Customer> customers = customerService.getAllCustomers();
        LOGGER.info("Fetched all customers successfully");
        return ResponseEntity.ok(customers);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Integer id) {
        LOGGER.info("Request received to fetch customer with ID: {}", id);
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            LOGGER.info("Found customer with ID: {}", id);
            return ResponseEntity.ok(customer);
        } else {
            LOGGER.warn("Customer with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id, @RequestBody Customer customerNew) {
        LOGGER.info("Request received to update customer with ID: {}", id);
        Customer updatedCustomer = customerService.updateCustomer(id, customerNew);
        if (updatedCustomer != null) {
            LOGGER.info("Updated customer with ID: {}", id);
            return ResponseEntity.ok(updatedCustomer);
        } else {
            LOGGER.warn("Customer with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        LOGGER.info("Request received to delete customer with ID: {}", id);
        boolean deleted = customerService.deleteCustomer(id);
        if (deleted) {
            LOGGER.info("Deleted customer with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            LOGGER.warn("Customer with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}
