package com.challenge.hotelsearch.search.application.service;

import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.domain.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SaveSearchService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final SearchRepository searchRepository;

    public void save(Search search) {
        String hash = generateHash(search);

        Search withHash = Search.builder()
                .searchId(search.getSearchId())
                .hotelId(search.getHotelId())
                .checkIn(search.getCheckIn())
                .checkOut(search.getCheckOut())
                .ages(search.getAges())
                .hash(hash)
                .build();

        searchRepository.save(withHash);
    }

    private String generateHash(Search search) {
        String input = String.join("|",
                search.getHotelId(),
                search.getCheckIn().format(FORMATTER),
                search.getCheckOut().format(FORMATTER),
                search.getAges()
        );
        return DigestUtils.sha256Hex(input);
    }
}
