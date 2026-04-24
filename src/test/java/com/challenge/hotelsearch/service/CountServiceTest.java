package com.challenge.hotelsearch.service;

import com.challenge.hotelsearch.search.application.dto.CountResultDTO;
import com.challenge.hotelsearch.search.application.exception.SearchNotFoundException;
import com.challenge.hotelsearch.search.application.service.CountService;
import com.challenge.hotelsearch.search.domain.model.Search;
import com.challenge.hotelsearch.search.domain.repository.SearchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountServiceTest {

    @Mock
    private SearchRepository searchRepository;

    @InjectMocks
    private CountService countService;

    @Test
    void shouldReturnCountResponseWhenSearchExists() {
        Search search = Search.builder()
                .searchId("search-1")
                .hash("hash-123")
                .hotelId("123")
                .checkIn(LocalDate.of(2026, 4, 20))
                .checkOut(LocalDate.of(2026, 4, 27))
                .ages("10,70")
                .build();

        when(searchRepository.findBySearchId("search-1")).thenReturn(Optional.of(search));
        when(searchRepository.countByHash("hash-123")).thenReturn(5L);

        CountResultDTO result = countService.count("search-1");

        assertAll(
            () -> assertEquals("search-1", result.searchId()),
            () -> assertEquals(search, result.search()),
            () -> assertEquals(5L, result.count())
        );
    }

    @Test
    void shouldThrowWhenSearchNotFound() {
        when(searchRepository.findBySearchId("missing")).thenReturn(Optional.empty());

        assertThrows(SearchNotFoundException.class, () -> countService.count("missing"));
    }
}
