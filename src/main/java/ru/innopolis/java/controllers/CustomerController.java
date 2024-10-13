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
    @Operation(summary = "Create Customer", description = "Creates a new Customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully created"),
    })
    @PostMapping("/add")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.addCustomer(customerDto));
    }

    // READ
    @Operation(summary = "Get all Customers", description = "Returns list of all Customers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    @GetMapping
    public ResponseEntity<List<CustomerDtoShort>> getAllCustomers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.getAll());
    }

    // READ - find by id
    @Operation(summary = "Get Customer by id", description = "Returns Customer as per id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - Customer was not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDtoShort> getCustomerById(@PathVariable("id") @Parameter(name = "id", description = "Customer id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.getCustomerById(id));
    }

    // UPDATE - by id
    @Operation(summary = "Update existing Customer by id", description = "Updates existing Customer as per id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "404", description = "Not Found - Nothing to update")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("id") @Parameter(name = "id", description = "Customer id") Long id, @RequestBody CustomerDto newData) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.updateCustomer(id, newData));
    }

    // DELETE - soft flag
    @Operation(summary = "Delete Customer by id", description = "Deletes Customer as per id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Not Found - Nothing to delete")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomerDto> deleteCustomer(@PathVariable("id") @Parameter(name = "id", description = "Customer id") Long id) {
        customerServiceImpl.softDeleteById(id);
        return ResponseEntity.noContent().build();
    }
}
