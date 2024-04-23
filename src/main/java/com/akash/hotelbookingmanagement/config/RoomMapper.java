package com.akash.hotelbookingmanagement.config;

import com.akash.hotelbookingmanagement.dto.RoomDto;
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
public class RoomMapper {

    private final ModelMapper modelMapper;
    private final CustomerService customerService;

    @Autowired
    public RoomMapper(ModelMapper modelMapper, CustomerService customerService) {
        this.modelMapper = modelMapper;
        this.customerService = customerService;
    }

    public Room convertToEntity(RoomDto roomDto) {
        Room room = modelMapper.map(roomDto, Room.class);
        System.out.println(roomDto.getCheckedInCustomerIdList());

        // Map room ids to Room entities
        if (roomDto.getCheckedInCustomerIdList() != null) {
            System.out.println("hi");
            List<Customer> checkedInCustomers = roomDto.getCheckedInCustomerIdList().stream()
                    .map(customerService::getCustomerById)
                    .collect(Collectors.toList());
            room.setCheckedInCustomers(checkedInCustomers);
        }
        return room;
    }

    public RoomDto convertToDto(Room room) {
        return modelMapper.map(room, RoomDto.class);
    }
}
