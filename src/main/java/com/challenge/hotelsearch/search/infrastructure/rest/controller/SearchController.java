package com.challenge.hotelsearch.search.infrastructure.rest.controller;

import com.challenge.hotelsearch.search.application.port.in.CreateSearchUseCase;
import com.challenge.hotelsearch.search.infrastructure.rest.request.SearchCreatedRequest;
import com.challenge.hotelsearch.search.infrastructure.rest.response.SearchCreatedResponse;
import com.challenge.hotelsearch.search.infrastructure.mapper.SearchMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@Tag(name = "Search")
public class SearchController {

    private final SearchMapper searchMapper;
    private final CreateSearchUseCase createSearchUseCase;

    public SearchController(SearchMapper searchMapper, CreateSearchUseCase createSearchUseCase) {
        this.searchMapper = searchMapper;
        this.createSearchUseCase = createSearchUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Create search",
            description = "Creates a new search and returns its identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Search created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service Unavailable",
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
    public ResponseEntity<SearchCreatedResponse> createSearch(@Valid @RequestBody SearchCreatedRequest request) {
        String searchId = createSearchUseCase.createSearch(searchMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(new SearchCreatedResponse(searchId));
    }
}
