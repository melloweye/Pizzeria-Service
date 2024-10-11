package ru.innopolis.java.services;

import ru.innopolis.java.dto.order.OrderDto;
import ru.innopolis.java.dto.order.OrderDtoShort;

import java.util.List;

public interface OrderService {

    // READ
    List<OrderDto> getAll();

    // READ
    OrderDto getOrderById(Long id);

    // DELETE
    void softDeleteById(Long id);

    // CREATE
    OrderDtoShort addOrder(OrderDtoShort orderDtoShort);

    // UPDATE
    OrderDtoShort updateOrder(Long id, OrderDtoShort newData);
}
