package com.challenge.hotelsearch.search.application.dto;

import com.challenge.hotelsearch.search.domain.model.Search;

public record CountResultDTO(String searchId, Search search, long count) {}
