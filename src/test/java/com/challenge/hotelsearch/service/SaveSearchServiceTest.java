package com.challenge.hotelsearch.service;

import com.challenge.hotelsearch.search.application.service.SaveSearchService;
import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.application.port.out.SearchRepositoryPort;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SaveSearchServiceTest {

    @Mock
    private SearchRepositoryPort repository;

    @InjectMocks
    private SaveSearchService service;

    @Test
    void shouldGenerateHashAndSave() {
        Search search = new Search(
                "search-test-id",
                null,
                "123",
                LocalDate.of(2026, 4, 20),
                LocalDate.of(2026, 4, 27),
                "10,70"
        );

        service.save(search);

        ArgumentCaptor<Search> captor = ArgumentCaptor.forClass(Search.class);
        verify(repository).save(captor.capture());
        Search saved = captor.getValue();

        String expectedHash = DigestUtils.sha256Hex("123|20/04/2026|27/04/2026|10,70");

        assertAll(
            () -> assertEquals(expectedHash, saved.hash()),
            () -> assertEquals(64, saved.hash().length()),
            () -> assertEquals("search-test-id", saved.searchId()),
            () -> assertEquals("123", saved.hotelId()),
            () -> assertEquals(LocalDate.of(2026, 4, 20), saved.checkIn()),
            () -> assertEquals(LocalDate.of(2026, 4, 27), saved.checkOut())
        );
    }
}
