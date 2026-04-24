package com.challenge.hotelsearch.search.infrastructure.validation.validator;

import com.challenge.hotelsearch.search.infrastructure.rest.request.SearchCreatedRequest;
import com.challenge.hotelsearch.search.infrastructure.validation.annotation.ValidDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, SearchCreatedRequest> {

    @Override
    public boolean isValid(SearchCreatedRequest dto, ConstraintValidatorContext context) {
        if (dto.checkIn() == null || dto.checkOut() == null) {
            return true;
        }

        if (!dto.checkIn().isBefore(dto.checkOut())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("checkIn")
                    .addConstraintViolation();
            return false;
        }
        return true;

    }
}
