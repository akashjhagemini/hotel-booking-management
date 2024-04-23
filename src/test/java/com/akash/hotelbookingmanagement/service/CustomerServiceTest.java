package com.akash.hotelbookingmanagement.service;

import com.akash.hotelbookingmanagement.exception.ResourceNotFoundException;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup a test customer
        testCustomer = Customer.builder()
                .customerId(1)
                .fullName("Akash")
                .address("Gemini")
                .age(30)
                .contactNumber("9876543211")
                .build();
    }

    @Test
    void testAddCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        Customer addedCustomer = customerService.addCustomer(testCustomer);

        assertNotNull(addedCustomer);
        assertEquals(testCustomer, addedCustomer);

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(java.util.Collections.singletonList(testCustomer));

        Iterable<Customer> customers = customerService.getAllCustomers();

        assertNotNull(customers);
        assertTrue(customers.iterator().hasNext());
        assertEquals(testCustomer, customers.iterator().next());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(testCustomer));

        Customer foundCustomer = customerService.getCustomerById(1);

        assertNotNull(foundCustomer);
        assertEquals(testCustomer, foundCustomer);

        verify(customerRepository, times(1)).findById(1);
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(1));

        verify(customerRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateCustomer() {
        Customer updatedCustomer = Customer.builder()
                .customerId(1)
                .fullName("Akash Jha")
                .address("Gemini Solutions")
                .age(35)
                .contactNumber("9876543210")
                .build();

        when(customerRepository.findById(1)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer result = customerService.updateCustomer(1, updatedCustomer);

        assertNotNull(result);
        assertEquals(updatedCustomer, result);

        verify(customerRepository, times(1)).findById(1);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomer() {
        when(customerRepository.existsById(1)).thenReturn(true);

        boolean deleted = customerService.deleteCustomer(1);

        assertTrue(deleted);

        verify(customerRepository, times(1)).existsById(1);
        verify(customerRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(customerRepository.existsById(1)).thenReturn(false);

        boolean deleted = customerService.deleteCustomer(1);

        assertFalse(deleted);

        verify(customerRepository, times(1)).existsById(1);
        verify(customerRepository, never()).deleteById(1);
    }
}
