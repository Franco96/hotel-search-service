package com.challenge.hotelsearch.validator;

import com.challenge.hotelsearch.search.infrastructure.rest.request.SearchCreatedRequest;
import com.challenge.hotelsearch.search.infrastructure.validation.validator.DateRangeValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DateRangeValidatorTest {

    private final DateRangeValidator validator = new DateRangeValidator();
    private ConstraintValidatorContext context;
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;

    @BeforeEach
    void setUp() {
        context = mock(ConstraintValidatorContext.class);
        violationBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        nodeBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);

        when(context.getDefaultConstraintMessageTemplate()).thenReturn("checkIn must be before checkOut");
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode("checkIn")).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);
    }

    @Test
    void shouldReturnTrueWhenCheckInIsNull() {
        SearchCreatedRequest dto = new SearchCreatedRequest("hotel", null, LocalDate.of(2026, 4, 20), List.of(1));
        assertTrue(validator.isValid(dto, context));
    }

    @Test
    void shouldReturnTrueWhenCheckOutIsNull() {
        SearchCreatedRequest dto = new SearchCreatedRequest("hotel", LocalDate.of(2026, 4, 20), null, List.of(1));
        assertTrue(validator.isValid(dto, context));
    }

    @Test
    void shouldReturnTrueWhenCheckInIsBeforeCheckOut() {
        SearchCreatedRequest dto = new SearchCreatedRequest("hotel",  LocalDate.of(2026, 4, 20),  LocalDate.of(2026, 4, 27), List.of(1));
        assertTrue(validator.isValid(dto, context));
    }

    @Test
    void shouldReturnFalseWhenCheckInIsAfterCheckOut() {
        SearchCreatedRequest dto = new SearchCreatedRequest("hotel",  LocalDate.of(2026, 4, 26),  LocalDate.of(2026, 4, 20), List.of(1));

        assertFalse(validator.isValid(dto, context));

        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(any());
    }

    @Test
    void shouldReturnFalseWhenCheckInEqualsCheckOut() {
        SearchCreatedRequest dto = new SearchCreatedRequest("hotel", LocalDate.of(2026, 4, 20), LocalDate.of(2026, 4, 20), List.of(1));
        assertFalse(validator.isValid(dto, context));
    }
}
