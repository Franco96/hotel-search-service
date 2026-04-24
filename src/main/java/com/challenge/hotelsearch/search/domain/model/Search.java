package com.challenge.hotelsearch.search.domain.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Getter
@Builder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Search {

    private String searchId;
    private String hash;
    private String hotelId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String ages;
}
