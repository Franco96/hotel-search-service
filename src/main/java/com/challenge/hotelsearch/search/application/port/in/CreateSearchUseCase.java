package com.challenge.hotelsearch.search.application.port.in;

import com.challenge.hotelsearch.search.domain.model.Search;

public interface CreateSearchUseCase {
    String createSearch(Search search);
}
