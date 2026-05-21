package com.challenge.hotelsearch.search.infrastructure.validation.validator;

import com.challenge.hotelsearch.search.infrastructure.rest.request.SearchCreatedRequest;
import com.challenge.hotelsearch.search.infrastructure.validation.annotation.ValidDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, SearchCreatedRequest> {

    @Override
    public boolean isValid(SearchCreatedRequest dto, ConstraintValidatorContext context) {
        if (dto.checkIn() == null || dto.checkOut() == null) {
            return true;
        }

        LocalDate today = LocalDate.now();

        if (dto.checkIn().isBefore(today))
            return reject(context, "checkIn", "Past dates are not allowed");
        if (dto.checkOut().isBefore(today))
            return reject(context, "checkOut", "Past dates are not allowed");
        if (!dto.checkIn().isBefore(dto.checkOut()))
            return reject(context, "checkIn", context.getDefaultConstraintMessageTemplate());

        return true;
    }

    private boolean reject(ConstraintValidatorContext context, String field, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field)
                .addConstraintViolation();
        return false;
    }
}
