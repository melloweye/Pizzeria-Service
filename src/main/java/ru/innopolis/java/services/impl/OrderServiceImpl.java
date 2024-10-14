package ru.innopolis.java.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.innopolis.java.dto.order.OrderDto;
import ru.innopolis.java.dto.order.OrderDtoShort;
import ru.innopolis.java.exceptions.CustomerNotFoundException;
import ru.innopolis.java.exceptions.OrderNotFoundException;
import ru.innopolis.java.exceptions.PizzaNotFoundException;
import ru.innopolis.java.models.Customer;
import ru.innopolis.java.models.Order;
import ru.innopolis.java.models.Pizza;
import ru.innopolis.java.repositories.CustomerRepository;
import ru.innopolis.java.repositories.OrderRepository;
import ru.innopolis.java.repositories.PizzaRepository;
import ru.innopolis.java.services.OrderService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.innopolis.java.dto.order.OrderDto.from;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final PizzaRepository pizzaRepository;

    @Override
    public List<OrderDto> getAll() {
        if(OrderDto.from(orderRepository.findAllNotDeleted()).isEmpty()) {
            throw new OrderNotFoundException("No orders found");
        }

       return OrderDto.from(orderRepository.findAllNotDeleted());
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));

        if (order.isDeleted()) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }

        return from(order);
    }

    @Override
    public void softDeleteById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));

        if (order.isDeleted()) {
            throw new OrderNotFoundException("Order with id " + id + " is already deleted");
        }

        order.setDeleted(true);
        orderRepository.save(order);
    }

    @Override
    public OrderDtoShort addOrder(OrderDtoShort order) {

        Customer customer = customerRepository.findById(order.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + order.getCustomerId() + " not found"));

        Set<Pizza> pizzas = order.getPizzaIds().stream()
                .map(pizzaId -> pizzaRepository.findById(pizzaId)
                        .orElseThrow(() -> new PizzaNotFoundException("Pizza with id " + pizzaId + " not found")))
                .collect(Collectors.toSet());

        return OrderDtoShort.from(orderRepository.save(
                Order.builder()
                        .customer(customer)
                        .pizzas(pizzas)
                        .totalPrice(order.getTotalPrice())
                        .orderDate(order.getOrderDate())
                        .build()
        ));
    }

    @Override
    public OrderDtoShort updateOrder(Long id, OrderDtoShort newData) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));

        Customer customer = customerRepository.findById(newData.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + newData.getCustomerId() + " not found"));

        Set<Pizza> pizzas = newData.getPizzaIds().stream()
                .map(pizzaId -> pizzaRepository.findById(pizzaId).orElseThrow(() -> new PizzaNotFoundException("Pizza with id " + pizzaId + " not found")))
                .collect(Collectors.toSet());

        order.setCustomer(customer);
        order.setPizzas(pizzas);
        order.setTotalPrice(newData.getTotalPrice());
        order.setOrderDate(newData.getOrderDate());

        return OrderDtoShort.from(orderRepository.save(order));
    }
}
