package com.challenge.hotelsearch.search.application.service;

import com.challenge.hotelsearch.search.application.dto.CountResultDTO;
import com.challenge.hotelsearch.search.application.exception.SearchNotFoundException;
import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.domain.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountService {

    private final SearchRepository searchRepository;

    public CountResultDTO count(String searchId) {
        Search search = searchRepository.findBySearchId(searchId)
                .orElseThrow(() -> new SearchNotFoundException("Search not found"));

        long count = searchRepository.countByHash(search.getHash());
        return new CountResultDTO(searchId, search, count);
    }
}
