package com.challenge.hotelsearch.search.application.port;

import com.challenge.hotelsearch.search.domain.model.Search;

public interface SearchEventPublisher {

    void publish(Search search);
}
