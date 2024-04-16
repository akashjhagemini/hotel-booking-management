package com.akash.hotelbookingmanagement.api;

import com.akash.hotelbookingmanagement.model.BookingDetails;
import com.akash.hotelbookingmanagement.service.BookingDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing booking-related API endpoints.
 */
@RestController
@RequestMapping("/booking")
public class BookingDetailsController {

    @Autowired
        private BookingDetailsService bookingDetailsService;

    /**
     * Creates a new booking.
     *
     * @param bookingDetails The booking details to create.
     * @return ResponseEntity representing the HTTP response.
     */
    @PostMapping("/")
    public ResponseEntity<BookingDetails> createBookingDetails(@RequestBody final BookingDetails bookingDetails) {
        ResponseEntity<BookingDetails> responseEntity = validateBookingDetails(bookingDetails);
        if (responseEntity != null) {
            return responseEntity;
        }
        BookingDetails createdBooking = bookingDetailsService.createBooking(bookingDetails);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    /**
     * Retrieves all bookings.
     *
     * @return ResponseEntity representing the HTTP response.
     */
    @GetMapping("/")
    public ResponseEntity<Iterable<BookingDetails>> getAllBookingDetails() {
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
        BookingDetails bookingDetails = bookingDetailsService.getBookingDetails(id);
        if (bookingDetails != null) {
            return new ResponseEntity<>(bookingDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates a booking.
     *
     * @param id             The ID of the booking to update.
     * @param bookingDetails The updated booking details.
     * @return ResponseEntity representing the HTTP response.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookingDetails> updateBookingDetails(@PathVariable final Integer id, @RequestBody final BookingDetails bookingDetails) {
        ResponseEntity<BookingDetails> responseEntity = validateBookingDetails(bookingDetails);
        if (responseEntity != null) {
            return responseEntity;
        }
        BookingDetails updatedBooking = bookingDetailsService.updateBookingDetails(id, bookingDetails);
        if (updatedBooking != null) {
            return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
        } else {
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
        boolean deleted = bookingDetailsService.deleteBookingDetails(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Validates booking details before creating or updating a booking.
     *
     * @param bookingDetails The booking details to validate.
     * @return ResponseEntity with error message if validation fails, otherwise null.
     */
    private ResponseEntity<BookingDetails> validateBookingDetails(BookingDetails bookingDetails) {
        if (bookingDetails.isNotAccompaniedByAdult()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!bookingDetails.isAdvancePaymentDone()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return null;
    }

}
