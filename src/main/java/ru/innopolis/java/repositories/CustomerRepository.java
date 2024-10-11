package ru.innopolis.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.innopolis.java.models.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = "select c from Customer c where c.isDeleted = false")
    List<Customer> findAllNotDeleted();
}
