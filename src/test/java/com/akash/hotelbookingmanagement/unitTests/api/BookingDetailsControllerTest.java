package com.akash.hotelbookingmanagement.unitTests.api;

import com.akash.hotelbookingmanagement.api.BookingDetailsController;
import com.akash.hotelbookingmanagement.model.BookingDetails;
import com.akash.hotelbookingmanagement.dto.BookingDetailsDto;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.model.enums.ModeOfBooking;
import com.akash.hotelbookingmanagement.model.enums.ModeOfPayment;
import com.akash.hotelbookingmanagement.service.BookingDetailsService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class BookingDetailsControllerTest {

    @Mock
    private BookingDetailsService bookingDetailsService;

    @InjectMocks
    private BookingDetailsController bookingDetailsController;

    private BookingDetails testBookingDetails;
    private BookingDetailsDto testBookingDetailsDto;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup testBookingDetails
        testBookingDetails = new BookingDetails();
        testBookingDetails.setBookingId(1);
        testBookingDetails.setDuration(5);
        testBookingDetails.setStartDate(LocalDate.of(2024, 4, 1));
        testBookingDetails.setEndDate(LocalDate.of(2024, 4, 6));
        testBookingDetails.setModeOfBooking(ModeOfBooking.online);
        testBookingDetails.setModeOfPayment(ModeOfPayment.prepaid);
        testBookingDetails.setCustomerList(Arrays.asList(new Customer()));
        testBookingDetails.setRoomList(Arrays.asList(new Room()));
        testBookingDetails.setBillAmount(500);
        testBookingDetails.setPaidAmount(250);

        // Set up a test booking details dto
        testBookingDetailsDto = new BookingDetailsDto();
        testBookingDetailsDto.setDuration(5);
        testBookingDetailsDto.setStartDate(LocalDate.now());
        testBookingDetailsDto.setEndDate(LocalDate.now().plusDays(5));
        testBookingDetailsDto.setModeOfBooking(ModeOfBooking.online);
        testBookingDetailsDto.setModeOfPayment(ModeOfPayment.prepaid);
        testBookingDetailsDto.setCustomerIdList(List.of(1));
        testBookingDetailsDto.setRoomNumberList(List.of(101));
        testBookingDetailsDto.setPaidAmount(250);
    }

    @Test
    @DisplayName("Should create a booking")
    void testCreateBookingDetails() {
        when(bookingDetailsService.createBooking(any(BookingDetailsDto.class))).thenReturn(testBookingDetails);
        ResponseEntity<BookingDetails> responseEntity = bookingDetailsController.createBookingDetails(testBookingDetailsDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should get all bookings")
    void testGetAllBookingDetails() {
        when(bookingDetailsService.getAllBookingDetails()).thenReturn(Arrays.asList(testBookingDetails));
        ResponseEntity<Iterable<BookingDetails>> responseEntity = bookingDetailsController.getAllBookingDetails();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should get booking by ID")
    void testGetBookingDetailsById() {
        when(bookingDetailsService.getBookingDetails(testBookingDetails.getBookingId())).thenReturn(testBookingDetails);
        ResponseEntity<BookingDetails> responseEntity = bookingDetailsController.getBookingDetailsById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should update booking")
    void testUpdateBookingDetails() {
        when(bookingDetailsService.updateBookingDetails(anyInt(), any(BookingDetailsDto.class))).thenReturn(testBookingDetails);
        ResponseEntity<BookingDetails> responseEntity = bookingDetailsController.updateBookingDetails(1, testBookingDetailsDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Should delete booking")
    void testDeleteBookingDetails() {
        when(bookingDetailsService.deleteBookingDetails(anyInt())).thenReturn(true);
        ResponseEntity<Void> responseEntity = bookingDetailsController.deleteBookingDetails(1);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}
