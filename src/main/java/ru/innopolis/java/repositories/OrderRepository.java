package ru.innopolis.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.innopolis.java.models.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "select c from Order c where c.isDeleted = false")
    List<Order> findAllNotDeleted();
}
