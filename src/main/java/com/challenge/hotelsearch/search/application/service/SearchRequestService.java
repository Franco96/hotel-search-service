package com.challenge.hotelsearch.search.application.service;

import com.challenge.hotelsearch.search.application.port.out.SearchEventPublisherPort;
import com.challenge.hotelsearch.search.application.port.in.CreateSearchUseCase;
import com.challenge.hotelsearch.search.domain.model.Search;

import java.util.UUID;

public class SearchRequestService implements CreateSearchUseCase {

    private final SearchEventPublisherPort searchEventPublisherPort;

    public SearchRequestService(SearchEventPublisherPort searchEventPublisherPort) {
        this.searchEventPublisherPort = searchEventPublisherPort;
    }

    @Override
    public String createSearch(Search search) {
        String searchId = UUID.randomUUID().toString();

        Search identified = new Search(
                searchId,
                search.hash(),
                search.hotelId(),
                search.checkIn(),
                search.checkOut(),
                search.ages()
        );

        searchEventPublisherPort.publish(identified);
        return searchId;
    }
}
