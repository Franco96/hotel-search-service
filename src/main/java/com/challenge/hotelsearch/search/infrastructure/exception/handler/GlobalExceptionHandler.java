package com.challenge.hotelsearch.search.infrastructure.exception.handler;

import com.challenge.hotelsearch.search.application.exception.SearchNotFoundException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;


import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String VALIDATION_TITLE = "Validation error";
    public static final String INVALID_DATE_MESSAGE =
            "Invalid date. Expected format dd/MM/yyyy with valid day, month and year";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleBodyValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> Optional.ofNullable(error.getDefaultMessage())
                                .orElse("Invalid value"),
                        (a, b) -> a
                ));

        return badRequest("Invalid request body", request, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleParamsValidation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (a, b) -> a
                ));

        return badRequest("Invalid request parameters", request, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        Throwable cause = ex.getCause();

        if (cause instanceof MismatchedInputException mismatched
                && !mismatched.getPath().isEmpty()) {

            String field = mismatched.getPath().getFirst().getFieldName();
            errors.put(field, INVALID_DATE_MESSAGE);

        } else {
            errors.put("body", "Invalid request body");
        }

        return badRequest("Invalid request body", request, errors);
    }

    @ExceptionHandler(SearchNotFoundException.class)
    public ProblemDetail handleSearchNotFound(
            SearchNotFoundException ex,
            HttpServletRequest request) {

        return build(HttpStatus.NOT_FOUND, "Not found",
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error",
                "Something went wrong", request, null);
    }

    private ProblemDetail badRequest(String detail,
                                     HttpServletRequest request,
                                     Map<String, String> errors) {

        return build(HttpStatus.BAD_REQUEST, VALIDATION_TITLE, detail, request, errors);
    }

    private ProblemDetail build(HttpStatus status,
                                String title,
                                String detail,
                                HttpServletRequest request,
                                Map<String, String> errors) {

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);
        problem.setInstance(URI.create(request.getRequestURI()));

        if (errors != null && !errors.isEmpty()) {
            problem.setProperty("errors", errors);
        }

        return problem;
    }

}