package com.akash.hotelbookingmanagement.api;

import com.akash.hotelbookingmanagement.model.BookingDetails;
import com.akash.hotelbookingmanagement.dto.BookingDetailsDto;
import com.akash.hotelbookingmanagement.service.BookingDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for managing booking-related API endpoints.
 */
@RestController
@RequestMapping("/booking")
public class BookingDetailsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingDetailsController.class);

    @Autowired
    private BookingDetailsService bookingDetailsService;

    /**
     * Creates a new booking.
     *
     * @param bookingDetailsData The booking details to create.
     * @return ResponseEntity representing the HTTP response.
     */
    @PostMapping("/")
    public ResponseEntity<BookingDetails> createBookingDetails(@RequestBody final BookingDetailsDto bookingDetailsData) {
        LOGGER.info("Request received to create a new booking");
        BookingDetails createdBooking = bookingDetailsService.createBooking(bookingDetailsData);
        if (createdBooking != null) {
            LOGGER.info("New booking created successfully");
            return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
        } else {
            LOGGER.error("Failed to create a new booking");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves all bookings.
     *
     * @return ResponseEntity representing the HTTP response.
     */
    @GetMapping("/")
    public ResponseEntity<Iterable<BookingDetails>> getAllBookingDetails() {
        LOGGER.info("Request received to fetch all bookings");
        Iterable<BookingDetails> bookingDetailsList = bookingDetailsService.getAllBookingDetails();
        return new ResponseEntity<>(bookingDetailsList, HttpStatus.OK);
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param id The ID of the booking.
     * @return ResponseEntity representing the HTTP response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingDetails> getBookingDetailsById(@PathVariable final Integer id) {
        LOGGER.info("Request received to fetch booking with ID: {}", id);
        BookingDetails bookingDetails = bookingDetailsService.getBookingDetails(id);
        if (bookingDetails != null) {
            LOGGER.info("Found booking with ID: {}", id);
            return new ResponseEntity<>(bookingDetails, HttpStatus.OK);
        } else {
            LOGGER.warn("Booking with ID: {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates a booking.
     *
     * @param id             The ID of the booking to update.
     * @param bookingDetailsData The updated booking details.
     * @return ResponseEntity representing the HTTP response.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingDetails> updateBookingDetails(@PathVariable final Integer id, @RequestBody final BookingDetailsDto bookingDetailsData) {
        LOGGER.info("Request received to update booking with ID: {}", id);
        BookingDetails updatedBooking = bookingDetailsService.updateBookingDetails(id, bookingDetailsData);
        if (updatedBooking != null) {
            LOGGER.info("Updated booking with ID: {}", id);
            return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
        } else {
            LOGGER.warn("Booking with ID: {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes a booking by its ID.
     *
     * @param id The ID of the booking to delete.
     * @return ResponseEntity representing the HTTP response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingDetails(@PathVariable final Integer id) {
        LOGGER.info("Request received to delete booking with ID: {}", id);
        boolean deleted = bookingDetailsService.deleteBookingDetails(id);
        if (deleted) {
            LOGGER.info("Deleted booking with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            LOGGER.warn("Booking with ID: {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
