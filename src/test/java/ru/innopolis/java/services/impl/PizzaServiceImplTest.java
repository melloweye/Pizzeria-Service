package ru.innopolis.java.services.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.innopolis.java.dto.pizza.PizzaDto;
import ru.innopolis.java.dto.pizza.PizzaDtoShort;
import ru.innopolis.java.models.Pizza;
import ru.innopolis.java.repositories.PizzaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Pizza Service работает, когда")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class PizzaServiceImplTest {

    @Mock
    private PizzaRepository pizzaRepository;

    @InjectMocks
    private PizzaServiceImpl pizzaService;

    private Pizza pizzaOne;
    private Pizza pizzaTwo;

    @BeforeEach
    void setUp() {
        pizzaOne = new Pizza();
        pizzaTwo = new Pizza();

        pizzaOne.setId(1L);
        pizzaOne.setName("Random Pizza");
        pizzaTwo.setSize("Small");

        pizzaTwo.setId(2L);
        pizzaTwo.setName("Margherita");
        pizzaTwo.setSize("Medium");
    }

    @AfterEach
    void tearDown() {
        pizzaOne = null;
        pizzaTwo = null;
    }

    @Test
    @Order(3)
    @DisplayName("получается список всех существующих пицц - R")
    void get_all_pizzas() {
        when(pizzaRepository.findAllNotDeleted()).thenReturn(Arrays.asList(pizzaOne, pizzaTwo));

        List<PizzaDto> allPizzas = pizzaService.getAll();

        assertNotNull(allPizzas);
        assertEquals(2, allPizzas.size());
        assertEquals(pizzaOne.getId(), allPizzas.get(0).getId());
        assertEquals(pizzaTwo.getId(), allPizzas.get(1).getId());

        verify(pizzaRepository, times(2)).findAllNotDeleted();
    }

    @Test
    @Order(2)
    @DisplayName("находится пицца по заданному id - R")
    void get_pizza_by_id() {
        when(pizzaRepository.findById(anyLong())).thenReturn(Optional.of(pizzaOne));

        PizzaDto result = pizzaService.getPizzaById(1L);

        assertNotNull(result);
        assertEquals(pizzaOne.getId(), result.getId());
        assertEquals(pizzaOne.getName(), result.getName());
        assertEquals(pizzaOne.getSize(), result.getSize());

        verify(pizzaRepository, times(1)).findById(anyLong());
    }

    @Test
    @Order(5)
    @DisplayName("удаляется пицца по заданному id - D")
    void soft_delete_pizza_by_id() {
        when(pizzaRepository.findById(anyLong())).thenReturn(Optional.of(pizzaOne));

        pizzaService.softDeleteById(pizzaOne.getId());

        assertTrue(pizzaOne.isIdDeleted());

        verify(pizzaRepository, times(1)).findById(anyLong());
    }

    @Test
    @Order(1)
    @DisplayName("создается запись о новой пицце - C")
    void add_new_pizza() {
        PizzaDtoShort pizzaDtoShort = PizzaDtoShort.builder()
                .name("Speciale")
                .size("Small")
                .price(120)
                .build();

        Pizza savedPizza = Pizza.builder()
                .name("Speciale")
                .size("Small")
                .price(120)
                .build();

        when(pizzaRepository.save(any(Pizza.class))).thenReturn(savedPizza);

        PizzaDtoShort result = pizzaService.addPizza(pizzaDtoShort);

        assertNotNull(savedPizza);
        assertEquals(savedPizza.getName(), result.getName());
        assertEquals(savedPizza.getSize(), result.getSize());
        assertEquals(savedPizza.getPrice(), result.getPrice());

        verify(pizzaRepository, times(1)).save(any(Pizza.class));

    }

    @Test
    @Order(4)
    @DisplayName("обновляется существующая пицца по id - U")
    void update_existing_pizza_by_id() {
        Pizza existingPizza = pizzaOne;

        PizzaDtoShort updatedPizzaDto = PizzaDtoShort.builder()
                .name("New Pizza")
                .size("Large")
                .build();

        Pizza updatedPizza = Pizza.builder()
                .name("New Pizza")
                .size("Large")
                .build();

        when(pizzaRepository.findById(anyLong())).thenReturn(Optional.of(existingPizza));
        when(pizzaRepository.save(any(Pizza.class))).thenReturn(updatedPizza);

        PizzaDtoShort result = pizzaService.updatePizza(existingPizza.getId(), updatedPizzaDto);

        assertNotNull(result);
        assertEquals(updatedPizzaDto.getName(), result.getName());
        assertEquals(updatedPizza.getSize(), result.getSize());

        verify(pizzaRepository, times(1)).findById(anyLong());
    }
}