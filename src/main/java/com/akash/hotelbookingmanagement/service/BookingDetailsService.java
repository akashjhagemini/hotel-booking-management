package com.akash.hotelbookingmanagement.service;

import com.akash.hotelbookingmanagement.exception.AdvancePaymentNotDoneException;
import com.akash.hotelbookingmanagement.exception.ChildrenNotAccompaniedByAdultException;
import com.akash.hotelbookingmanagement.exception.RoomNotAvailableException;
import com.akash.hotelbookingmanagement.exception.ResourceNotFoundException;
import com.akash.hotelbookingmanagement.model.BookingDetails;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.repository.BookingDetailsRepository;
import com.akash.hotelbookingmanagement.repository.RoomRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * Service class for managing booking details.
 */
@Service
public class BookingDetailsService {

    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    private static final int MAX_NUMBER_OF_ROOMS_WITHOUT_ANY_ADV_PAYMENT_REQUIREMENT = 3;
    private static final int MIN_ADULT_AGE = 18;


    /**
     * Creates a new booking.
     *
     * @param bookingDetails The booking details to create.
     * @return The created booking details.
     */
    public BookingDetails createBooking(@Valid final BookingDetails bookingDetails) {

        // populate customers from customerId
        List<Customer> customerList = bookingDetails.getCustomerList().stream().map(customer ->
            customerService.getCustomerById(customer.getCustomerId())
        ).toList();

        bookingDetails.setCustomerList(customerList);

        //check if all the rooms are available or not
        if (!roomService.checkRoomsAvailability(bookingDetails.getRoomList())) {
            throw new RoomNotAvailableException("All rooms selected are currently not available");
        }

        //populate all rooms from roomNumber
        List<Room> roomList = bookingDetails.getRoomList().stream().map(room ->
            roomService.getRoomByRoomNumber(room.getRoomNumber())
        ).toList();

        bookingDetails.setRoomList(roomList);

        if (!isAccompaniedByAdult(bookingDetails)) {
            throw new ChildrenNotAccompaniedByAdultException("At least one adult must be present with children");
        }

        if (!isAdvancePaymentDone(bookingDetails)) {
            throw new AdvancePaymentNotDoneException("For number of customers more than 3, at least 50% payment must be done");
        }

        // from the list of rooms calculate the total bill amount from price-per-day and duration
        bookingDetails.setBillAmount(bookingDetails.getRoomList().stream().mapToInt(Room::getPricePerDay).sum());

        BookingDetails savedBookingDetails = bookingDetailsRepository.save(bookingDetails);

        //set the availability of every room to be false after saving so as to avoid possibility of error
        bookingDetails.getRoomList().forEach(room -> {
            room = roomService.getRoomByRoomNumber(room.getRoomNumber());
            room.setAvailability(false);
            roomRepository.save(room);
        });
        return savedBookingDetails;
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param id The ID of the booking.
     * @return The booking details if found, otherwise null.
     */
    public BookingDetails getBookingDetails(final Integer id) {
        return bookingDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking details not found with id: " + id));
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

        // populate customers from customerId
        if (bookingDetails.getCustomerList() != null) {
            List<Customer> customerList = bookingDetails.getCustomerList().stream().map(customer ->
                customerService.getCustomerById(customer.getCustomerId())
            ).toList();

            existingBooking.setCustomerList(customerList);
        }

        if (bookingDetails.getRoomList() != null) {
            //check if all the rooms are available or not
            if (!roomService.checkRoomsAvailability(bookingDetails.getRoomList())) {
                throw new RoomNotAvailableException("All rooms selected are currently not available");
            }
            //populate all rooms from roomNumber
            List<Room> roomList = bookingDetails.getRoomList().stream().map(room ->
                roomService.getRoomByRoomNumber(room.getRoomNumber())
            ).toList();

            existingBooking.setRoomList(roomList);
        }

        if (!isAccompaniedByAdult(bookingDetails)) {
            throw new ChildrenNotAccompaniedByAdultException("At least one adult must be present with children");
        }

        if (!isAdvancePaymentDone(bookingDetails)) {
            throw new AdvancePaymentNotDoneException("For number of customers more than 2, at least 50% payment must be done");
        }

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
        if (bookingDetails.getBillAmount() != null) {
            existingBooking.setBillAmount(bookingDetails.getBillAmount());
        }
        if (bookingDetails.getPaidAmount() != null) {
            existingBooking.setPaidAmount(bookingDetails.getPaidAmount());
        }

        if (existingBooking.getRoomList() != null) {
            existingBooking.setBillAmount(existingBooking.getRoomList().stream().mapToInt(Room::getPricePerDay).sum());
        }

        BookingDetails savedBookingDetails = bookingDetailsRepository.save(existingBooking);

        //set the availability of every room to be false after saving so as to avoid possibility of error
        bookingDetails.getRoomList().forEach(room -> {
            room = roomService.getRoomByRoomNumber(room.getRoomNumber());
            room.setAvailability(false);
            roomRepository.save(room);
        });

        return savedBookingDetails;
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
     * @param bookingDetails the details of booking
     * @return True if advance payment is done, false otherwise.
     */
    public boolean isAdvancePaymentDone(final BookingDetails bookingDetails) {
        if (bookingDetails.getRoomList() == null || bookingDetails.getRoomList().size() <= MAX_NUMBER_OF_ROOMS_WITHOUT_ANY_ADV_PAYMENT_REQUIREMENT) {
            return true;
        }
        return (bookingDetails.getBillAmount() / 2 <= bookingDetails.getPaidAmount());
    }

    /**
     * Checks if children are accompanied by at least one adult.
     *
     * @param bookingDetails the details of booking
     * @return True if children are not accompanied by adult, false otherwise.
     */
    public boolean isAccompaniedByAdult(final BookingDetails bookingDetails) {
        if (bookingDetails.getCustomerList() == null) {
            return true;
        }
        long children = bookingDetails.getCustomerList().stream().filter(customer -> customer.getAge() < MIN_ADULT_AGE).count();
        return children != bookingDetails.getCustomerList().size();
    }

}
