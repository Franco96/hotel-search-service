package com.challenge.hotelsearch.search.infrastructure.rest.response;

import java.util.List;

public record SearchResponse(
    String hotelId,
    String checkIn,
    String checkOut,
    List<Integer> ages
) {}
