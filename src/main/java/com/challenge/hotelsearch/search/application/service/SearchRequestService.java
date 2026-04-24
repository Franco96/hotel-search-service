package com.challenge.hotelsearch.search.application.service;

import com.challenge.hotelsearch.search.application.port.SearchEventPublisher;
import com.challenge.hotelsearch.search.domain.model.Search;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchRequestService {

    private final SearchEventPublisher searchEventPublisher;

    public String createSearch(Search search) {
        String searchId = UUID.randomUUID().toString();

        Search identified = Search.builder()
                .searchId(searchId)
                .hotelId(search.getHotelId())
                .checkIn(search.getCheckIn())
                .checkOut(search.getCheckOut())
                .ages(search.getAges())
                .build();

        searchEventPublisher.publish(identified);
        return searchId;
    }
}
