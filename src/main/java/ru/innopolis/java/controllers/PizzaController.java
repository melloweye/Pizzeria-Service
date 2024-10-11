package ru.innopolis.java.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.innopolis.java.dto.pizza.PizzaDto;
import ru.innopolis.java.dto.pizza.PizzaDtoShort;
import ru.innopolis.java.services.PizzaService;

import java.util.List;

@Tag(name = "Pizza API", description = "Взаимодействие с покупками")
@RequiredArgsConstructor
@RestController
@RequestMapping("/pizzas")
public class PizzaController {

    private final PizzaService pizzaService;

    // CREATE
    @PostMapping("/add")
    public ResponseEntity<PizzaDtoShort> createPizza(@RequestBody PizzaDtoShort pizzaDtoShort) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pizzaService.addPizza(pizzaDtoShort));
    }

    // READ
    @GetMapping
    public ResponseEntity<List<PizzaDto>> getAllPizzas() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pizzaService.getAll());
    }

    // READ - find by id
    @GetMapping("/{id}")
    public ResponseEntity<PizzaDto> getPizzaById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pizzaService.getPizzaById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PizzaDtoShort> updatePizza(@PathVariable("id") Long id, @RequestBody PizzaDtoShort newData) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pizzaService.updatePizza(id, newData));
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<PizzaDto> deletePizza(@PathVariable("id") Long id) {
        pizzaService.softDeleteById(id);
        return ResponseEntity.noContent().build();
    }
}
