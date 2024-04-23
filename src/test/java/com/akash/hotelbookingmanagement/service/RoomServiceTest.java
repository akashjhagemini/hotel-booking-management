package com.akash.hotelbookingmanagement.service;

import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.repository.RoomRepository;
import com.akash.hotelbookingmanagement.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRoom() {
        // Arrange
        Room room = Room.builder()
                .type("Single")
                .pricePerDay(100)
                .occupancy(2)
                .isCheckedIn(true)
                .isCheckedOut(false)
                .build();
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act
        Room savedRoom = roomService.createRoom(room);

        // Assert
        assertNotNull(savedRoom);
        assertEquals(room, savedRoom);
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void testGetAllRooms() {
        // Arrange
        List<Room> rooms = new ArrayList<>();
        rooms.add(Room.builder()
                .type("Single")
                .pricePerDay(100)
                .occupancy(2)
                .isCheckedIn(true)
                .isCheckedOut(false).build());
        rooms.add(Room.builder().type("Double").pricePerDay(150).occupancy(4).isCheckedIn(true).isCheckedOut(false).build());
        when(roomRepository.findAll()).thenReturn(rooms);

        // Act
        Iterable<Room> result = roomService.getAllRooms();

        // Assert
        assertNotNull(result);
        assertEquals(rooms, result);
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void testGetRoomByRoomNumber_RoomFound() {
        // Arrange
        Integer roomNumber = 101;
        Room room = Room.builder()
                .type("Single")
                .pricePerDay(100)
                .occupancy(2)
                .isCheckedIn(true)
                .isCheckedOut(false).build();
        when(roomRepository.findById(roomNumber)).thenReturn(Optional.of(room));

        // Act
        Room result = roomService.getRoomByRoomNumber(roomNumber);

        // Assert
        assertNotNull(result);
        assertEquals(room, result);
        verify(roomRepository, times(1)).findById(roomNumber);
    }

    @Test
    void testGetRoomByRoomNumber_RoomNotFound() {
        // Arrange
        Integer roomNumber = 101;
        when(roomRepository.findById(roomNumber)).thenReturn(Optional.empty());

        // Act
        Room result = roomService.getRoomByRoomNumber(roomNumber);

        // Assert
        assertNull(result);
        verify(roomRepository, times(1)).findById(roomNumber);
    }

    @Test
    void testGetRoomsByType() {
        // Arrange
        String roomType = "Single";
        List<Room> rooms = new ArrayList<>();
        rooms.add(Room.builder()
                .type("Single")
                .pricePerDay(100)
                .occupancy(2)
                .isCheckedIn(true)
                .isCheckedOut(false).build());
        rooms.add(Room.builder().type("Single").pricePerDay(120).occupancy(2).isCheckedIn(true).isCheckedOut(false).build());
        when(roomRepository.findAllByType(roomType)).thenReturn(rooms);

        // Act
        Iterable<Room> result = roomService.getRoomsByType(roomType);

        // Assert
        assertNotNull(result);
        assertEquals(rooms, result);
        verify(roomRepository, times(1)).findAllByType(roomType);
    }

    @Test
    void testGetCheckedInCustomers_RoomFound() {
        // Arrange
        Integer roomNumber = 101;
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("John Doe", "123 Main St", 30, "1234567890"));
        customers.add(new Customer("Jane Doe", "456 Elm St", 25, "9876543210"));
        Room room = Room.builder()
                .type("Single")
                .pricePerDay(100)
                .occupancy(2)
                .isCheckedIn(true)
                .isCheckedOut(false)
                .checkedInCustomers(customers).build();
        when(roomRepository.findById(roomNumber)).thenReturn(Optional.of(room));

        // Act
        Iterable<Customer> result = roomService.getCheckedInCustomers(roomNumber);

        // Assert
        assertNotNull(result);
        assertEquals(customers, result);
        verify(roomRepository, times(1)).findById(roomNumber);
    }

    @Test
    void testGetCheckedInCustomers_RoomNotFound() {
        // Arrange
        Integer roomNumber = 101;
        when(roomRepository.findById(roomNumber)).thenReturn(Optional.empty());

        // Act
        Iterable<Customer> result = roomService.getCheckedInCustomers(roomNumber);

        // Assert
        assertNull(result);
        verify(roomRepository, times(1)).findById(roomNumber);
    }

    @Test
    void testUpdateRoomById_RoomFound() {
        // Arrange
        Integer roomNumber = 101;
        Room roomOld = Room.builder()
                .type("Single")
                .pricePerDay(100)
                .occupancy(2)
                .isCheckedIn(true)
                .isCheckedOut(false).build();
        Room roomNew = Room.builder().type("Double").pricePerDay(150).occupancy(4).isCheckedIn(true).isCheckedOut(false).build();
        when(roomRepository.findById(roomNumber)).thenReturn(Optional.of(roomOld));
        when(roomRepository.save(any(Room.class))).thenReturn(roomNew);

        // Act
        Room result = roomService.updateRoomById(roomNumber, roomNew);

        // Assert
        assertNotNull(result);
        assertEquals(roomNew, result);
        verify(roomRepository, times(1)).findById(roomNumber);
        verify(roomRepository, times(1)).save(roomOld);
    }

    @Test
    void testUpdateRoomById_RoomNotFound() {
        // Arrange
        Integer roomNumber = 101;
        Room roomNew = Room.builder()
                .type("Double")
                .pricePerDay(150)
                .occupancy(4)
                .isCheckedIn(true)
                .isCheckedOut(false).build();
        when(roomRepository.findById(roomNumber)).thenReturn(Optional.empty());

        // Act
        Room result = roomService.updateRoomById(roomNumber, roomNew);

        // Assert
        assertNull(result);
        verify(roomRepository, times(1)).findById(roomNumber);
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void testDeleteRoom_RoomExists() {
        // Arrange
        Integer roomNumber = 101;
        when(roomRepository.existsById(roomNumber)).thenReturn(true);

        // Act
        roomService.deleteRoom(roomNumber);

        // Assert
        verify(roomRepository, times(1)).existsById(roomNumber);
        verify(roomRepository, times(1)).deleteById(roomNumber);
    }

    @Test
    void testDeleteRoom_RoomDoesNotExist() {
        // Arrange
        Integer roomNumber = 101;
        when(roomRepository.existsById(roomNumber)).thenReturn(false);

        // Act
        roomService.deleteRoom(roomNumber);

        // Assert
        verify(roomRepository, times(1)).existsById(roomNumber);
        verify(roomRepository, never()).deleteById(roomNumber);
    }
}
