package com.challenge.hotelsearch.search.application.service;

import com.challenge.hotelsearch.search.application.port.in.SaveSearchUseCase;
import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.application.port.out.SearchRepositoryPort;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.format.DateTimeFormatter;

public class SaveSearchService implements SaveSearchUseCase {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final SearchRepositoryPort searchRepositoryPort;

    public SaveSearchService(SearchRepositoryPort searchRepositoryPort) {
        this.searchRepositoryPort = searchRepositoryPort;
    }

    @Override
    public void save(Search search) {
        String hash = generateHash(search);

        Search withHash = new Search(
                search.searchId(),
                hash,
                search.hotelId(),
                search.checkIn(),
                search.checkOut(),
                search.ages()
        );

        searchRepositoryPort.save(withHash);
    }

    private String generateHash(Search search) {
        String input = String.join("|",
                search.hotelId(),
                search.checkIn().format(FORMATTER),
                search.checkOut().format(FORMATTER),
                search.ages()
        );
        return DigestUtils.sha256Hex(input);
    }
}
