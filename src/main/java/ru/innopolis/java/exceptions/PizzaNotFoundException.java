package ru.innopolis.java.exceptions;

public class PizzaNotFoundException extends RuntimeException {
    public PizzaNotFoundException(String message) {
        super(message);
    }
}
