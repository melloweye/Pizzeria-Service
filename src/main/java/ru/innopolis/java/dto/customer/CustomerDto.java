package ru.innopolis.java.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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

    @Schema(name = "Customer's name", example = "John Doe")
    private String fullName;

    @Email
    @Schema(name = "Customer's email", example = "john_doe@gmail.com")
    private String email;

    @Schema(name = "Customer's contact phone", example = "+12345")
    private String phone;

    @Schema(name = "Customer's address", example = "9, Main Street")
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
