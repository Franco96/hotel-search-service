package com.challenge.hotelsearch.controller;

import com.challenge.hotelsearch.search.application.exception.SearchEventPublishException;
import com.challenge.hotelsearch.search.application.exception.SearchNotFoundException;
import com.challenge.hotelsearch.search.infrastructure.exception.RestExceptionHandler;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    void shouldHandleBodyValidation() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/search");

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(
                List.of(new FieldError("searchCreatedRequest", "hotelId", "hotelId is required"),
                        new FieldError("searchCreatedRequest", "hotelId", "must be alphanumeric")
                )
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

        ConstraintViolation<Object> violation1 = mock(ConstraintViolation.class);
        ConstraintViolation<Object> violation2 = mock(ConstraintViolation.class);

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation1);
        violations.add(violation2);

        Path path = mock(Path.class);
        when(path.toString()).thenReturn("count.searchId");

        when(ex.getConstraintViolations()).thenReturn(violations);
        when(violation1.getPropertyPath()).thenReturn(path);
        when(violation1.getMessage()).thenReturn("Value must follow pattern ^[a-zA-Z0-9-]+$.");
        when(violation2.getPropertyPath()).thenReturn(path);
        when(violation2.getMessage()).thenReturn("Value must be no longer than 100 characters.");

        ProblemDetail detail = handler.handleParamsValidation(ex, request);

        @SuppressWarnings("unchecked")
        var errors = (java.util.Map<String, String>) detail.getProperties().get("errors");

        assertAll(
            () -> assertEquals(400, detail.getStatus()),
            () -> assertEquals("Validation error", detail.getTitle()),
            () -> assertEquals("Invalid request parameters", detail.getDetail()),
            () -> assertEquals("/count", detail.getInstance().toString()),
            () -> assertNotNull(errors),
            () -> assertTrue(errors.get("count.searchId").contains("Value must follow pattern ^[a-zA-Z0-9-]+$.")),
            () -> assertTrue(errors.get("count.searchId").contains("Value must be no longer than 100 characters."))
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
    void shouldHandlePublishError() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/search");

        ProblemDetail detail = handler.handlePublishError(
                new SearchEventPublishException("Kafka unavailable"), request);

        assertAll(
            () -> assertEquals(503, detail.getStatus()),
            () -> assertEquals("Event publishing failed", detail.getTitle()),
            () -> assertEquals("Kafka unavailable", detail.getDetail()),
            () -> assertEquals("/search", detail.getInstance().toString())
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
