package ru.innopolis.java.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "Order id", example = "1")
    private Long id;

    @Schema(name = "Customer's id", example = "1")
    private Long customerId;

    @Schema(name = "Pizza ids", example = "1, 2, 3")
    private Set<Long> pizzaIds;

    @Schema(name = "Price of Order", example = "1200")
    private int totalPrice;

    @Schema(name = "Order confirmation date and time")
    private LocalDateTime orderDate;

    @Schema(name = "Order status", example = "Waiting")
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
