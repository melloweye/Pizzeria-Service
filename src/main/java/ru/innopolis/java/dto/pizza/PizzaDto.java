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
public class PizzaDto {

    @Schema(name = "Pizza id", example = "1")
    private Long id;

    @Schema(name = "Pizza name", example = "Margherita")
    private String name;

    @Schema(name = "Ingredients of a Pizza")
    private String description;

    @Schema(name = "Price if a Pizza", example = "100")
    private int price;

    @Schema(name = "Pizza size", example = "S/M/L")
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
