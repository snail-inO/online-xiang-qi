package me.portfolio.application.aop;

import me.portfolio.library.exceptions.EntityNotFoundException;
import me.portfolio.library.exceptions.InvalidOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomControllerAdvice {
    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String EntityNotFoundHandler(EntityNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String InvalidOperationException(InvalidOperationException ex) {
        return ex.getMessage();
    }
}
