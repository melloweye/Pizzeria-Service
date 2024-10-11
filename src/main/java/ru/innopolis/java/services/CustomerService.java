package ru.innopolis.java.services;

import ru.innopolis.java.dto.customer.CustomerDto;
import ru.innopolis.java.dto.customer.CustomerDtoShort;

import java.util.List;

public interface CustomerService {

    // READ
    List<CustomerDtoShort> getAll();

    // READ
    CustomerDtoShort getCustomerById(Long customerId);

    // DELETE
    void softDeleteById(Long id); // make soft delete here

    // CREATE
    CustomerDto addCustomer(CustomerDto customerDto);

    // UPDATE
    CustomerDto updateCustomer(Long customerId, CustomerDto newData);
}
