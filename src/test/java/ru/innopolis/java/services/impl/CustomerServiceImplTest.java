package ru.innopolis.java.services.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import ru.innopolis.java.dto.customer.CustomerDto;
import ru.innopolis.java.dto.customer.CustomerDtoShort;
import ru.innopolis.java.models.Customer;
import ru.innopolis.java.repositories.CustomerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("Customer Service работает, когда")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customerFirst;
    private Customer customerSecond;

    @BeforeEach
    void setUp() {
        customerFirst = new Customer();
        customerFirst.setId(12L);
        customerFirst.setFullName("Ivan Ivanov");

        customerSecond = new Customer();
        customerSecond.setId(13L);
        customerSecond.setFullName("Ivan Ivanov");
    }

    @AfterEach
    void tearDown() {
        customerFirst = null;
        customerSecond = null;
    }

    @Test
    @Order(3)
    @DisplayName("получается список всех существующих покупателей - R")
    void get_all_customers() {
        when(customerRepository.findAllNotDeleted()).thenReturn(Arrays.asList(customerFirst, customerSecond));

        List<CustomerDtoShort> result = customerService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(customerFirst.getId(), result.get(0).getId());
        assertEquals(customerSecond.getId(), result.get(1).getId());

        verify(customerRepository, times(2)).findAllNotDeleted();
    }

    @Test
    @Order(2)
    @DisplayName("находится покупатель по заданному id - R")
    void get_customer_by_id() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customerFirst));

        CustomerDtoShort result = customerService.getCustomerById(12L);

        assertEquals(customerFirst.getId(), result.getId());
        assertEquals(customerFirst.getFullName(), result.getFullName());

        verify(customerRepository, times(1)).findById(12L);
    }

    @Test
    @Order(5)
    @DisplayName("удаляется покупатель по заданному id - D")
    void soft_delete_customer_by_id() {
        when(customerRepository.findById(customerFirst.getId())).thenReturn(Optional.of(customerFirst));

        customerService.softDeleteById(customerFirst.getId());

        assertTrue(customerFirst.isDeleted());

        verify(customerRepository, times(1)).save(customerFirst);
    }

    @Test
    @Order(1)
    @DisplayName("создается запись о новом покупателе - C")
    void add_new_customer() {
        CustomerDto customerDto = CustomerDto.builder()
                .fullName("Andrew")
                .email("email@mail.net")
                .phone("+13433")
                .address("99, Street")
                .build();

        Customer savedCustomer = Customer.builder()
                .fullName("Andrew")
                .email("email@mail.net")
                .phoneNumber("+13433")
                .address("99, Street")
                .build();


        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerDto result = customerService.addCustomer(customerDto);

        assertEquals(savedCustomer.getFullName(), result.getFullName());
        assertEquals(savedCustomer.getEmail(), result.getEmail());
        assertEquals(savedCustomer.getPhoneNumber(), result.getPhone());
        assertEquals(savedCustomer.getAddress(), result.getAddress());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @Order(4)
    @DisplayName("обновляется существующий покупатель по id - U")
    void update_existing_customer_by_id() {
        Customer existingCustomer = customerFirst;

        CustomerDto updatedCustomerDto = CustomerDto.builder()
                .fullName("Andrew")
                .email("ssodvds@ssjdnv.rer")
                .phone("+13433")
                .address("99, Street")
                .build();

        Customer updatedCustomer = Customer.builder()
                .fullName("Andrew")
                .email("ssodvds@ssjdnv.rer")
                .phoneNumber("+13433")
                .address("99, Street")
                .build();

        when(customerRepository.findById(existingCustomer.getId())).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        CustomerDto result = customerService.updateCustomer(existingCustomer.getId(), updatedCustomerDto);

        assertNotNull(result);
        assertEquals(updatedCustomerDto.getFullName(), result.getFullName());
        assertEquals(updatedCustomerDto.getEmail(), result.getEmail());
        assertEquals(updatedCustomerDto.getPhone(), result.getPhone());
        assertEquals(updatedCustomerDto.getAddress(), result.getAddress());

        verify(customerRepository, times(1)).findById(existingCustomer.getId());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
}