package com.challenge.hotelsearch.search.infrastructure.validation.annotation;

import com.challenge.hotelsearch.search.infrastructure.validation.validator.DateRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {

    String message() default "checkIn must be before checkOut";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
