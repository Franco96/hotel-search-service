package com.challenge.hotelsearch.search.application.service;

import com.challenge.hotelsearch.search.application.dto.CountResultDTO;
import com.challenge.hotelsearch.search.application.exception.SearchNotFoundException;
import com.challenge.hotelsearch.search.application.port.in.CountSearchUseCase;
import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.application.port.out.SearchRepositoryPort;


public class CountService implements CountSearchUseCase {

    private final SearchRepositoryPort searchRepositoryPort;

    public CountService(SearchRepositoryPort searchRepositoryPort) {
        this.searchRepositoryPort = searchRepositoryPort;
    }

    @Override
    public CountResultDTO count(String searchId) {
        Search search = searchRepositoryPort.findBySearchId(searchId)
                .orElseThrow(() -> new SearchNotFoundException("Search not found"));

        long count = searchRepositoryPort.countByHash(search.hash());
        return new CountResultDTO(searchId, search, count);
    }
}
