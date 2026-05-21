package com.challenge.hotelsearch.search.infrastructure.exception;

import com.challenge.hotelsearch.search.application.exception.SearchEventPublishException;
import com.challenge.hotelsearch.search.application.exception.SearchNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;


import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    private static final String VALIDATION_TITLE = "Validation error";
    private static final String INVALID_REQUEST_PARAMETER = "Invalid request parameters";
    private static final String INVALID_REQUEST_BODY = "Invalid request body";


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error on {}", request.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error",
                "Something went wrong", request, null);
    }

    @ExceptionHandler(SearchEventPublishException.class)
    public ProblemDetail handlePublishError(SearchEventPublishException ex, HttpServletRequest request) {
        log.error("Kafka publish error", ex);
        return build(HttpStatus.SERVICE_UNAVAILABLE, "Event publishing failed",
                ex.getMessage(), request, null);
    }

    @ExceptionHandler(SearchNotFoundException.class)
    public ProblemDetail handleSearchNotFound(SearchNotFoundException ex, HttpServletRequest request) {
        log.error("Search not found error", ex);
        return build(HttpStatus.NOT_FOUND, "Not found",
                ex.getMessage(), request, null);
    }

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
                        (a, b) ->  String.join(", ", a, b)
                ));

        return badRequest(INVALID_REQUEST_BODY, request, errors);
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
                        (a, b) -> String.join(", ", a, b)
                ));

        return badRequest(INVALID_REQUEST_PARAMETER, request, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        return badRequest(INVALID_REQUEST_BODY, request, Map.of("body", INVALID_REQUEST_BODY));
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

        if (errors != null) {
            problem.setProperty("errors", errors);
        }

        return problem;
    }

}