package com.akash.hotelbookingmanagement.api;

import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @PostMapping(value = "/")
    public ResponseEntity<Customer> createCustomer(final @RequestBody Customer customer) {
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
    public ResponseEntity<Customer> getCustomer(final @PathVariable Integer id) {
        LOGGER.info("Request received to fetch customer with ID: {}", id);
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> updateCustomer(final @PathVariable Integer id, final @RequestBody Customer customerNew) {
        LOGGER.info("Request received to update customer with ID: {}", id);
        Customer updatedCustomer = customerService.updateCustomer(id, customerNew);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(final @PathVariable Integer id) {
        LOGGER.info("Request received to delete customer with ID: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
