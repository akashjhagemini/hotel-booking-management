package com.akash.hotelbookingmanagement.api;

import com.akash.hotelbookingmanagement.model.BookingDetails;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.model.enums.ModeOfBooking;
import com.akash.hotelbookingmanagement.model.enums.ModeOfPayment;
import com.akash.hotelbookingmanagement.service.BookingDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BookingDetailsControllerTest {

    @Mock
    private BookingDetailsService bookingDetailsService;

    @InjectMocks
    private BookingDetailsController bookingDetailsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup testBookingDetails
        BookingDetails testBookingDetails = new BookingDetails();
        testBookingDetails.setBookingId(1);
        testBookingDetails.setDuration(5);
        testBookingDetails.setStartDate(LocalDate.of(2024, 4, 1));
        testBookingDetails.setEndDate(LocalDate.of(2024, 4, 6));
        testBookingDetails.setModeOfBooking(ModeOfBooking.Online);
        testBookingDetails.setModeOfPayment(ModeOfPayment.Prepaid);
        testBookingDetails.setCustomerList(Arrays.asList(new Customer()));
        testBookingDetails.setRoomList(Arrays.asList(new Room()));
        testBookingDetails.setBillAmount(500);
        testBookingDetails.setPaidAmount(250);

        when(bookingDetailsService.createBooking(any())).thenReturn(testBookingDetails);
        when(bookingDetailsService.getAllBookingDetails()).thenReturn(Arrays.asList(testBookingDetails));
        when(bookingDetailsService.getBookingDetails(anyInt())).thenReturn(testBookingDetails);
        when(bookingDetailsService.updateBookingDetails(anyInt(), any())).thenReturn(testBookingDetails);
        when(bookingDetailsService.deleteBookingDetails(anyInt())).thenReturn(true);
    }

    @Test
    @DisplayName("Should create a booking")
    void testCreateBookingDetails() {
        BookingDetails bookingDetails = new BookingDetails();

        ResponseEntity<BookingDetails> responseEntity = bookingDetailsController.createBookingDetails(bookingDetails);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should get all bookings")
    void testGetAllBookingDetails() {
        ResponseEntity<Iterable<BookingDetails>> responseEntity = bookingDetailsController.getAllBookingDetails();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should get booking by ID")
    void testGetBookingDetailsById() {
        ResponseEntity<BookingDetails> responseEntity = bookingDetailsController.getBookingDetailsById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should update booking")
    void testUpdateBookingDetails() {
        BookingDetails bookingDetails = new BookingDetails();

        ResponseEntity<BookingDetails> responseEntity = bookingDetailsController.updateBookingDetails(1, bookingDetails);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should delete booking")
    void testDeleteBookingDetails() {
        ResponseEntity<Void> responseEntity = bookingDetailsController.deleteBookingDetails(1);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}
