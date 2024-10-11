package ru.innopolis.java.services;

import ru.innopolis.java.dto.pizza.PizzaDto;
import ru.innopolis.java.dto.pizza.PizzaDtoShort;

import java.util.List;

public interface PizzaService {

    // READ
    List<PizzaDto> getAll();

    // READ
    PizzaDto getPizzaById(Long id);

    // DELETE
    void softDeleteById(Long id);

    // CREATE
    PizzaDtoShort addPizza(PizzaDtoShort pizzaDtoShort);

    // UPDATE
    PizzaDtoShort updatePizza(Long id, PizzaDtoShort newData);
}
