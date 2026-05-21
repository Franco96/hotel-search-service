package com.challenge.hotelsearch.search.infrastructure.rest.controller;

import com.challenge.hotelsearch.search.application.dto.CountResultDTO;
import com.challenge.hotelsearch.search.application.port.in.CountSearchUseCase;
import com.challenge.hotelsearch.search.infrastructure.rest.response.CountResponse;
import com.challenge.hotelsearch.search.infrastructure.mapper.SearchMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/count")
@Tag(name = "Count")
public class CountController {

    private final CountSearchUseCase countSearchUseCase;
    private final SearchMapper searchMapper;

    public CountController(CountSearchUseCase countSearchUseCase, SearchMapper searchMapper) {
        this.countSearchUseCase = countSearchUseCase;
        this.searchMapper = searchMapper;
    }

    @GetMapping
    @Operation(
            summary = "Count searches",
            description = "Returns the number of matching searches for a given searchId"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns matching search count"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid searchId supplied",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Search not found",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public CountResponse count(
            @RequestParam
            @NotBlank(message = "must not be blank")
            @Size(max = 100, message = "must be at most 100 characters")
            @Pattern(
                    regexp = "^[a-zA-Z0-9-]+$",
                    message = "must be a valid UUID"
            )
            String searchId) {

        CountResultDTO result = countSearchUseCase.count(searchId);

        return new CountResponse(
                result.searchId(),
                searchMapper.toSearchResponseDTO(result.search()),
                result.count()
        );
    }

}
