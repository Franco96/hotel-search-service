package com.challenge.hotelsearch.controller;

import com.challenge.hotelsearch.search.application.exception.SearchNotFoundException;
import com.challenge.hotelsearch.search.infrastructure.exception.handler.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.challenge.hotelsearch.search.infrastructure.exception.handler.GlobalExceptionHandler.INVALID_DATE_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleBodyValidation() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/search");

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(
                Collections.singletonList(new FieldError("object", "hotelId", "must not be blank"))
        );

        ProblemDetail detail = handler.handleBodyValidation(ex, request);

        assertAll(
            () -> assertEquals(400, detail.getStatus()),
            () -> assertEquals("Validation error", detail.getTitle()),
            () -> assertEquals("Invalid request body", detail.getDetail()),
            () -> assertEquals("/search", detail.getInstance().toString()),
            () -> assertNotNull(detail.getProperties()),
            () -> assertTrue(detail.getProperties().containsKey("errors"))
        );
    }

    @Test
    void shouldHandleParamsValidation() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/count");

        ConstraintViolationException ex = mock(ConstraintViolationException.class);

        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        Path path = mock(Path.class);
        when(path.toString()).thenReturn("count.searchId");

        when(ex.getConstraintViolations()).thenReturn(violations);
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be blank");

        ProblemDetail detail = handler.handleParamsValidation(ex, request);

        assertAll(
            () -> assertEquals(400, detail.getStatus()),
            () -> assertEquals("Validation error", detail.getTitle()),
            () -> assertEquals("Invalid request parameters", detail.getDetail()),
            () -> assertEquals("/count", detail.getInstance().toString())
        );
    }

    @Test
    void shouldHandleHttpMessageNotReadableWithInvalidFormatException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/search");

        InvalidFormatException cause = InvalidFormatException.from(
                null,
                "Invalid date",
                "31-12-2023",
                LocalDate.class
        );

        cause.prependPath(Object.class, "checkIn");

        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
        when(ex.getCause()).thenReturn(cause);

        ProblemDetail detail = handler.handleHttpMessageNotReadable(ex, request);

        Map<String, String> errors =
                (Map<String, String>) detail.getProperties().get("errors");

        assertAll(
                () -> assertEquals(400, detail.getStatus()),
                () -> assertEquals("Validation error", detail.getTitle()),
                () -> assertEquals("Invalid request body", detail.getDetail()),
                () -> assertTrue(errors.containsKey("checkIn")),
                () -> assertEquals(INVALID_DATE_MESSAGE, errors.get("checkIn"))
        );
    }

    @Test
    void shouldHandleHttpMessageNotReadable() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/search");
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
        when(ex.getMessage()).thenReturn("checkIn invalid");
        ProblemDetail detail = handler.handleHttpMessageNotReadable(ex, request);

        assertAll(
            () -> assertEquals(400, detail.getStatus()),
            () -> assertEquals("Validation error", detail.getTitle()),
            () -> assertEquals("Invalid request body", detail.getDetail())
        );
    }

    @Test
    void shouldHandleNotFound() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/count");

        ProblemDetail detail = handler.handleSearchNotFound(new SearchNotFoundException("Search not found"), request);

        assertAll(
            () -> assertEquals(404, detail.getStatus()),
            () -> assertEquals("Not found", detail.getTitle()),
            () -> assertEquals("Search not found", detail.getDetail())
        );
    }

    @Test
    void shouldHandleGenericException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/any");

        ProblemDetail detail = handler.handleGeneric(new RuntimeException("boom"), request);

        assertAll(
            () -> assertEquals(500, detail.getStatus()),
            () -> assertEquals("Unexpected error", detail.getTitle()),
            () -> assertEquals("Something went wrong", detail.getDetail())
        );
    }
}
