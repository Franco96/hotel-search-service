package com.challenge.hotelsearch.search.domain.model;

import java.time.LocalDate;

public record Search (
        String searchId,
        String hash,
        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        String ages
) {}
