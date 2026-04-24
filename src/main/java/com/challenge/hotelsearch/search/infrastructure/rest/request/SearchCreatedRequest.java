package com.challenge.hotelsearch.search.infrastructure.rest.request;

import com.challenge.hotelsearch.search.infrastructure.validation.annotation.ValidDateRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.annotation.OptBoolean.FALSE;

@ValidDateRange
public record SearchCreatedRequest(

        @Schema(description = "Hotel ID", example = "1234aBc")
        @NotBlank(message = "hotelId is required")
        @Size(max = 50, message = "hotelId must be at most 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "must be alphanumeric")
        String hotelId,

        @Schema(description = "Check-in date in format dd/MM/yyyy", example = "20/04/2026")
        @NotNull(message = "checkIn is required")
        @JsonFormat(pattern = "dd/MM/uuuu", lenient = FALSE)
        LocalDate checkIn,

        @Schema(description = "Check-out date in format dd/MM/yyyy", example = "27/04/2026")
        @NotNull(message = "checkOut is required")
        @JsonFormat(pattern = "dd/MM/uuuu", lenient = FALSE)
        LocalDate checkOut,

        @Schema(description = "List of guest ages", example = "[29, 29, 0]")
        @NotNull(message = "ages cannot be null")
        @Size(min = 1, max = 10, message = "ages must have between 1 and 10 values")
        List<
                @NotNull(message = "age cannot be null")
                @Min(value = 0, message = "age must be >= 0")
                @Max(value = 120, message = "age must be <= 120")
                Integer
        > ages

) {}
