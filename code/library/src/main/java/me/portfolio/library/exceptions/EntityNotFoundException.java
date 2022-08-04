package me.portfolio.library.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> c, String id) {
        super("Could not find " + c.getName() + " " + id);
    }
}
