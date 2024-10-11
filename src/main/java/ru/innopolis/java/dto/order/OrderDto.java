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
public class OrderDto {

    private Long id;

    private Long customerId;

    private Set<Long> pizzaIds;

    private int totalPrice;

    private LocalDateTime orderDate;

    private String status;


    public static OrderDto from(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .pizzaIds(order.getPizzas().stream()
                        .map(Pizza::getId)
                        .collect(Collectors.toSet()))
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .build();
    }

    public static List<OrderDto> from(List<Order> orders) {
        return orders
                .stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }
}
