package com.challenge.hotelsearch.search.infrastructure.rest.controller;

import com.challenge.hotelsearch.search.application.dto.CountResultDTO;
import com.challenge.hotelsearch.search.application.service.CountService;
import com.challenge.hotelsearch.search.infrastructure.rest.response.CountResponse;
import com.challenge.hotelsearch.search.infrastructure.mapper.SearchMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/count")
@Tag(name = "Count")
public class CountController {

    private final CountService countService;
    private final SearchMapper searchMapper;

    @GetMapping
    public CountResponse count(
            @RequestParam
            @NotBlank(message = "must not be blank")
            @Size(max = 100, message = "must be at most 100 characters")
            @Pattern(
                    regexp = "^[a-zA-Z0-9-]+$",
                    message = "must be a valid UUID"
            )
            String searchId) {

        CountResultDTO result = countService.count(searchId);

        return new CountResponse(
                result.searchId(),
                searchMapper.toSearchResponseDTO(result.search()),
                result.count()
        );
    }

}
