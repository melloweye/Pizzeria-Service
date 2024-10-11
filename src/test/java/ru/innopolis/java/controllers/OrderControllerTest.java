package ru.innopolis.java.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.innopolis.java.dto.order.OrderDto;
import ru.innopolis.java.dto.order.OrderDtoShort;
import ru.innopolis.java.services.impl.OrderServiceImpl;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceImpl orderServiceImpl;

    OrderDto orderDto;
    OrderDtoShort orderDtoShort;

    @BeforeEach
    void setUp() {
        orderDto = OrderDto.builder()
                .id(1L)
                .customerId(1L)
                .pizzaIds(Set.of(1L, 2L))
                .totalPrice(1500)
                .status("Waiting")
                .build();

        orderDtoShort = OrderDtoShort.builder()
                .customerId(1L)
                .pizzaIds(Set.of(1L, 2L))
                .totalPrice(1500)
                .status("Waiting")
                .build();
    }

    @AfterEach
    void tearDown() {
        orderDto = null;
        orderDtoShort = null;
    }

    @Test
    void addOrder() throws Exception {
        String orderJson = "{ \"customerId\": 1, \"pizzaIds\": [1, 2], \"orderDate\": \"2023-10-11T14:30:00\", \"totalPrice\": 1500, \"status\": \"Waiting\" }";

        when(orderServiceImpl.addOrder(any(OrderDtoShort.class))).thenReturn(orderDtoShort);

        mockMvc.perform(post("/orders/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated())  // Ожидаем статус 201 Created
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.pizzaIds.length()").value(2))
                .andExpect(jsonPath("$.totalPrice").value(1500))
                .andExpect(jsonPath("$.status").value("Waiting"));

        verify(orderServiceImpl, times(1)).addOrder(any(OrderDtoShort.class));
    }

    @Test
    void getAllOrders() throws Exception {

        List<OrderDto> orders = List.of(orderDto);

        when(orderServiceImpl.getAll()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerId").value(1L))
                .andExpect(jsonPath("$[0].pizzaIds.length()").value(2))
                .andExpect(jsonPath("$[0].totalPrice").value(1500));

        verify(orderServiceImpl, times(1)).getAll();
    }

    @Test
    void getOrderById() throws Exception {

        when(orderServiceImpl.getOrderById(anyLong())).thenReturn(orderDto);

        mockMvc.perform(get("/orders/{id}", orderDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.pizzaIds.length()").value(2));

        verify(orderServiceImpl, times(1)).getOrderById(anyLong());
    }

    @Test
    void updateOrder() throws Exception {

        Long orderId = 1L;

        when(orderServiceImpl.updateOrder(eq(orderId), any(OrderDtoShort.class))).thenReturn(orderDtoShort);

        mockMvc.perform(put("/orders/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"customerId\": 1, \"pizzaIds\": [1, 2], \"orderDate\": \"2023-10-11T14:30:00\", \"totalPrice\": 1500, \"status\": \"Waiting\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.pizzaIds.length()").value(2))
                .andExpect(jsonPath("$.totalPrice").value(1500));

        verify(orderServiceImpl, times(1)).updateOrder(eq(orderId), any(OrderDtoShort.class));
    }

    @Test
    void deleteOrder() throws Exception {

        Long orderId = 1L;

        doNothing().when(orderServiceImpl).softDeleteById(orderId);

        mockMvc.perform(delete("/orders/delete/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(orderServiceImpl, times(1)).softDeleteById(orderId);
    }
}


















