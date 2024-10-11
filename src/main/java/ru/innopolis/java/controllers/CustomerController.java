package ru.innopolis.java.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.innopolis.java.dto.customer.CustomerDto;
import ru.innopolis.java.dto.customer.CustomerDtoShort;
import ru.innopolis.java.services.CustomerService;
import ru.innopolis.java.services.impl.CustomerServiceImpl;

import java.util.List;

@Tag(name = "Customer API", description = "Взаимодействие с покупателями")
@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerServiceImpl customerServiceImpl;

    // CREATE
    @PostMapping("/add")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.addCustomer(customerDto));
    }

    // READ
    @GetMapping
    public ResponseEntity<List<CustomerDtoShort>> getAllCustomers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.getAll());
    }

    // READ - find by id
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDtoShort> getCustomerById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.getCustomerById(id));
    }

    // UPDATE - by id
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerDto newData) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.updateCustomer(id, newData));
    }

    // DELETE - soft flag
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomerDto> deleteCustomer(@PathVariable("id") Long id) {
        customerServiceImpl.softDeleteById(id);
        return ResponseEntity.noContent().build();
    }
}
