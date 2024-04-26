package com.akash.hotelbookingmanagement.integrationTests;

import com.akash.hotelbookingmanagement.api.CustomerController;
import com.akash.hotelbookingmanagement.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.function.Suppliers;
import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CustomerController customerController;

    static MySQLContainer mySQLContainer=new MySQLContainer("mysql:latest");

    @DynamicPropertySource
    static void configProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username",mySQLContainer::getUsername);
        registry.add("spring.datasource.password",mySQLContainer::getPassword);
//        registry.add("spring.jpa.hibernate.ddl-auto",()->"update" );
//        registry.add("spring.sql.init.mode",()->"always" );
    }

    @BeforeAll
    static void startContainer(){
        mySQLContainer.start();
    }
    @AfterAll
    static void closeContainer(){
        mySQLContainer.stop();
    }
    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void createCustomerTest() throws Exception {
        Customer customer = Customer.builder()
                .fullName("akash")
                .age(12)
                .address("gemini")
                .contactNumber("1234567898")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/customer/")
                        .content(mapper.writeValueAsString(customer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
}
