package com.akash.hotelbookingmanagement.config;

import com.akash.hotelbookingmanagement.dto.RoomDto;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.service.CustomerService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class RoomMapper {

    private final ModelMapper modelMapper;
    private final CustomerService customerService;

    @Autowired
    public RoomMapper(final ModelMapper modelMapper, final CustomerService customerService) {
        this.modelMapper = modelMapper;
        this.customerService = customerService;
    }

    public Room convertToEntity(final RoomDto roomDto) {
        Room room = modelMapper.map(roomDto, Room.class);

        // Map room ids to Room entities
        if (roomDto.getCheckedInCustomerIdList() != null) {
            List<Customer> checkedInCustomers = roomDto.getCheckedInCustomerIdList().stream()
                    .map(customerService::getCustomerById)
                    .toList();
            room.setCheckedInCustomers(checkedInCustomers);
        }
        return room;
    }

    public RoomDto convertToDto(final Room room) {
        return modelMapper.map(room, RoomDto.class);
    }
}
