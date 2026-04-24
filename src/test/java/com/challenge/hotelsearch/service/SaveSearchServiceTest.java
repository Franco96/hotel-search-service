package com.challenge.hotelsearch.service;

import com.challenge.hotelsearch.search.application.service.SaveSearchService;
import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.domain.repository.SearchRepository;
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
    private SearchRepository repository;

    @InjectMocks
    private SaveSearchService service;

    @Test
    void shouldGenerateHashAndSave() {
        Search search = Search.builder()
                .searchId("search-test-id")
                .hotelId("123")
                .checkIn(LocalDate.of(2026, 4, 20))
                .checkOut(LocalDate.of(2026, 4, 27))
                .ages("10,70")
                .build();

        service.save(search);

        ArgumentCaptor<Search> captor = ArgumentCaptor.forClass(Search.class);
        verify(repository).save(captor.capture());
        Search saved = captor.getValue();

        assertAll(
            () -> assertNotNull(saved.getHash()),
            () -> assertEquals(64, saved.getHash().length()),
            () -> assertEquals("search-test-id", saved.getSearchId()),
            () -> assertEquals("123", saved.getHotelId()),
            () -> assertEquals(LocalDate.of(2026, 4, 20), saved.getCheckIn()),
            () -> assertEquals(LocalDate.of(2026, 4, 27), saved.getCheckOut())
        );
    }
}
