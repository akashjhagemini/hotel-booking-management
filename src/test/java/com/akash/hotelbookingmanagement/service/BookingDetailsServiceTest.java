package com.akash.hotelbookingmanagement.service;

import com.akash.hotelbookingmanagement.model.BookingDetails;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.repository.BookingDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingDetailsServiceTest {

    @Mock
    private BookingDetailsRepository bookingDetailsRepository;

    @InjectMocks
    private BookingDetailsService bookingDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBooking() {
        // Arrange
        BookingDetails bookingDetails = new BookingDetails();
        bookingDetails.setStartDate(LocalDate.of(2024, 4, 15));
        bookingDetails.setEndDate(LocalDate.of(2024, 4, 20));
        List<Room> roomList = new ArrayList<>();
        roomList.add(Room.builder().type("Single").pricePerDay(100).build());
        roomList.add(Room.builder().type("Double").pricePerDay(150).build());
        bookingDetails.setRoomList(roomList);
        when(bookingDetailsRepository.save(any(BookingDetails.class))).thenReturn(bookingDetails);

        // Act
        BookingDetails createdBooking = bookingDetailsService.createBooking(bookingDetails);

        // Assert
        assertNotNull(createdBooking);
        assertEquals(bookingDetails.getRoomList().stream().mapToInt(Room::getPricePerDay).sum(), createdBooking.getBillAmount());
        verify(bookingDetailsRepository, times(1)).save(bookingDetails);
    }

    @Test
    void getBookingDetails() {
        // Arrange
        Integer bookingId = 1;
        BookingDetails bookingDetails = new BookingDetails();
        when(bookingDetailsRepository.findById(bookingId)).thenReturn(Optional.of(bookingDetails));

        // Act
        BookingDetails result = bookingDetailsService.getBookingDetails(bookingId);

        // Assert
        assertNotNull(result);
        assertEquals(bookingDetails, result);
        verify(bookingDetailsRepository, times(1)).findById(bookingId);
    }

    @Test
    void getAllBookingDetails() {
        // Arrange
        List<BookingDetails> bookingDetailsList = new ArrayList<>();
        bookingDetailsList.add(new BookingDetails());
        bookingDetailsList.add(new BookingDetails());
        when(bookingDetailsRepository.findAll()).thenReturn(bookingDetailsList);

        // Act
        Iterable<BookingDetails> result = bookingDetailsService.getAllBookingDetails();

        // Assert
        assertNotNull(result);
        assertEquals(bookingDetailsList, result);
        verify(bookingDetailsRepository, times(1)).findAll();
    }

    @Test
    void updateBookingDetails() {
        // Arrange
        Integer bookingId = 1;
        BookingDetails existingBooking = new BookingDetails();
        existingBooking.setBillAmount(500);
        BookingDetails updatedBooking = new BookingDetails();
        updatedBooking.setBillAmount(750);
        when(bookingDetailsRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));
        when(bookingDetailsRepository.save(any(BookingDetails.class))).thenReturn(updatedBooking);

        // Act
        BookingDetails result = bookingDetailsService.updateBookingDetails(bookingId, updatedBooking);

        // Assert
        assertNotNull(result);
        assertEquals(updatedBooking.getBillAmount(), result.getBillAmount());
        verify(bookingDetailsRepository, times(1)).findById(bookingId);
        verify(bookingDetailsRepository, times(1)).save(existingBooking);
    }

    @Test
    void deleteBookingDetails() {
        // Arrange
        Integer bookingId = 1;
        when(bookingDetailsRepository.existsById(bookingId)).thenReturn(true);

        // Act
        boolean result = bookingDetailsService.deleteBookingDetails(bookingId);

        // Assert
        assertTrue(result);
        verify(bookingDetailsRepository, times(1)).existsById(bookingId);
        verify(bookingDetailsRepository, times(1)).deleteById(bookingId);
    }
}
