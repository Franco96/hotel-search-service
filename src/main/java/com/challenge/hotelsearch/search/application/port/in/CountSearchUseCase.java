package com.challenge.hotelsearch.search.application.port.in;

import com.challenge.hotelsearch.search.application.dto.CountResultDTO;

public interface CountSearchUseCase {
    CountResultDTO count(String searchId);
}
