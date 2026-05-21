package com.challenge.hotelsearch.search.application.port.out;

import com.challenge.hotelsearch.search.domain.model.Search;

import java.util.Optional;

public interface SearchRepositoryPort {
    void save(Search search);
    Optional<Search> findBySearchId(String searchId);
    long countByHash(String hash);
}
