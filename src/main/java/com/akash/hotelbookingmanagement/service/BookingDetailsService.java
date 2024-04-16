package com.akash.hotelbookingmanagement.service;

import com.akash.hotelbookingmanagement.model.BookingDetails;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.repository.BookingDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing booking details.
 */
@Service
public class BookingDetailsService {

    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;

    /**
     * Creates a new booking.
     *
     * @param bookingDetails The booking details to create.
     * @return The created booking details.
     */
    public BookingDetails createBooking(final BookingDetails bookingDetails) {
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
        if (existingBooking == null) {
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
}
