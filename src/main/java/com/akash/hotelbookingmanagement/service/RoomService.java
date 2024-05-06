package com.akash.hotelbookingmanagement.service;

import com.akash.hotelbookingmanagement.config.RoomMapper;
import com.akash.hotelbookingmanagement.dto.RoomDto;
import com.akash.hotelbookingmanagement.exception.ResourceNotFoundException;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.repository.RoomRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing rooms.
 */
@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RoomMapper roomMapper;

    /**
     * Creates a new room.
     *
     * @param roomData The data of room to create.
     * @return The created room.
     */
    public Room createRoom(@Valid final RoomDto roomData) {
        Room room = roomMapper.convertToEntity(roomData);
        return roomRepository.save(room);
    }

    /**
     * Retrieves all rooms.
     *
     * @return Iterable of all rooms.
     */
    public Iterable<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    /**
     * Retrieves a room by its room number.
     *
     * @param roomNumber The room number.
     * @return The room with the given room number, or null if not found.
     */
    public Room getRoomByRoomNumber(final Integer roomNumber) {
        return roomRepository.findById(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room details not found with room number: " + roomNumber));
    }

    /**
     * Retrieves rooms by their type.
     *
     * @param type The room type.
     * @return Iterable of rooms with the specified type.
     */
    public Iterable<Room> getRoomsByType(final String type) {
        return roomRepository.findAllByType(type);
    }

    /**
     * Retrieves checked-in customers for a room.
     *
     * @param roomNumber The room number.
     * @return Iterable of checked-in customers for the specified room.
     */
    public Iterable<Customer> getCheckedInCustomers(final Integer roomNumber) {
        Room room = getRoomByRoomNumber(roomNumber);
        return room != null ? room.getCheckedInCustomers() : null;
    }

    /**
     * Updates a room by its room number.
     *
     * @param roomNumber The room number.
     * @param roomData   The updated room information.
     * @return The updated room.
     */
    public Room updateRoomById(final Integer roomNumber, final RoomDto roomData) {

        Room roomOld = getRoomByRoomNumber(roomNumber);
        if (roomOld == null) {
            return null;
        }
        Room room = roomMapper.convertToEntity(roomData);
        if (room.getOccupancy() != null) {
            roomOld.setOccupancy(room.getOccupancy());
        }
        if (room.getType() != null) {
            roomOld.setType(room.getType());
        }
        if (room.getPricePerDay() != null) {
            roomOld.setPricePerDay(room.getPricePerDay());
        }
        if (room.getCheckedInCustomers() != null) {
            roomOld.setCheckedInCustomers(room.getCheckedInCustomers());
        }
        if (room.getIsCheckedIn() != null) {
            roomOld.setIsCheckedIn(room.getIsCheckedIn());
        }
        if (room.getIsCheckedOut() != null) {
            roomOld.setIsCheckedOut(room.getIsCheckedOut());
        }
        if (room.getAvailability() != null) {
            roomOld.setAvailability(room.getAvailability());
        }

        return roomRepository.save(roomOld);
    }

    /**
     * Deletes a room by its room number.
     *
     * @param roomNumber The room number.
     */
    public void deleteRoom(final Integer roomNumber) {
        if (roomRepository.existsById(roomNumber)) {
            roomRepository.deleteById(roomNumber);
        }
    }

    /**
     * Checks if all rooms in the provided list are available.
     *
     * @param roomList The list of rooms to check.
     * @return True if all rooms are available, false otherwise.
     */
    public boolean checkRoomsAvailability(final List<Room> roomList) {
        return roomList.stream().allMatch(room -> getRoomByRoomNumber(room.getRoomNumber()).getAvailability());
    }
}
