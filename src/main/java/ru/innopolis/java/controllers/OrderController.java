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
import ru.innopolis.java.dto.order.OrderDto;
import ru.innopolis.java.dto.order.OrderDtoShort;
import ru.innopolis.java.services.OrderService;

import java.util.List;

@Tag(name = "Order API", description = "Взаимодействие с заказами")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // CREATE
    @Operation(summary = "Create Order", description = "Creates a new Order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully created"),
    })
    @PostMapping("/add")
    public ResponseEntity<OrderDtoShort> addOrder(@RequestBody OrderDtoShort orderDtoShort) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.addOrder(orderDtoShort));
    }

    // READ
    @Operation(summary = "Get all Orders", description = "Returns list of all Orders")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getAll());
    }

    // READ - find by id
    @Operation(summary = "Get Order by id", description = "Returns Order as per id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - Customer was not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") @Parameter(name = "id", description = "Order id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getOrderById(id));
    }

    // UPDATE
    @Operation(summary = "Update existing Order by id", description = "Updates existing Order as per id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "404", description = "Not Found - Nothing to update")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderDtoShort> updateOrder(@PathVariable("id") @Parameter(name = "id", description = "Order id") Long id, @RequestBody OrderDtoShort newData) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.updateOrder(id, newData));
    }

    // DELETE
    @Operation(summary = "Delete Order by id", description = "Deletes Order as per id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Not Found - Nothing to delete")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<OrderDto> deleteOrder(@PathVariable("id") @Parameter(name = "id", description = "Order id") Long id) {
        orderService.softDeleteById(id);
        return ResponseEntity.noContent().build();
    }
}
