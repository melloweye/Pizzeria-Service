package ru.innopolis.java.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.innopolis.java.dto.pizza.PizzaDto;
import ru.innopolis.java.dto.pizza.PizzaDtoShort;
import ru.innopolis.java.exceptions.PizzaNotFoundException;
import ru.innopolis.java.models.Pizza;
import ru.innopolis.java.repositories.PizzaRepository;
import ru.innopolis.java.services.PizzaService;

import java.util.List;

import static ru.innopolis.java.dto.pizza.PizzaDto.from;

@Service
@RequiredArgsConstructor
public class PizzaServiceImpl implements PizzaService {

    private final PizzaRepository pizzaRepository;

    @Override
    public List<PizzaDto> getAll() {
        return from(pizzaRepository.findAllNotDeleted());
    }

    @Override
    public PizzaDto getPizzaById(Long id) {
        Pizza pizza = pizzaRepository.findById(id).orElseThrow(() -> new PizzaNotFoundException("Pizza with id " + id + " not found"));

        if (pizza.isIdDeleted()) {
            throw new PizzaNotFoundException("Pizza with id " + id + " not found");
        }

        return from(pizza);
    }

    @Override
    public void softDeleteById(Long id) {

        Pizza pizza = pizzaRepository.findById(id).orElseThrow(() -> new PizzaNotFoundException("Pizza with id " + id + " not found"));

        if (pizza.isIdDeleted()) {
            throw new PizzaNotFoundException("Pizza with id " + id + " ia already deleted");
        }

        pizza.setIdDeleted(true);
        pizzaRepository.save(pizza);
    }

    @Override
    public PizzaDtoShort addPizza(PizzaDtoShort pizza) {
        return PizzaDtoShort.from(pizzaRepository.save(
                Pizza.builder()
                        .name(pizza.getName())
                        .description(pizza.getDescription())
                        .price(pizza.getPrice())
                        .size(pizza.getSize())
                        .build()
        ));
    }

    @Override
    public PizzaDtoShort updatePizza(Long id, PizzaDtoShort newData) {
        Pizza pizza = pizzaRepository.findById(id).orElseThrow(() -> new PizzaNotFoundException("Pizza with id " + id + " not found"));

        pizza.setName(newData.getName());
        pizza.setDescription(newData.getDescription());
        pizza.setPrice(newData.getPrice());
        pizza.setSize(newData.getSize());

        return PizzaDtoShort.from(pizzaRepository.save(pizza));
    }
}
