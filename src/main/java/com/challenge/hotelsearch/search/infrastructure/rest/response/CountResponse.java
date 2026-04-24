package com.challenge.hotelsearch.search.infrastructure.rest.response;

public record CountResponse(
        String searchId,
        SearchResponse search,
        Long count
) {}
