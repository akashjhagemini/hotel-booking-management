package com.akash.hotelbookingmanagement.unitTests.api;

import com.akash.hotelbookingmanagement.api.CustomerController;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create a customer")
    void testCreateCustomer() {
        Customer customer = new Customer("Akash", "Delhi", 30, "1234567890");

        when(customerService.addCustomer(any(Customer.class))).thenReturn(customer);

        ResponseEntity<Customer> responseEntity = customerController.createCustomer(customer);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(customer, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should get all customers")
    void testGetAllCustomers() {
        Customer customer1 = new Customer("Akash", "Gemini", 30, "1234567890");
        Customer customer2 = new Customer("Aman", "Delhi", 25, "9876543210");

        List<Customer> customerList = Arrays.asList(customer1, customer2);

        when(customerService.getAllCustomers()).thenReturn(customerList);

        ResponseEntity<Iterable<Customer>> responseEntity = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(customerList, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should get customer by ID")
    void testGetCustomer() {
        Customer customer = new Customer("Akash", "Gemini", 30, "1234567890");

        when(customerService.getCustomerById(anyInt())).thenReturn(customer);

        ResponseEntity<Customer> responseEntity = customerController.getCustomer(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(customer, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should update customer")
    void testUpdateCustomer() {
        Customer customer = new Customer("Akash", "Delhi", 30, "1234567890");

        when(customerService.updateCustomer(anyInt(), any(Customer.class))).thenReturn(customer);

        ResponseEntity<Customer> responseEntity = customerController.updateCustomer(1, customer);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(customer, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should delete customer")
    void testDeleteCustomer() {
        when(customerService.deleteCustomer(anyInt())).thenReturn(true);

        ResponseEntity<Void> responseEntity = customerController.deleteCustomer(1);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(customerService, times(1)).deleteCustomer(anyInt());
    }
}
