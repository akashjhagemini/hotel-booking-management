package com.akash.hotelbookingmanagement.repo;

import com.akash.hotelbookingmanagement.HotelBookingManagementApplication;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = HotelBookingManagementApplication.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void testSaveCustomer() {
        // Arrange
        Customer customer = new Customer("John Doe", "123 Main St", 30, "1234567890");

        // Act
        Customer savedCustomer = customerRepository.save(customer);

        // Assert
        assertTrue(savedCustomer.getCustomerId() > 0);
        assertEquals(customer.getFullName(), savedCustomer.getFullName());
        assertEquals(customer.getAddress(), savedCustomer.getAddress());
        assertEquals(customer.getAge(), savedCustomer.getAge());
        assertEquals(customer.getContactNumber(), savedCustomer.getContactNumber());
    }


}
