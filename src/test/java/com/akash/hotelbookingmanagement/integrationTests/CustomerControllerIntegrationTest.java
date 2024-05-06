package com.akash.hotelbookingmanagement.integrationTests;

import com.akash.hotelbookingmanagement.api.CustomerController;
import com.akash.hotelbookingmanagement.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CustomerController customerController;

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
        this.mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    @Transactional
    public void createCustomerTest() throws Exception {
        Customer customer = Customer.builder()
                .fullName("akash-test")
                .age(16)
                .address("gemini")
                .contactNumber("1234567898")
                .build();
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/customer/")
                        .content(mapper.writeValueAsString(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

    }

    @Test
    public void getAllCustomersTest() throws Exception {

        //we are initializing data using migration script file via flyway, so here we check whether we receive 4 customers or not
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/customer/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)));

    }

    @Test
    @Transactional
    public void getCustomerByIdTest() throws Exception {
        Customer customer = Customer.builder()
                .fullName("akash-test")
                .age(16)
                .address("gemini")
                .contactNumber("1234567898")
                .build();
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/customer/")
                        .content(mapper.writeValueAsString(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String savedCustomerAsString = result.getResponse().getContentAsString();

        Customer savedCustomer = mapper.readValue(savedCustomerAsString, Customer.class);

        // Perform the request to fetch a customer by ID
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/customer/{id}", savedCustomer.getCustomerId())) // Replace 1 with the ID of the customer you want to fetch
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fullName", equalTo(customer.getFullName()))) // Replace with the expected customer name
                .andExpect(jsonPath("$.age", equalTo(customer.getAge()))) // Replace with the expected age
                .andExpect(jsonPath("$.address", equalTo(customer.getAddress())))
                .andExpect(jsonPath("$.contactNumber", equalTo(customer.getContactNumber()))); // Replace with the expected address
    }

    @Test
    @Transactional
    public void updateCustomerTest() throws Exception {
        Customer customer = Customer.builder()
                .fullName("akash-test")
                .age(16)
                .address("gemini")
                .contactNumber("1234567898")
                .build();
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/customer/")
                        .content(mapper.writeValueAsString(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String savedCustomerAsString = result.getResponse().getContentAsString();

        Customer savedCustomer = mapper.readValue(savedCustomerAsString, Customer.class);

        Customer updatedCustomer = Customer.builder()
                .fullName("akash")
                .age(12)
                .build();

        // Perform the PUT request to update a customer by ID
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/customer/{id}", savedCustomer.getCustomerId()) // Replace 1 with the ID of the customer you want to update
                        .content(mapper.writeValueAsString(updatedCustomer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fullName", equalTo("akash")))
                .andExpect(jsonPath("$.age", equalTo(12)))
                .andExpect(jsonPath("$.address", equalTo(customer.getAddress())))
                .andExpect(jsonPath("$.contactNumber", equalTo(customer.getContactNumber())));

    }

    @Test
    public void deleteCustomerTest() throws Exception {
        Customer customer = Customer.builder()
                .fullName("akash-test")
                .age(16)
                .address("gemini")
                .contactNumber("1234567898")
                .build();
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/customer/")
                        .content(mapper.writeValueAsString(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String savedCustomerAsString = result.getResponse().getContentAsString();

        Customer savedCustomer = mapper.readValue(savedCustomerAsString, Customer.class);
        // Perform the DELETE request to delete a customer by ID
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/customer/{id}", savedCustomer.getCustomerId())) // Replace 1 with the ID of the customer you want to delete
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }
}
