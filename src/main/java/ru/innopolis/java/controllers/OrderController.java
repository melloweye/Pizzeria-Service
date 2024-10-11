package ru.innopolis.java.controllers;

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
    @PostMapping("/add")
    public ResponseEntity<OrderDtoShort> addOrder(@RequestBody OrderDtoShort orderDtoShort) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.addOrder(orderDtoShort));
    }

    // READ
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getAll());
    }

    // READ - find by id
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getOrderById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<OrderDtoShort> updateOrder(@PathVariable("id") Long id, @RequestBody OrderDtoShort newData) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.updateOrder(id, newData));
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<OrderDto> deleteOrder(@PathVariable("id") Long id) {
        orderService.softDeleteById(id);
        return ResponseEntity.noContent().build();
    }
}
