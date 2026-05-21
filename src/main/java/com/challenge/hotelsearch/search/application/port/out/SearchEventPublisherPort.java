package com.challenge.hotelsearch.search.application.port.out;

import com.challenge.hotelsearch.search.domain.model.Search;

public interface SearchEventPublisherPort {
    void publish(Search search);
}
