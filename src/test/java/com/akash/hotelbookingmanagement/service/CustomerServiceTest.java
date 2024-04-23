package com.akash.hotelbookingmanagement.service;

import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCustomer() {
        // Arrange
        Customer customer = new Customer("John Doe", "123 Main St", 30, "1234567890");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act
        Customer addedCustomer = customerService.addCustomer(customer);

        // Assert
        assertNotNull(addedCustomer);
        assertEquals(customer, addedCustomer);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testGetAllCustomers() {
        // Arrange
        Customer customer1 = new Customer("akash", "delhi", 30, "1234567890");
        Customer customer2 = new Customer("aman", "haryana", 30, "1234567899");

        List<Customer> customers=new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);

        when(customerRepository.findAll()).thenReturn(customers);

        // Act
        List<Customer> result = (List<Customer>) customerService.getAllCustomers();

        // Assert
        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals(customers, result);
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetCustomerById_CustomerFound() {
        // Arrange
        Customer customer = new Customer("John Doe", "123 Main St", 30, "1234567890");
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customer));

        // Act
        Customer result = customerService.getCustomerById(1);

        // Assert
        assertNotNull(result);
        assertEquals(customer, result);
        verify(customerRepository, times(1)).findById(1);
    }

    @Test
    void testGetCustomerById_CustomerNotFound() {
        // Arrange
        Integer customerId = 1;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act
        Customer result = customerService.getCustomerById(customerId);

        // Assert
        assertNull(result);
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void testUpdateCustomer_CustomerFound() {
        // Arrange
        Integer customerId = 1;
        Customer customerOld = new Customer("John Doe", "123 Main St", 30, "1234567890");
        Customer customerNew = new Customer("Jane Doe", "456 Elm St", 25, "9876543210");
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerOld));
        when(customerRepository.save(any(Customer.class))).thenReturn(customerNew);

        // Act
        Customer result = customerService.updateCustomer(customerId, customerNew);

        // Assert
        assertNotNull(result);
        assertEquals(customerNew, result);
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(customerOld);
    }

    @Test
    void testUpdateCustomer_CustomerNotFound() {
        // Arrange
        Integer customerId = 1;
        Customer customerNew = new Customer("Jane Doe", "456 Elm St", 25, "9876543210");
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act
        Customer result = customerService.updateCustomer(customerId, customerNew);

        // Assert
        assertNull(result);
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomer_CustomerFound() {
        // Arrange
        Integer customerId = 1;
        when(customerRepository.existsById(customerId)).thenReturn(true);

        // Act
        boolean result = customerService.deleteCustomer(customerId);

        // Assert
        assertTrue(result);
        verify(customerRepository, times(1)).existsById(customerId);
        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    void testDeleteCustomer_CustomerNotFound() {
        // Arrange
        Integer customerId = 1;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        // Act
        boolean result = customerService.deleteCustomer(customerId);

        // Assert
        assertFalse(result);
        verify(customerRepository, times(1)).existsById(customerId);
        verify(customerRepository, never()).deleteById(customerId);
    }
}
