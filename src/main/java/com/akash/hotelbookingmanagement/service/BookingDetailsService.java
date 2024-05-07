package com.akash.hotelbookingmanagement.service;

import com.akash.hotelbookingmanagement.config.BookingDetailsMapper;
import com.akash.hotelbookingmanagement.exception.AdvancePaymentNotDoneException;
import com.akash.hotelbookingmanagement.exception.ChildrenNotAccompaniedByAdultException;
import com.akash.hotelbookingmanagement.exception.RoomNotAvailableException;
import com.akash.hotelbookingmanagement.exception.ResourceNotFoundException;
import com.akash.hotelbookingmanagement.model.BookingDetails;
import com.akash.hotelbookingmanagement.dto.BookingDetailsDto;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.repository.BookingDetailsRepository;
import com.akash.hotelbookingmanagement.repository.RoomRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    @Autowired
    private BookingDetailsMapper bookingDetailsMapper;

    private static final int MAX_NUMBER_OF_ROOMS_WITHOUT_ANY_ADV_PAYMENT_REQUIREMENT = 3;
    private static final int MIN_ADULT_AGE = 18;


    /**
     * Creates a new booking.
     *
     * @param bookingDetailsData The booking details to create.
     * @return The created booking details.
     */
    public BookingDetails createBooking(@Valid final BookingDetailsDto bookingDetailsData) {

        BookingDetails bookingDetails = bookingDetailsMapper.convertToEntity(bookingDetailsData);

        //check if all the rooms are available or not
        if (!roomService.checkRoomsAvailability(bookingDetails.getRoomList())) {
            throw new RoomNotAvailableException("All rooms selected are currently not available");
        }

        //check if children are accompanied by atleast one adult
        if (!isAccompaniedByAdult(bookingDetails)) {
            throw new ChildrenNotAccompaniedByAdultException("At least one adult must be present with children");
        }

        //calculate bill amount based on duration of booking and price-per-day of rooms
        bookingDetails.setBillAmount(bookingDetails.getRoomList().stream().mapToInt(Room::getPricePerDay).sum());

        //now check for constraint that if number of rooms booked is greater than 3 than advanced payment of atleast 50% of bill amount must be done
        if (!isAdvancePaymentDone(bookingDetails)) {
            throw new AdvancePaymentNotDoneException("For number of customers more than 3, at least 50% payment must be done");
        }

        BookingDetails savedBookingDetails = bookingDetailsRepository.save(bookingDetails);

        //set the availability of every room to be false after saving to avoid possibility of error
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
     * @param bookingDetailsData The updated booking details.
     * @return The updated booking details if successful, otherwise null.
     */
    public BookingDetails updateBookingDetails(final Integer id, final BookingDetailsDto bookingDetailsData) {
        BookingDetails existingBooking = getBookingDetails(id);
        BookingDetails bookingDetails = bookingDetailsMapper.convertToEntity(bookingDetailsData);

        //check if all the rooms are available or not
        if (bookingDetails.getRoomList() != null && !roomService.checkRoomsAvailability(bookingDetails.getRoomList())) {
            throw new RoomNotAvailableException("All rooms selected are currently not available");
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
        if (bookingDetails.getPaidAmount() != null) {
            existingBooking.setPaidAmount(bookingDetails.getPaidAmount());
        }
        if (bookingDetails.getCustomerList() != null) {
            existingBooking.setCustomerList(bookingDetails.getCustomerList());
        }
        if (bookingDetails.getRoomList() != null) {
            existingBooking.setRoomList(bookingDetails.getRoomList());
        }


        if (existingBooking.getRoomList() != null) {
            existingBooking.setBillAmount(existingBooking.getRoomList().stream().mapToInt(Room::getPricePerDay).sum());
        }

        if (!isAccompaniedByAdult(existingBooking)) {
            throw new ChildrenNotAccompaniedByAdultException("At least one adult must be present with children");
        }

        if (!isAdvancePaymentDone(existingBooking)) {
            throw new AdvancePaymentNotDoneException("For number of rooms more than 3, at least 50% payment must be done");
        }

        BookingDetails savedBookingDetails = bookingDetailsRepository.save(existingBooking);

        //set the availability of every room to be false after saving to avoid possibility of error
        if (bookingDetails.getRoomList() != null) {
            bookingDetails.getRoomList().forEach(room -> {
                room = roomService.getRoomByRoomNumber(room.getRoomNumber());
                room.setAvailability(false);
                roomRepository.save(room);
            });
        }

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
