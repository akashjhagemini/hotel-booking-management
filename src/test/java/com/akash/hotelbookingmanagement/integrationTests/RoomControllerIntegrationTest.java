package com.akash.hotelbookingmanagement.integrationTests;

import com.akash.hotelbookingmanagement.api.CustomerController;
import com.akash.hotelbookingmanagement.api.RoomController;
import com.akash.hotelbookingmanagement.dto.RoomDto;
import com.akash.hotelbookingmanagement.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.MediaType;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@Testcontainers
//@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoomControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MockMvc mockMvc2;

    @Autowired
    private RoomController roomController;
    @Autowired
    private CustomerController customerController;

//    @Container
//    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");
//
//    @DynamicPropertySource
//    static void configProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", mySQLContainer::getUsername);
//        registry.add("spring.datasource.password", mySQLContainer::getPassword);
//    }

    @BeforeAll
    static void startContainer() {
//        mySQLContainer.start();
    }

    @AfterAll
    static void closeContainer() {
//        mySQLContainer.stop();
    }

    //order annotation
    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
//        this.mockMvc2 = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void createRoomTest() throws Exception {
        // Define the request body
        Customer customer = Customer.builder()
                .fullName("akash-test")
                .age(16)
                .address("gemini")
                .contactNumber("1234567898")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mockMvc2.perform(MockMvcRequestBuilders
                        .post("/customer/")
                        .content(mapper.writeValueAsString(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        RoomDto roomDto = new RoomDto();
        roomDto.setType("Standard");
        roomDto.setOccupancy(2);
        roomDto.setPricePerDay(300);
        roomDto.setAvailability(true);
        roomDto.setCheckedInCustomerIdList(List.of(customer.getCustomerId()));

        // Perform the POST request to create a room
        mockMvc.perform(MockMvcRequestBuilders.post("/rooms/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(roomDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(Matchers.greaterThan(0))));
    }



}
