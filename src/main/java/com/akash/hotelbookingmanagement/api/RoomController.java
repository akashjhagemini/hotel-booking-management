package com.akash.hotelbookingmanagement.api;

import com.akash.hotelbookingmanagement.dto.RoomDto;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.service.RoomService;

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

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody final RoomDto roomData) {
        LOGGER.info("Request received to create a new room");
        Room savedRoom = roomService.createRoom(roomData);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    @GetMapping
    public ResponseEntity<Iterable<Room>> getAllRooms() {
        LOGGER.info("Request received to fetch all rooms");
        Iterable<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{roomNumber}")
    public ResponseEntity<Room> getRoomByRoomNumber(@PathVariable final Integer roomNumber) {
        LOGGER.info("Request received to fetch room with room number: {}", roomNumber);
        Room room = roomService.getRoomByRoomNumber(roomNumber);
        if (room != null) {
            LOGGER.info("Found room with room number: {}", roomNumber);
            return ResponseEntity.ok(room);
        } else {
            LOGGER.warn("Room with room number: {} not found", roomNumber);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Iterable<Room>> getRoomsByType(@PathVariable final String type) {
        LOGGER.info("Request received to fetch rooms with type: {}", type);
        Iterable<Room> rooms = roomService.getRoomsByType(type);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{roomNumber}/customers")
    public ResponseEntity<Iterable<Customer>> getCheckedInCustomers(@PathVariable final Integer roomNumber) {
        LOGGER.info("Request received to fetch checked-in customers for room with room number: {}", roomNumber);
        Iterable<Customer> customers = roomService.getCheckedInCustomers(roomNumber);
        if (customers != null) {
            LOGGER.info("Fetched checked-in customers for room with room number: {}", roomNumber);
            return ResponseEntity.ok(customers);
        } else {
            LOGGER.warn("No checked-in customers found for room with room number: {}", roomNumber);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{roomNumber}")
    public ResponseEntity<Room> updateRoomById(@PathVariable final Integer roomNumber, @RequestBody final RoomDto roomData) {
        LOGGER.info("Request received to update room with room number: {}", roomNumber);
        Room updatedRoom = roomService.updateRoomById(roomNumber, roomData);
        if (updatedRoom != null) {
            LOGGER.info("Updated room with room number: {}", roomNumber);
            return ResponseEntity.ok(updatedRoom);
        } else {
            LOGGER.warn("Room with room number: {} not found", roomNumber);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{roomNumber}")
    public ResponseEntity<Void> deleteRoom(@PathVariable final Integer roomNumber) {
        LOGGER.info("Request received to delete room with room number: {}", roomNumber);
        roomService.deleteRoom(roomNumber);
        LOGGER.info("Deleted room with room number: {}", roomNumber);
        return ResponseEntity.noContent().build();
    }
}
