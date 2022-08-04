package me.portfolio.library.exceptions;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(Class<?> c, String id) {
        super("Invalid operation on " + c.getName() + " " + id);
    }
}
