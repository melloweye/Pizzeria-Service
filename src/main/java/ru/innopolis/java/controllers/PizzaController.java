package ru.innopolis.java.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Create Pizza", description = "Creates a new Pizza")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully created"),
    })
    @PostMapping("/add")
    public ResponseEntity<PizzaDtoShort> createPizza(@RequestBody PizzaDtoShort pizzaDtoShort) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pizzaService.addPizza(pizzaDtoShort));
    }

    // READ
    @Operation(summary = "Get all Pizzas", description = "Returns list of all Pizzas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @GetMapping
    public ResponseEntity<List<PizzaDto>> getAllPizzas() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pizzaService.getAll());
    }

    // READ - find by id
    @Operation(summary = "Get Pizza by id", description = "Returns Pizza as per id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - Customer was not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PizzaDto> getPizzaById(@PathVariable("id") @Parameter(name = "id", description = "Pizza id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pizzaService.getPizzaById(id));
    }

    // UPDATE
    @Operation(summary = "Update existing Pizza by id", description = "Updates existing Pizza as per id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "404", description = "Not Found - Nothing to update")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PizzaDtoShort> updatePizza(@PathVariable("id") @Parameter(name = "id", description = "Pizza id") Long id, @RequestBody PizzaDtoShort newData) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pizzaService.updatePizza(id, newData));
    }

    // DELETE
    @Operation(summary = "Delete Pizza by id", description = "Deletes Pizza as per id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Not Found - Nothing to delete")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<PizzaDto> deletePizza(@PathVariable("id") @Parameter(name = "id", description = "Pizza id") Long id) {
        pizzaService.softDeleteById(id);
        return ResponseEntity.noContent().build();
    }
}
