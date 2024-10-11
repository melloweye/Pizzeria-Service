package ru.innopolis.java.dto.customer;

import lombok.*;
import ru.innopolis.java.models.Customer;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDtoShort {

    private Long id;

    private String fullName;

    public static CustomerDtoShort from(Customer customer) {
        return CustomerDtoShort.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .build();
    }

    public static List<CustomerDtoShort> from(List<Customer> customers) {
        return customers
                .stream()
                .map(CustomerDtoShort::from)
                .collect(Collectors.toList());
    }
}
