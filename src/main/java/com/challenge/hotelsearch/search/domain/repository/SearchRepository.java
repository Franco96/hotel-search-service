package com.challenge.hotelsearch.search.domain.repository;

import com.challenge.hotelsearch.search.domain.model.Search;

import java.util.Optional;

public interface SearchRepository {
    void save(Search search);
    Optional<Search> findBySearchId(String searchId);
    long countByHash(String hash);
}
