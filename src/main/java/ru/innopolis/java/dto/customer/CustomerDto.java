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
public class CustomerDto {

    private String fullName;

    private String email;

    private String phone;

    private String address;

    public static CustomerDto from(Customer customer) {
        return CustomerDto.builder()
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phone(customer.getPhoneNumber())
                .address(customer.getAddress())
                .build();
    }

    public static List<CustomerDto> from(List<Customer> customers) {
        return customers
                .stream()
                .map(CustomerDto::from)
                .collect(Collectors.toList());
    }
}
