package ru.innopolis.java.dto.pizza;

import lombok.*;
import ru.innopolis.java.models.Pizza;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PizzaDtoShort {

    private String name;

    private String description;

    private int price;

    private String size;

    public static PizzaDtoShort from(Pizza pizza) {
        return PizzaDtoShort.builder()
                .name(pizza.getName())
                .description(pizza.getDescription())
                .price(pizza.getPrice())
                .size(pizza.getSize())
                .build();
    }

    public static List<PizzaDtoShort> from(List<Pizza> pizzas) {
        return pizzas
                .stream()
                .map(PizzaDtoShort::from)
                .collect(Collectors.toList());
    }
}
