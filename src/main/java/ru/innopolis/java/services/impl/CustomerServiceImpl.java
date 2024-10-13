package ru.innopolis.java.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.innopolis.java.dto.customer.CustomerDto;
import ru.innopolis.java.dto.customer.CustomerDtoShort;
import ru.innopolis.java.exceptions.CustomerNotFoundException;
import ru.innopolis.java.models.Customer;
import ru.innopolis.java.repositories.CustomerRepository;
import ru.innopolis.java.services.CustomerService;

import java.util.List;

import static ru.innopolis.java.dto.customer.CustomerDto.from;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerDtoShort> getAll() {
        return CustomerDtoShort.from(customerRepository.findAllNotDeleted());
    }

    @Override
    public CustomerDtoShort getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("Customer with id " + customerId + " not found"));
        if (customer.isDeleted()) {
            throw new CustomerNotFoundException("Customer with id " + customerId + " not found");
        }

        return CustomerDtoShort.from(customer);
    }

    @Override
    public void softDeleteById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));

        if (customer.isDeleted()) {
            throw new CustomerNotFoundException("Customer with id " + id + " is already deleted");
        }

        customer.setDeleted(true);
        customerRepository.save(customer);
    }

    @Override
    public CustomerDto addCustomer(CustomerDto customer) {
        return from(customerRepository.save(
                Customer.builder()
                        .fullName(customer.getFullName())
                        .phoneNumber(customer.getPhone())
                        .email(customer.getEmail())
                        .address(customer.getAddress())
                        .build()
        ));
    }

    @Override
    public CustomerDto updateCustomer(Long customerId, CustomerDto newData) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer with id " + customerId + " not found"));

        customer.setFullName(newData.getFullName());
        customer.setEmail(newData.getEmail());
        customer.setPhoneNumber(newData.getPhone());
        customer.setAddress(newData.getAddress());

        return from(customerRepository.save(customer));
    }
}
