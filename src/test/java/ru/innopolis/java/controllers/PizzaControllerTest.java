package ru.innopolis.java.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.innopolis.java.dto.pizza.PizzaDto;
import ru.innopolis.java.dto.pizza.PizzaDtoShort;
import ru.innopolis.java.services.impl.PizzaServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PizzaController.class)
class PizzaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PizzaServiceImpl pizzaServiceImpl;

    PizzaDto pizzaDto;
    PizzaDtoShort pizzaDtoShort;

    @BeforeEach
    void setUp() {
        pizzaDto = PizzaDto.builder()
                .id(1L)
                .name("Hawaiian")
                .description("ingredients")
                .price(120)
                .size("Small")
                .build();

        pizzaDtoShort = PizzaDtoShort.builder()
                .name("Hawaiian")
                .description("ingredients")
                .price(120)
                .size("Small")
                .build();
    }

    @AfterEach
    void tearDown() {
        pizzaDto = null;
        pizzaDtoShort = null;
    }

    @Test
    void createPizza() throws Exception {
        when(pizzaServiceImpl.addPizza(any(PizzaDtoShort.class))).thenReturn(pizzaDtoShort);

        String pizzaJson = "{ \"name\": \"Hawaiian\", \"description\": \"ingredients\", \"price\": 120, \"size\": \"Small\" }";

        mockMvc.perform(post("/pizzas/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pizzaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Hawaiian"))
                .andExpect(jsonPath("$.description").value("ingredients"))
                .andExpect(jsonPath("$.price").value(120))
                .andExpect(jsonPath("$.size").value("Small"));

        verify(pizzaServiceImpl, times(1)).addPizza(any(PizzaDtoShort.class));
    }

    @Test
    void getAllPizzas() throws Exception {
        PizzaDto pizzaOne = new PizzaDto(1L, "Hawaiian", "ingredients", 120, "Small");
        PizzaDto pizzaTwo = new PizzaDto(2L, "Margherita", "ingredients", 150, "Medium");

        List<PizzaDto> pizzas = List.of(pizzaOne, pizzaTwo);

        when(pizzaServiceImpl.getAll()).thenReturn(pizzas);

        mockMvc.perform(get("/pizzas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Hawaiian"))
                .andExpect(jsonPath("$[1].name").value("Margherita"))
                .andExpect(jsonPath("$[0].size").value("Small"))
                .andExpect(jsonPath("$[1].size").value("Medium"));

        verify(pizzaServiceImpl, times(1)).getAll();
    }

    @Test
    void getPizzaById() throws Exception {

        when(pizzaServiceImpl.getPizzaById(1L)).thenReturn(pizzaDto);

        mockMvc.perform(get("/pizzas/{id}", pizzaDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hawaiian"))
                .andExpect(jsonPath("$.description").value("ingredients"))
                .andExpect(jsonPath("$.price").value(120))
                .andExpect(jsonPath("$.size").value("Small"));

        verify(pizzaServiceImpl, times(1)).getPizzaById(1L);
    }

    @Test
    void updatePizza() throws Exception {

        Long id = 1L;

        when(pizzaServiceImpl.updatePizza(eq(id), any(PizzaDtoShort.class))).thenReturn(pizzaDtoShort);

        mockMvc.perform(put("/pizzas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Hawaiian\", \"description\": \"ingredients\", \"price\": 120, \"size\": \"Small\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hawaiian"))
                .andExpect(jsonPath("$.description").value("ingredients"))
                .andExpect(jsonPath("$.price").value(120))
                .andExpect(jsonPath("$.size").value("Small"));

        verify(pizzaServiceImpl, times(1)).updatePizza(eq(id), any(PizzaDtoShort.class));
    }

    @Test
    void deletePizza() throws Exception {

        Long id = 1L;

        doNothing().when(pizzaServiceImpl).softDeleteById(id);

        mockMvc.perform(delete("/pizzas/delete/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(pizzaServiceImpl, times(1)).softDeleteById(id);
    }
}



























