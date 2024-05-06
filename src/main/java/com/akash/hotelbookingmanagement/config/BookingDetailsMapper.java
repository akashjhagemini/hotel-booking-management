package com.akash.hotelbookingmanagement.config;

import com.akash.hotelbookingmanagement.model.BookingDetails;
import com.akash.hotelbookingmanagement.dto.BookingDetailsDto;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.service.CustomerService;
import com.akash.hotelbookingmanagement.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingDetailsMapper {

    private final ModelMapper modelMapper;
    private final CustomerService customerService;
    private final RoomService roomService;

    @Autowired
    public BookingDetailsMapper(final ModelMapper modelMapper, final CustomerService customerService, final RoomService roomService) {
        this.modelMapper = modelMapper;
        this.customerService = customerService;
        this.roomService = roomService;
    }

    /**
     * Converts BookingDetailsDto to BookingDetails entity.
     *
     * @param bookingDetailsDto The BookingDetailsDto to convert.
     * @return BookingDetails entity.
     */
    public BookingDetails convertToEntity(final BookingDetailsDto bookingDetailsDto) {
        BookingDetails bookingDetails = modelMapper.map(bookingDetailsDto, BookingDetails.class);

        // Map customer ids to Customer entities
        if (bookingDetailsDto.getCustomerIdList() != null && !bookingDetailsDto.getCustomerIdList().isEmpty()) {
            List<Customer> customers = bookingDetailsDto.getCustomerIdList().stream()
                    .map(customerService::getCustomerById)
                    .toList();
            bookingDetails.setCustomerList(customers);
        }

        // Map room numbers to Room entities
        if (bookingDetailsDto.getRoomNumberList() != null && !bookingDetailsDto.getRoomNumberList().isEmpty()) {
            List<Room> rooms = bookingDetailsDto.getRoomNumberList().stream()
                    .map(roomService::getRoomByRoomNumber)
                    .toList();
            bookingDetails.setRoomList(rooms);
        }

        return bookingDetails;
    }

    /**
     * Converts BookingDetails entity to BookingDetailsDto.
     *
     * @param bookingDetails The BookingDetails entity to convert.
     * @return BookingDetailsDto.
     */
    public BookingDetailsDto convertToDto(final BookingDetails bookingDetails) {
        BookingDetailsDto bookingDetailsDto = modelMapper.map(bookingDetails, BookingDetailsDto.class);

        // Extract customer ids from Customer entities
        if (bookingDetails.getCustomerList() != null && !bookingDetails.getCustomerList().isEmpty()) {
            List<Integer> customerIds = bookingDetails.getCustomerList().stream()
                    .map(Customer::getCustomerId)
                    .toList();
            bookingDetailsDto.setCustomerIdList(customerIds);
        }

        // Extract room numbers from Room entities
        if (bookingDetails.getRoomList() != null && !bookingDetails.getRoomList().isEmpty()) {
            List<Integer> roomNumbers = bookingDetails.getRoomList().stream()
                    .map(Room::getRoomNumber)
                    .toList();
            bookingDetailsDto.setRoomNumberList(roomNumbers);
        }

        return bookingDetailsDto;
    }
}
