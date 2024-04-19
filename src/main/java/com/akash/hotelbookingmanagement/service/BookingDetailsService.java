package com.akash.hotelbookingmanagement.service;

import com.akash.hotelbookingmanagement.model.BookingDetails;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.repository.BookingDetailsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing booking details.
 */
@Service
public class BookingDetailsService {

    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    RoomService roomService;

    /**
     * Creates a new booking.
     *
     * @param bookingDetails The booking details to create.
     * @return The created booking details.
     */
    public BookingDetails createBooking(@Valid final BookingDetails bookingDetails) {

        List<Customer> customerList = bookingDetails.getCustomerList().stream().map(customer -> {
            return customerService.getCustomerById(customer.getCustomerId());
        }).collect(Collectors.toList());

        bookingDetails.setCustomerList(customerList);

        List<Room> roomList = bookingDetails.getRoomList().stream().map(room -> {
            return roomService.getRoomByRoomNumber(room.getRoomNumber());
        }).collect(Collectors.toList());

        bookingDetails.setRoomList(roomList);

        Boolean flag = validateBookingDetails(bookingDetails);
        if (validateBookingDetails(bookingDetails)) {
            return null;
        }

        bookingDetails.setBillAmount(bookingDetails.getRoomList().stream().mapToInt(Room::getPricePerDay).reduce(0, Integer::sum));
        return bookingDetailsRepository.save(bookingDetails);
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param id The ID of the booking.
     * @return The booking details if found, otherwise null.
     */
    public BookingDetails getBookingDetails(final Integer id) {
        Optional<BookingDetails> optionalBookingDetails = bookingDetailsRepository.findById(id);
        return optionalBookingDetails.orElse(null);
    }

    /**
     * Retrieves all bookings.
     *
     * @return Iterable containing all bookings.
     */
    public Iterable<BookingDetails> getAllBookingDetails() {
        return bookingDetailsRepository.findAll();
    }

    /**
     * Updates a booking.
     *
     * @param id             The ID of the booking to update.
     * @param bookingDetails The updated booking details.
     * @return The updated booking details if successful, otherwise null.
     */
    public BookingDetails updateBookingDetails(final Integer id, final BookingDetails bookingDetails) {
        BookingDetails existingBooking = getBookingDetails(id);

        if(bookingDetails.getCustomerList()!=null){
            List<Customer> customerList = bookingDetails.getCustomerList().stream().map(customer -> {
                return customerService.getCustomerById(customer.getCustomerId());
            }).collect(Collectors.toList());

            bookingDetails.setCustomerList(customerList);
        }

        if(bookingDetails.getRoomList()!=null){
            List<Room> roomList = bookingDetails.getRoomList().stream().map(room -> {
                return roomService.getRoomByRoomNumber(room.getRoomNumber());
            }).collect(Collectors.toList());

            bookingDetails.setRoomList(roomList);
        }

        Boolean flag = validateBookingDetails(bookingDetails);
        if (validateBookingDetails(bookingDetails)) {
            return null;
        }

        // Update booking details
        if (bookingDetails.getDuration() != null) {
            existingBooking.setDuration(bookingDetails.getDuration());
        }
        if (bookingDetails.getStartDate() != null) {
            existingBooking.setStartDate(bookingDetails.getStartDate());
        }
        if (bookingDetails.getEndDate() != null) {
            existingBooking.setEndDate(bookingDetails.getEndDate());
        }
        if (bookingDetails.getModeOfBooking() != null) {
            existingBooking.setModeOfBooking(bookingDetails.getModeOfBooking());
        }
        if (bookingDetails.getModeOfPayment() != null) {
            existingBooking.setModeOfPayment(bookingDetails.getModeOfPayment());
        }
        if (bookingDetails.getCustomerList() != null) {
            existingBooking.setCustomerList(bookingDetails.getCustomerList());
        }
        if (bookingDetails.getRoomList() != null) {
            existingBooking.setRoomList(bookingDetails.getRoomList());
        }
        if (bookingDetails.getBillAmount() != null) {
            existingBooking.setBillAmount(bookingDetails.getBillAmount());
        }
        if (bookingDetails.getPaidAmount() != null) {
            existingBooking.setPaidAmount(bookingDetails.getPaidAmount());
        }

        // Recalculate bill amount
        if(existingBooking.getRoomList()!=null) {
            existingBooking.setBillAmount(existingBooking.getRoomList().stream().mapToInt(Room::getPricePerDay).sum());
        }

        return bookingDetailsRepository.save(existingBooking);
    }

    /**
     * Deletes a booking by its ID.
     *
     * @param id The ID of the booking to delete.
     * @return true if the booking was deleted, false otherwise.
     */
    public boolean deleteBookingDetails(final Integer id) {
        if (bookingDetailsRepository.existsById(id)) {
            bookingDetailsRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the advance payment has been made for booking more than 3 rooms.
     *
     * @return True if advance payment is done, false otherwise.
     */
    public boolean isAdvancePaymentDone(BookingDetails bookingDetails) {
        return bookingDetails.getRoomList().size() > 3 && bookingDetails.getBillAmount() / 2 <= bookingDetails.getPaidAmount();
    }

    /**
     * Checks if children are accompanied by at least one adult.
     *
     * @return True if children are not accompanied by adult, false otherwise.
     */
    public boolean isAccompaniedByAdult(BookingDetails bookingDetails) {
        int children = (int) bookingDetails.getCustomerList().stream().filter(customer -> customer.getAge() < 18).count();
        return children != bookingDetails.getCustomerList().size();
    }

    /**
     * Validates booking details before creating or updating a booking.
     *
     * @param bookingDetails The booking details to validate.
     * @return ResponseEntity with error message if validation fails, otherwise null.
     */
    private Boolean validateBookingDetails(BookingDetails bookingDetails) {
        return isAccompaniedByAdult(bookingDetails)&&isAdvancePaymentDone(bookingDetails);
    }
}
