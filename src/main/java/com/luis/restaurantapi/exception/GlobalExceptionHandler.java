package com.luis.restaurantapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ProblemDetail;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
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

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(status);

        problem.setTitle("Método não permitido");
        problem.setDetail("O método HTTP utilizado não é permitido para esta rota.");
        problem.setProperty("error", "Method Not Allowed");
        problem.setProperty("code", "METHOD_NOT_ALLOWED");
        problem.setInstance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()));

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(status);

        problem.setTitle("Item inválido");
        problem.setDetail("Um ou mais campos são inválidos.");
        problem.setProperty("error", "Bad Request");
        problem.setProperty("code", "ITEM_NOT_VALID");
        problem.setInstance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()));

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> {
                    errors.put(
                            fieldError.getField(),
                            fieldError.getDefaultMessage()
                    );
                });

        problem.setProperty("fields", errors);

        return handleExceptionInternal(exception, problem, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleServerInternalError(Exception exception, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        problem.setTitle("Erro interno do servidor");
        problem.setDetail("Ocorreu um erro inesperado.");
        problem.setProperty("error", "Server Internal Error");
        problem.setProperty("code", "SERVER_INTERNAL_ERROR");
        problem.setInstance(URI.create(request.getRequestURI()));

        return problem;
    }
}