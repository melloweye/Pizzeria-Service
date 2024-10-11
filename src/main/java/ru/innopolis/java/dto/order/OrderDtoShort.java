package ru.innopolis.java.dto.order;

import lombok.*;
import ru.innopolis.java.models.Order;
import ru.innopolis.java.models.Pizza;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDtoShort {

    private Long customerId;

    private Set<Long> pizzaIds;

    private int totalPrice;

    private LocalDateTime orderDate;

    private String status;

    public static OrderDtoShort from(Order order) {
        return OrderDtoShort.builder()
                .customerId(order.getCustomer().getId())
                .pizzaIds(order.getPizzas().stream()
                        .map(Pizza::getId)
                        .collect(Collectors.toSet()))
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .build();
    }

    public static List<OrderDtoShort> from(List<Order> orders) {
        return orders
                .stream()
                .map(OrderDtoShort::from)
                .collect(Collectors.toList());
    }
}
