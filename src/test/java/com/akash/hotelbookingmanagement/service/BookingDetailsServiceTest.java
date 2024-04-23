package com.akash.hotelbookingmanagement.service;

import com.akash.hotelbookingmanagement.exception.*;
import com.akash.hotelbookingmanagement.model.BookingDetails;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.repository.BookingDetailsRepository;
import com.akash.hotelbookingmanagement.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.akash.hotelbookingmanagement.model.enums.ModeOfBooking;
import com.akash.hotelbookingmanagement.model.enums.ModeOfPayment;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingDetailsServiceTest {

    @InjectMocks
    BookingDetailsService bookingDetailsService;

    @Mock
    BookingDetailsRepository bookingDetailsRepository;

    @Mock
    RoomRepository roomRepository;

    @Mock
    RoomService roomService;

    @Mock
    CustomerService customerService;

    private BookingDetails testBookingDetails;
    private Customer testCustomer;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up a test customer
        testCustomer = new Customer(1, "John Doe", "123 Main St", 30, "1234567890");

        // Set up a test room
        testRoom = new Room(101, "Standard", 2, 100, true, false, true, new ArrayList<>());

        // Set up a test booking details
        testBookingDetails = new BookingDetails();
        testBookingDetails.setBookingId(1);
        testBookingDetails.setDuration(5);
        testBookingDetails.setStartDate(LocalDate.now());
        testBookingDetails.setEndDate(LocalDate.now().plusDays(5));
        testBookingDetails.setModeOfBooking(ModeOfBooking.Online);
        testBookingDetails.setModeOfPayment(ModeOfPayment.Prepaid);
        testBookingDetails.setCustomerList(List.of(testCustomer));
        testBookingDetails.setRoomList(List.of(testRoom));
        testBookingDetails.setBillAmount(500);
        testBookingDetails.setPaidAmount(250);
    }

    @Test
    void testCreateBooking_Success() {
        testBookingDetails.getRoomList().forEach(room -> System.out.println(room.getRoomNumber()));
        testBookingDetails.getCustomerList().forEach(customer -> System.out.println(customer.getCustomerId()));
        // Arrange
        when(customerService.getCustomerById(testCustomer.getCustomerId())).thenReturn(testCustomer);
        when(roomService.getRoomByRoomNumber(testRoom.getRoomNumber())).thenReturn(testRoom);
        when(roomService.checkRoomsAvailability(testBookingDetails.getRoomList())).thenReturn(true);
        when(bookingDetailsRepository.save(testBookingDetails)).thenReturn(testBookingDetails);
        when(roomRepository.save(any(Room.class))).thenReturn(testRoom);

        // Act
        BookingDetails createdBooking = bookingDetailsService.createBooking(testBookingDetails);

        // Assert
        assertNotNull(createdBooking);
        assertEquals(testBookingDetails.getBookingId(), createdBooking.getBookingId());
        verify(bookingDetailsRepository, times(1)).save(testBookingDetails);
    }

    @Test
    void testCreateBooking_RoomNotAvailableException() {
        // Arrange
        when(roomService.checkRoomsAvailability(testBookingDetails.getRoomList())).thenReturn(false);

        // Act & Assert
        assertThrows(RoomNotAvailableException.class, () -> bookingDetailsService.createBooking(testBookingDetails));
    }

    @Test
    void testCreateBooking_AdvancePaymentNotDoneException() {
        // Arrange
        testBookingDetails.setRoomList(List.of(testRoom, testRoom, testRoom,testRoom)); // More than 3 rooms
        testBookingDetails.setPaidAmount(100); // Less than 50% payment
        when(customerService.getCustomerById(testCustomer.getCustomerId())).thenReturn(testCustomer);
        when(roomService.getRoomByRoomNumber(testRoom.getRoomNumber())).thenReturn(testRoom);
        when(roomService.checkRoomsAvailability(testBookingDetails.getRoomList())).thenReturn(true);

        // Act & Assert
        assertThrows(AdvancePaymentNotDoneException.class, () -> bookingDetailsService.createBooking(testBookingDetails));
    }

    @Test
    void testCreateBooking_ChildrenNotAccompaniedByAdultException() {
        // Arrange
        Customer childCustomer = new Customer(2, "Child", "123 Main St", 10, "1234567890");
        testBookingDetails.setRoomList(List.of(testRoom));
        testBookingDetails.setCustomerList(List.of(childCustomer)); // Child without adult
        when(roomService.getRoomByRoomNumber(testRoom.getRoomNumber())).thenReturn(testRoom);
        when(roomService.checkRoomsAvailability(any(List.class))).thenReturn(true);
        when(customerService.getCustomerById(childCustomer.getCustomerId())).thenReturn(childCustomer);

        // Act & Assert
        assertThrows(ChildrenNotAccompaniedByAdultException.class, () -> bookingDetailsService.createBooking(testBookingDetails));
    }

    @Test
    void testGetBookingDetails_Success() {
        // Arrange
        when(bookingDetailsRepository.findById(testBookingDetails.getBookingId())).thenReturn(Optional.of(testBookingDetails));

        // Act
        BookingDetails retrievedBooking = bookingDetailsService.getBookingDetails(testBookingDetails.getBookingId());

        // Assert
        assertNotNull(retrievedBooking);
        assertEquals(testBookingDetails.getBookingId(), retrievedBooking.getBookingId());
    }

    @Test
    void testGetBookingDetails_NotFoundException() {
        // Arrange
        when(bookingDetailsRepository.findById(testBookingDetails.getBookingId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookingDetailsService.getBookingDetails(testBookingDetails.getBookingId()));
    }

    @Test
    void testGetAllBookingDetails() {
        // Arrange
        List<BookingDetails> bookingDetailsList = new ArrayList<>();
        bookingDetailsList.add(testBookingDetails);
        when(bookingDetailsRepository.findAll()).thenReturn(bookingDetailsList);

        // Act
        Iterable<BookingDetails> retrievedBookingDetails = bookingDetailsService.getAllBookingDetails();

        // Assert
        assertNotNull(retrievedBookingDetails);
        assertEquals(1, ((List<BookingDetails>) retrievedBookingDetails).size());
    }

    @Test
    void testUpdateBookingDetails_Success() {
        // Arrange
        testRoom.setAvailability(true);
        testBookingDetails.setRoomList(List.of(testRoom));
        when(bookingDetailsRepository.findById(testBookingDetails.getBookingId())).thenReturn(Optional.of(testBookingDetails));
        when(bookingDetailsRepository.save(any(BookingDetails.class))).thenReturn(testBookingDetails);
        when(roomService.getRoomByRoomNumber(testRoom.getRoomNumber())).thenReturn(testRoom);
        when(roomService.checkRoomsAvailability(testBookingDetails.getRoomList())).thenReturn(true);
        when(roomRepository.save(any(Room.class))).thenReturn(testRoom);
        when(customerService.getCustomerById(testCustomer.getCustomerId())).thenReturn(testCustomer);

        // Act
        BookingDetails updatedBooking = bookingDetailsService.updateBookingDetails(testBookingDetails.getBookingId(), testBookingDetails);

        // Assert
        assertNotNull(updatedBooking);
        assertEquals(testBookingDetails.getBookingId(), updatedBooking.getBookingId());
        verify(bookingDetailsRepository, times(1)).save(testBookingDetails);
    }

    @Test
    void testUpdateBookingDetails_RoomNotAvailableException() {
        // Arrange
        when(bookingDetailsRepository.findById(testBookingDetails.getBookingId())).thenReturn(Optional.of(testBookingDetails));
        when(roomService.checkRoomsAvailability(testBookingDetails.getRoomList())).thenReturn(false);

        // Act & Assert
        assertThrows(RoomNotAvailableException.class, () -> bookingDetailsService.updateBookingDetails(testBookingDetails.getBookingId(), testBookingDetails));
    }

    @Test
    void testUpdateBookingDetails_ChildrenNotAccompaniedByAdultException() {
        // Arrange
        Customer childCustomer = new Customer(2, "Child", "123 Main St", 10, "1234567890");
        testBookingDetails.setCustomerList(List.of(childCustomer)); // Child without adult
        testBookingDetails.setRoomList(List.of(testRoom));
        when(bookingDetailsRepository.findById(testBookingDetails.getBookingId())).thenReturn(Optional.of(testBookingDetails));
        when(customerService.getCustomerById(anyInt())).thenReturn(childCustomer);
        when(roomService.checkRoomsAvailability(testBookingDetails.getRoomList())).thenReturn(true);
        when(roomService.getRoomByRoomNumber(testRoom.getRoomNumber())).thenReturn(testRoom);
        // Act & Assert
        assertThrows(ChildrenNotAccompaniedByAdultException.class, () -> bookingDetailsService.updateBookingDetails(testBookingDetails.getBookingId(), testBookingDetails));
    }

    @Test
    void testDeleteBookingDetails_Success() {
        // Arrange
        when(bookingDetailsRepository.existsById(testBookingDetails.getBookingId())).thenReturn(true);

        // Act
        boolean isDeleted = bookingDetailsService.deleteBookingDetails(testBookingDetails.getBookingId());

        // Assert
        assertTrue(isDeleted);
        verify(bookingDetailsRepository, times(1)).deleteById(testBookingDetails.getBookingId());
    }

    @Test
    void testDeleteBookingDetails_NotFound() {
        // Arrange
        when(bookingDetailsRepository.existsById(testBookingDetails.getBookingId())).thenReturn(false);

        // Act
        boolean isDeleted = bookingDetailsService.deleteBookingDetails(testBookingDetails.getBookingId());

        // Assert
        assertFalse(isDeleted);
        verify(bookingDetailsRepository, never()).deleteById(anyInt());
    }

    @Test
    void testIsAdvancePaymentDone_True() {
        // Arrange
        testBookingDetails.setRoomList(List.of(testRoom, testRoom));
        testBookingDetails.setPaidAmount(250);

        // Act
        boolean isAdvancePaymentDone = bookingDetailsService.isAdvancePaymentDone(testBookingDetails);

        // Assert
        assertTrue(isAdvancePaymentDone);
    }

    @Test
    void testIsAdvancePaymentDone_False() {
        // Arrange
        testBookingDetails.setRoomList(List.of(testRoom, testRoom, testRoom, testRoom));
        testBookingDetails.setPaidAmount(100);

        // Act
        boolean isAdvancePaymentDone = bookingDetailsService.isAdvancePaymentDone(testBookingDetails);

        // Assert
        assertFalse(isAdvancePaymentDone);
    }

    @Test
    void testIsAccompaniedByAdult_True() {
        // Arrange
        Customer adultCustomer = new Customer(2, "Adult", "123 Main St", 30, "9876543210");
        testBookingDetails.setCustomerList(List.of(adultCustomer));

        // Act
        boolean isAccompaniedByAdult = bookingDetailsService.isAccompaniedByAdult(testBookingDetails);

        // Assert
        assertTrue(isAccompaniedByAdult);
    }

    @Test
    void testIsAccompaniedByAdult_False() {
        // Arrange
        Customer childCustomer = new Customer(2, "Child", "123 Main St", 10, "9876543210");
        testBookingDetails.setCustomerList(List.of(childCustomer));

        // Act
        boolean isAccompaniedByAdult = bookingDetailsService.isAccompaniedByAdult(testBookingDetails);

        // Assert
        assertFalse(isAccompaniedByAdult);
    }

}
