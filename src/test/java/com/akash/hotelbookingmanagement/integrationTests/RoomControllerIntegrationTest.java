package com.akash.hotelbookingmanagement.integrationTests;

import com.akash.hotelbookingmanagement.api.RoomController;
import com.akash.hotelbookingmanagement.dto.RoomDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoomController roomController;

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");

    @DynamicPropertySource
    static void configProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeAll
    static void startContainer() {
        mySQLContainer.start();
    }

    @AfterAll
    static void closeContainer() {
        mySQLContainer.stop();
    }

    //order annotation
    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    @Order(1)
    public void createRoomTest() throws Exception {

        RoomDto roomDto = new RoomDto();
        roomDto.setType("single");
        roomDto.setOccupancy(2);
        roomDto.setPricePerDay(300);
        roomDto.setCheckedInCustomerIdList(List.of(1,2));

        ObjectMapper mapper=new ObjectMapper();
        // Perform the POST request to create a room
        mockMvc.perform(MockMvcRequestBuilders.post("/rooms/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(roomDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type",Matchers.equalTo(roomDto.getType())))
                .andExpect(jsonPath("$.occupancy",Matchers.equalTo(roomDto.getOccupancy())))
                .andExpect(jsonPath("$.checkedInCustomers",Matchers.hasSize(2)));
    }

    @Test
    @Order(2)
    public void getAllRoomsTest() throws Exception {
        // Perform the GET request to fetch all rooms
        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(5)));
    }

    @Test
    @Order(3)
    public void getRoomByRoomNumberTest() throws Exception {
        // Perform the GET request to fetch room by room number
        // For this we already have saved sample data in database using flyway migration script
        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomNumber}", 1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.roomNumber", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.type", Matchers.equalTo("single")))
                .andExpect(jsonPath("$.occupancy", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.pricePerDay", Matchers.equalTo(100)));
    }

    @Test
    @Order(4)
    public void getRoomsByTypeTest() throws Exception {
        // Perform the GET request to fetch rooms by type
        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/type/{type}", "single")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].type", Matchers.equalTo("single")))
                .andExpect(jsonPath("$[0].occupancy", Matchers.equalTo(1)))
                .andExpect(jsonPath("$[0].pricePerDay", Matchers.equalTo(100)));
    }

    @Test
    @Order(5)
    public void getCheckedInCustomersTest() throws Exception {
        // Perform the GET request to fetch checked-in customers for the room
        mockMvc.perform(MockMvcRequestBuilders.get("/rooms/{roomNumber}/customers", 5)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].fullName", Matchers.equalTo("akash")))
                .andExpect(jsonPath("$[1].fullName", Matchers.equalTo("aman")));
    }

    @Test
    @Order(6)
    public void updateRoomByIdTest() throws Exception {

        RoomDto updatedRoomDto = new RoomDto();
        updatedRoomDto.setType("deluxe");
        updatedRoomDto.setOccupancy(4);
        updatedRoomDto.setPricePerDay(500);
        updatedRoomDto.setCheckedInCustomerIdList(List.of(1,2,3,4));
        ObjectMapper mapper = new ObjectMapper();
        // Perform the PUT request to update the room
        mockMvc.perform(MockMvcRequestBuilders.put("/rooms/{roomNumber}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedRoomDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type", Matchers.equalTo("deluxe")))
                .andExpect(jsonPath("$.occupancy", Matchers.equalTo(4)))
                .andExpect(jsonPath("$.pricePerDay", Matchers.equalTo(500)))
                .andExpect(jsonPath("$.checkedInCustomers", Matchers.hasSize(4)));
    }

    @Test
    @Order(7)
    public void deleteRoomTest() throws Exception {
        // Perform the DELETE request to delete the room
        mockMvc.perform(MockMvcRequestBuilders.delete("/rooms/{roomNumber}", 5)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());

    }


}
