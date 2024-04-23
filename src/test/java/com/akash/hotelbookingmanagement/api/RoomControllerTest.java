package com.akash.hotelbookingmanagement.api;

import com.akash.hotelbookingmanagement.dto.RoomDto;
import com.akash.hotelbookingmanagement.model.Customer;
import com.akash.hotelbookingmanagement.model.Room;
import com.akash.hotelbookingmanagement.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private Room testRoom;
    private RoomDto testRoomDto;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test customer
        testCustomer = new Customer("Akash", "Gemini", 30, "1234567890");
        testCustomer.setCustomerId(1);

        // Initialize test room
        testRoom = Room.builder()
                .roomNumber(101)
                .type("Standard")
                .occupancy(2)
                .pricePerDay(100)
                .availability(true)
                .isCheckedIn(false)
                .isCheckedOut(false)
                .checkedInCustomers(Arrays.asList(testCustomer))
                .build();

        // Initialize test RoomDto
        testRoomDto = new RoomDto();
        testRoomDto.setRoomNumber(101);
        testRoomDto.setType("Standard");
        testRoomDto.setOccupancy(2);
        testRoomDto.setPricePerDay(100);
        testRoomDto.setAvailability(true);
        testRoomDto.setIsCheckedIn(false);
        testRoomDto.setIsCheckedOut(false);
        testRoomDto.setCheckedInCustomerIdList(Collections.singletonList(testCustomer.getCustomerId()));
    }

    @Test
    @DisplayName("Should create a room")
    void testCreateRoom() {
        when(roomService.createRoom(any(Room.class))).thenReturn(testRoom);

        ResponseEntity<Room> responseEntity = roomController.createRoom(testRoom);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(testRoom, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should get all rooms")
    void testGetAllRooms() {
        List<Room> roomList = Arrays.asList(testRoom, new Room());

        when(roomService.getAllRooms()).thenReturn(roomList);

        ResponseEntity<Iterable<Room>> responseEntity = roomController.getAllRooms();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(roomList, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should get room by room number")
    void testGetRoomByRoomNumber() {
        when(roomService.getRoomByRoomNumber(anyInt())).thenReturn(testRoom);

        ResponseEntity<Room> responseEntity = roomController.getRoomByRoomNumber(101);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(testRoom, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should get rooms by type")
    void testGetRoomsByType() {
        List<Room> roomList = Arrays.asList(testRoom, new Room());

        when(roomService.getRoomsByType(anyString())).thenReturn(roomList);

        ResponseEntity<Iterable<Room>> responseEntity = roomController.getRoomsByType("Standard");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(roomList, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should get checked-in customers for a room")
    void testGetCheckedInCustomers() {
        List<Customer> customerList = Collections.singletonList(testCustomer);

        when(roomService.getCheckedInCustomers(anyInt())).thenReturn(customerList);

        ResponseEntity<Iterable<Customer>> responseEntity = roomController.getCheckedInCustomers(101);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(customerList, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should update room by room number")
    void testUpdateRoomById() {
        when(roomService.updateRoomById(anyInt(), any())).thenReturn(testRoom);

        ResponseEntity<Room> responseEntity = roomController.updateRoomById(101, testRoomDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(testRoom, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should delete room by room number")
    void testDeleteRoom() {
        ResponseEntity<Void> responseEntity = roomController.deleteRoom(101);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(roomService, times(1)).deleteRoom(anyInt());
    }
}
