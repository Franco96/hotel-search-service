package com.challenge.hotelsearch.search.infrastructure.rest.controller;

import com.challenge.hotelsearch.search.application.service.SearchRequestService;
import com.challenge.hotelsearch.search.infrastructure.rest.request.SearchCreatedRequest;
import com.challenge.hotelsearch.search.infrastructure.rest.response.SearchCreatedResponse;
import com.challenge.hotelsearch.search.infrastructure.mapper.SearchMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(name = "Search")
public class SearchController {

    private final SearchMapper searchMapper;
    private final SearchRequestService searchRequestService;

    @PostMapping
    public SearchCreatedResponse createSearch(@Valid @RequestBody SearchCreatedRequest request) {
        String searchId = searchRequestService.createSearch(searchMapper.toEntity(request));
        return new SearchCreatedResponse(searchId);
    }
}
