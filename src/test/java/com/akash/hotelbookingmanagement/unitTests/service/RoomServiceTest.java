package com.akash.hotelbookingmanagement.unitTests.service;

import com.akash.hotelbookingmanagement.config.RoomMapper;
import com.akash.hotelbookingmanagement.dto.RoomDto;
import com.akash.hotelbookingmanagement.exception.ResourceNotFoundException;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.repository.RoomRepository;
import com.akash.hotelbookingmanagement.service.CustomerService;
import com.akash.hotelbookingmanagement.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomService roomService;

    private Room testRoom;

    private RoomDto testRoomDto;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup a test customer
        testCustomer = new Customer(1, "Akash", "Delhi", 30, "1234567890");

        // Set up a test room with the test customer
        List<Customer> checkedInCustomers = new ArrayList<>();
        checkedInCustomers.add(testCustomer);

        testRoom = Room.builder()
                .roomNumber(101)
                .type("Standard")
                .occupancy(2)
                .pricePerDay(100)
                .availability(true)
                .isCheckedIn(false)
                .isCheckedOut(true)
                .checkedInCustomers(checkedInCustomers)
                .build();

        // Initialize test RoomDto
        testRoomDto = new RoomDto();
        testRoomDto.setType("Standard");
        testRoomDto.setOccupancy(2);
        testRoomDto.setPricePerDay(100);
        testRoomDto.setCheckedInCustomerIdList(Collections.singletonList(testCustomer.getCustomerId()));


    }

    @Test
    void testCreateRoom() {
        // Arrange
        when(roomRepository.save(any(Room.class))).thenReturn(testRoom);
        when(roomMapper.convertToEntity(any(RoomDto.class))).thenReturn(testRoom);

        // Act
        Room createdRoom = roomService.createRoom(testRoomDto);

        // Assert
        assertNotNull(createdRoom);
        assertEquals(testRoom, createdRoom);

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void testGetAllRooms() {
        // Arrange
        List<Room> roomList = new ArrayList<>();
        roomList.add(testRoom);
        when(roomRepository.findAll()).thenReturn(roomList);

        // Act
        Iterable<Room> rooms = roomService.getAllRooms();

        // Assert
        assertNotNull(rooms);
        assertTrue(rooms.iterator().hasNext());
        assertEquals(testRoom, rooms.iterator().next());

        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void testGetRoomByRoomNumber() {
        // Arrange
        when(roomRepository.findById(101)).thenReturn(Optional.of(testRoom));

        // Act
        Room foundRoom = roomService.getRoomByRoomNumber(101);

        // Assert
        assertNotNull(foundRoom);
        assertEquals(testRoom, foundRoom);

        verify(roomRepository, times(1)).findById(101);
    }

    @Test
    void testGetRoomByRoomNumber_NotFound() {
        // Arrange
        when(roomRepository.findById(101)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> roomService.getRoomByRoomNumber(101));

        verify(roomRepository, times(1)).findById(101);
    }

    @Test
    void testUpdateRoomById() {
        // Arrange
        RoomDto updatedRoomDto = new RoomDto();
        updatedRoomDto.setType("Deluxe");

        Room updatedRoom = Room.builder()
                .roomNumber(101)
                .type("Deluxe")
                .build();

        when(roomRepository.findById(101)).thenReturn(Optional.of(testRoom));
        when(roomMapper.convertToEntity(updatedRoomDto)).thenReturn(updatedRoom);
        when(roomRepository.save(testRoom)).thenReturn(updatedRoom);

        // Act
        Room result = roomService.updateRoomById(101, updatedRoomDto);

        // Assert
        assertNotNull(result);
        assertEquals(updatedRoom, result);

        verify(roomRepository, times(1)).findById(101);
        verify(roomMapper, times(1)).convertToEntity(updatedRoomDto);
        verify(roomRepository, times(1)).save(testRoom);
    }

    @Test
    void testDeleteRoom() {
        // Arrange
        when(roomRepository.existsById(101)).thenReturn(true);

        // Act
        roomService.deleteRoom(101);

        // Assert
        verify(roomRepository, times(1)).existsById(101);
        verify(roomRepository, times(1)).deleteById(101);
    }

    @Test
    void testDeleteRoom_NotFound() {
        // Arrange
        when(roomRepository.existsById(101)).thenReturn(false);

        // Act
        roomService.deleteRoom(101);

        // Assert
        verify(roomRepository, times(1)).existsById(101);
        verify(roomRepository, never()).deleteById(101);
    }

    @Test
    void testCheckRoomsAvailability_AllAvailable() {
        // Arrange
        List<Room> roomList = new ArrayList<>();
        roomList.add(testRoom);
        when(roomRepository.findById(testRoom.getRoomNumber())).thenReturn(Optional.of(testRoom));

        // Act
        boolean result = roomService.checkRoomsAvailability(roomList);

        // Assert
        assertTrue(result);
    }

    @Test
    void testCheckRoomsAvailability_NotAvailable() {
        // Arrange
        testRoom.setAvailability(false);
        List<Room> roomList = new ArrayList<>();
        roomList.add(testRoom);

        when(roomRepository.findById(testRoom.getRoomNumber())).thenReturn(Optional.of(testRoom));


        // Act
        boolean result = roomService.checkRoomsAvailability(roomList);

        // Assert
        assertFalse(result);
    }
}
