package com.akash.hotelbookingmanagement.api;

import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller class for managing rooms.
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    /**
     * Creates a new room.
     *
     * @param room The room to create.
     * @return ResponseEntity representing the HTTP response.
     */
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody final Room room) {
        Room savedRoom = roomService.createRoom(room);
        if (savedRoom != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves all rooms.
     *
     * @return ResponseEntity representing the HTTP response.
     */
    @GetMapping
    public ResponseEntity<Iterable<Room>> getAllRooms() {
        Iterable<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    /**
     * Retrieves a room by its room number.
     *
     * @param roomNumber The room number.
     * @return ResponseEntity representing the HTTP response.
     */
    @GetMapping("/{roomNumber}")
    public ResponseEntity<Room> getRoomByRoomNumber(@PathVariable final Integer roomNumber) {
        Room room = roomService.getRoomByRoomNumber(roomNumber);
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves rooms by their type.
     *
     * @param type The room type.
     * @return ResponseEntity representing the HTTP response.
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<Iterable<Room>> getRoomsByType(@PathVariable final String type) {
        Iterable<Room> rooms = roomService.getRoomsByType(type);
        return ResponseEntity.ok(rooms);
    }

    /**
     * Retrieves checked-in customers for a room.
     *
     * @param roomNumber The room number.
     * @return ResponseEntity representing the HTTP response.
     */
    @GetMapping("/{roomNumber}/customers")
    public ResponseEntity<Iterable<Customer>> getCheckedInCustomers(@PathVariable final Integer roomNumber) {
        Iterable<Customer> customers = roomService.getCheckedInCustomers(roomNumber);
        if (customers != null) {
            return ResponseEntity.ok(customers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates a room by its room number.
     *
     * @param roomNumber The room number.
     * @param room       The updated room information.
     * @return ResponseEntity representing the HTTP response.
     */
    @PutMapping("/{roomNumber}")
    public ResponseEntity<Room> updateRoomById(@PathVariable final Integer roomNumber, @RequestBody final Room room) {
        Room updatedRoom = roomService.updateRoomById(roomNumber, room);
        if (updatedRoom != null) {
            return ResponseEntity.ok(updatedRoom);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a room by its room number.
     *
     * @param roomNumber The room number.
     * @return ResponseEntity representing the HTTP response.
     */
    @DeleteMapping("/{roomNumber}")
    public ResponseEntity<Void> deleteRoom(@PathVariable final Integer roomNumber) {
        roomService.deleteRoom(roomNumber);
        return ResponseEntity.noContent().build();
    }
}
