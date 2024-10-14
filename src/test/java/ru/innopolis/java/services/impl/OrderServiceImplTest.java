package ru.innopolis.java.services.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.innopolis.java.dto.order.OrderDto;
import ru.innopolis.java.dto.order.OrderDtoShort;
import ru.innopolis.java.exceptions.CustomerNotFoundException;
import ru.innopolis.java.models.Customer;
import ru.innopolis.java.models.Pizza;
import ru.innopolis.java.repositories.CustomerRepository;
import ru.innopolis.java.repositories.OrderRepository;
import ru.innopolis.java.repositories.PizzaRepository;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Order Service работает, когда")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PizzaRepository orderPizzaRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Customer customer;
    private Pizza pizza;
    private ru.innopolis.java.models.Order order;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .fullName("John Doe")
                .build();


        pizza = Pizza.builder()
                .id(1L)
                .name("Some Pizza")
                .build();

        order = ru.innopolis.java.models.Order.builder()
                .id(1L)
                .customer(customer)
                .pizzas(Set.of(pizza))
                .build();
    }

    @AfterEach
    void tearDown() {
        customer = null;
        pizza = null;
        order = null;
    }

    @Test
    @Order(3)
    @DisplayName("получается список всех существующих заказов - R")
    void get_all_orders() {
        when(orderRepository.findAllNotDeleted()).thenReturn(List.of(order));

        List<OrderDto> orders = orderService.getAll();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(order.getId(), orders.get(0).getId());

        verify(orderRepository, times(2)).findAllNotDeleted();
    }

    @Test
    @Order(2)
    @DisplayName("находится заказ по заданному id - R")
    void get_order_by_id() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        OrderDto orderDto = orderService.getOrderById(1L);

        assertNotNull(orderDto);
        assertEquals(order.getId(), orderDto.getId());
    }

    @Test
    @Order(5)
    @DisplayName("удаляется заказ по заданному id - D")
    void soft_delete_order_by_id() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        orderService.softDeleteById(order.getId());

        assertTrue(order.isDeleted());

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @Order(1)
    @DisplayName("создается запись о новом заказе - C")
    void add_new_order() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderPizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));

        OrderDtoShort orderDtoShort = OrderDtoShort.builder()
                .customerId(1L)
                .pizzaIds(Set.of(1L))
                .build();

        ru.innopolis.java.models.Order savedOrder = new ru.innopolis.java.models.Order();
        savedOrder.setId(1L);
        savedOrder.setCustomer(customer);
        savedOrder.setPizzas(Set.of(pizza));

        when(orderRepository.save(any(ru.innopolis.java.models.Order.class))).thenReturn(savedOrder);

        OrderDtoShort result = orderService.addOrder(orderDtoShort);

        assertNotNull(result);
        assertEquals(1L, result.getCustomerId());
        assertTrue(result.getPizzaIds().contains(1L));
    }

    @Test
    @Order(2)
    @DisplayName("выбрасывается исключение, если указан id несуществующего покупателя")
    void add_new_order_when_customer_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        OrderDtoShort orderDtoShort = OrderDtoShort.builder()
                .customerId(1L)
                .pizzaIds(Set.of(1L))
                .build();

        assertThrows(CustomerNotFoundException.class, () -> orderService.addOrder(orderDtoShort));

        verify(orderRepository, never()).save(any(ru.innopolis.java.models.Order.class));
    }

    @Test
    @Disabled
    @Order(4)
    @DisplayName("обновляется существующий заказ по id - U")
    void update_existing_order_by_id() {
        ru.innopolis.java.models.Order existingOrder = ru.innopolis.java.models.Order.builder()
                .id(1L)
                .customer(customer)
                .pizzas(Set.of(pizza))
                .build();

        OrderDtoShort updatedOrderDtoShort = OrderDtoShort.builder()
                .customerId(1L)
                .pizzaIds(Set.of(1L))
                .build();

        ru.innopolis.java.models.Order updatedOrder = ru.innopolis.java.models.Order.builder()
                .id(1L)
                .customer(customer)
                .pizzas(Set.of(pizza))
                .build();

        when(orderRepository.findById(existingOrder.getId())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(ru.innopolis.java.models.Order.class))).thenReturn(updatedOrder);

        OrderDtoShort result = orderService.updateOrder(existingOrder.getId(), updatedOrderDtoShort);

        assertNotNull(result);

        verify(orderRepository, times(1)).save(any(ru.innopolis.java.models.Order.class));
    }
}