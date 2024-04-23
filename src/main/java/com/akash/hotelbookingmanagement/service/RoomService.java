package com.akash.hotelbookingmanagement.service;

import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.repository.RoomRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing rooms.
 */
@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    /**
     * Creates a new room.
     *
     * @param room The room to create.
     */
    public Room createRoom(@Valid Room room) {
        Room savedRoom=roomRepository.save(room);
        return savedRoom;
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
        return roomRepository.findById(roomNumber).orElse(null);
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
        Room room = roomRepository.findById(roomNumber).orElse(null);
        if (room != null && room.getCheckedInCustomers() != null) {
            return room.getCheckedInCustomers();
        } else {
            return null;
        }
    }

    /**
     * Updates a room by its room number.
     *
     * @param roomNumber The room number.
     * @param room       The updated room information.
     */
    public Room updateRoomById(final Integer roomNumber, final Room room) {
//        System.out.println("hi");
        Room roomOld = roomRepository.findById(roomNumber).orElse(null);
        if(roomOld==null){
            return null;
        }
        if (room.getOccupancy() != null) {
            roomOld.setOccupancy(room.getOccupancy());
        }
        if (room.getType() != null) {
            roomOld.setType(room.getType());
        }
        if (room.getPricePerDay() != null) {
            roomOld.setPricePerDay(room.getPricePerDay());
        }
        if (room.getAvailability() != null) {
            roomOld.setAvailability(room.getAvailability());
        }
        if(room.getIsCheckedIn()!=null){
            roomOld.setIsCheckedIn(room.getIsCheckedIn());
        }
        if(room.getIsCheckedOut()!=null){
            roomOld.setIsCheckedOut(room.getIsCheckedOut());
        }
        if (room.getCheckedInCustomers() != null) {
            roomOld.setCheckedInCustomers(room.getCheckedInCustomers());
        }
        Room savedRoom=roomRepository.save(roomOld);
        return savedRoom;
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
}
