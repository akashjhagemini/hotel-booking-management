package com.akash.hotelbookingmanagement.integrationTests;

import com.akash.hotelbookingmanagement.api.BookingDetailsController;
import com.akash.hotelbookingmanagement.dto.BookingDetailsDto;
import com.akash.hotelbookingmanagement.model.enums.ModeOfBooking;
import com.akash.hotelbookingmanagement.model.enums.ModeOfPayment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
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

import java.time.LocalDate;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookingDetailsController bookingDetailsController;

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

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookingDetailsController).build();
    }

    @Test
    @Order(1)
    public void createBookingDetailsTest() throws Exception {
        BookingDetailsDto bookingDetailsData = BookingDetailsDto.builder()
                .duration(2)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .modeOfBooking(ModeOfBooking.online)
                .modeOfPayment(ModeOfPayment.prepaid)
                .paidAmount(100)
                .roomNumberList(List.of(1,2))
                .customerIdList(List.of(1,2))
                .build();

        // Including time module explicitly to avoid error while mapping date properties to string
        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mockMvc.perform(MockMvcRequestBuilders.post("/booking/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDetailsData)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomList",Matchers.hasSize(2)))
                .andExpect(jsonPath("$.customerList",Matchers.hasSize(2)));
    }

    @Test
    @Order(2)
    public void getAllBookingDetailsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/booking/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",Matchers.hasSize(1)));
    }

    @Test
    @Order(3)
    public void getBookingDetailsByIdTest() throws Exception {
        // Perform the GET request to fetch booking details by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/booking/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.duration").value(2))
                .andExpect(jsonPath("$.roomList",Matchers.hasSize(2)))
                .andExpect(jsonPath("$.customerList",Matchers.hasSize(2)));
    }

    @Test
    @Order(4)
    public void updateBookingDetailsTest() throws Exception {
        int id = 1;

        BookingDetailsDto updatedBookingData = BookingDetailsDto.builder()
                .duration(3)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(4))
                .modeOfBooking(ModeOfBooking.online)
                .modeOfPayment(ModeOfPayment.prepaid)
                .paidAmount(200)
                .roomNumberList(List.of(3))
                .customerIdList(List.of(3))
                .build();

        ObjectMapper mapper=new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Perform the PUT request to update booking details
        mockMvc.perform(MockMvcRequestBuilders.put("/booking/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedBookingData)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.duration").value(3))
                .andExpect(jsonPath("$.paidAmount").value(200))
                .andExpect(jsonPath("$.customerList",Matchers.hasSize(1)))
                .andExpect(jsonPath("$.roomList",Matchers.hasSize(1)));
    }

    @Test
    public void deleteBookingDetailsTest() throws Exception {
        int id = 1;

        // Perform the DELETE request to delete the booking by ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/booking/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }
}
