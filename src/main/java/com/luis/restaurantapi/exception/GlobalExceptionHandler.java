package com.luis.restaurantapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ItemNotFoundException.class)
    public ProblemDetail handleItemNotFound(ItemNotFoundException exception, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problem.setTitle("Item não encontrado");
        problem.setDetail(exception.getMessage());
        problem.setProperty("error", "Not Found");
        problem.setProperty("code", "ITEM_NOT_FOUND");
        problem.setInstance(URI.create(request.getRequestURI()));

        return problem;
    }
}