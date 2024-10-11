package ru.innopolis.java.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.innopolis.java.dto.customer.CustomerDto;
import ru.innopolis.java.dto.customer.CustomerDtoShort;
import ru.innopolis.java.services.impl.CustomerServiceImpl;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerServiceImpl customerServiceImpl;

    CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        customerDto = CustomerDto.builder()
                .fullName("Andrew")
                .email("andrew@gmail.com")
                .phone("+12345")
                .address("9, Somestreet")
                .build();
    }

    @AfterEach
    void tearDown() {
        customerDto = null;
    }

    @Test
    void createCustomer() throws Exception {
        when(customerServiceImpl.addCustomer(any(CustomerDto.class))).thenReturn(customerDto);

        String customerJson = "{ \"fullName\": \"Andrew\", \"email\": \"andrew@gmail.com\", \"phone\": \"+12345\", \"address\": \"9, Somestreet\" }";

        mockMvc.perform(post("/customers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Andrew"))
                .andExpect(jsonPath("$.email").value("andrew@gmail.com"))
                .andExpect(jsonPath("$.phone").value("+12345"))
                .andExpect(jsonPath("$.address").value("9, Somestreet"));

        verify(customerServiceImpl, times(1)).addCustomer(any(CustomerDto.class));
    }

    @Test
    void getAllCustomers() throws Exception {
        CustomerDtoShort customerOne = new CustomerDtoShort(1L, "Andrew Perepel");
        CustomerDtoShort customerTwo = new CustomerDtoShort(2L, "Andrew Somesurname");

        List<CustomerDtoShort> customerDtoShortList = List.of(customerOne, customerTwo);

        when(customerServiceImpl.getAll()).thenReturn(customerDtoShortList);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].fullName").value("Andrew Perepel"))
                .andExpect(jsonPath("$[1].fullName").value("Andrew Somesurname"));

        verify(customerServiceImpl, times(1)).getAll();
    }

    @Test
    void get_customer_by_id() throws Exception {
        CustomerDtoShort customerDtoShort = new CustomerDtoShort(1L, "Andrew Perepel");

        when(customerServiceImpl.getCustomerById(1L)).thenReturn(customerDtoShort);

        mockMvc.perform(get("/customers/{id}", customerDtoShort.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Andrew Perepel"));

        verify(customerServiceImpl, times(1)).getCustomerById(1L);
    }

    @Test
    void updateCustomer() throws Exception {
        Long id = 1L;

        CustomerDto updatedCustomerDto = CustomerDto.builder()
                .fullName("Andrew Some")
                .phone("+12345")
                .email("mail@mail.ru")
                .address("9, Street")
                .build();

        when(customerServiceImpl.updateCustomer(eq(id), any(CustomerDto.class))).thenReturn(updatedCustomerDto);

        mockMvc.perform(put("/customers/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"fullName\": \"Andrew Some\", \"email\": \"mail@mail.ru\", \"phone\": \"+12345\", \"address\": \"9, Street\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Andrew Some"))
                .andExpect(jsonPath("$.phone").value("+12345"))
                .andExpect(jsonPath("$.email").value("mail@mail.ru"))
                .andExpect(jsonPath("$.address").value("9, Street"));

        verify(customerServiceImpl, times(1)).updateCustomer(eq(id), any(CustomerDto.class));
    }

    @Test
    void deleteCustomer() throws Exception {
        Long id = 1L;

        doNothing().when(customerServiceImpl).softDeleteById(id);

        mockMvc.perform(delete("/customers/delete/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerServiceImpl, times(1)).softDeleteById(id);
    }
}























