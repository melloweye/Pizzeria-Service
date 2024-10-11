package ru.innopolis.java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.innopolis.java.models.Pizza;

import java.util.List;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    @Query(value = "select c from Pizza c where c.idDeleted = false")
    List<Pizza> findAllNotDeleted();
}
