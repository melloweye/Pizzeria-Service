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
public class PizzaDto {

    private Long id;

    private String name;

    private String description;

    private int price;

    private String size;

    public static PizzaDto from(Pizza pizza) {
        return PizzaDto.builder()
                .id(pizza.getId())
                .name(pizza.getName())
                .description(pizza.getDescription())
                .price(pizza.getPrice())
                .size(pizza.getSize())
                .build();
    }

    public static List<PizzaDto> from(List<Pizza> pizzas) {
        return pizzas
                .stream()
                .map(PizzaDto::from)
                .collect(Collectors.toList());
    }
}
