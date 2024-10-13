package ru.innopolis.java.dto.pizza;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "Pizza name", example = "Margherita")
    private String name;

    @Schema(name = "Ingredients of a Pizza")
    private String description;

    @Schema(name = "Price if a Pizza", example = "100")
    private int price;

    @Schema(name = "Pizza size", example = "S/M/L")
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
